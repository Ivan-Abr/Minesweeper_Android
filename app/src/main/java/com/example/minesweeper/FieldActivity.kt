package com.example.minesweeper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.DBHelper.Companion.COLUMN_FIELD_SIZE
import com.example.minesweeper.DBHelper.Companion.COLUMN_MINE_COUNT
import com.example.minesweeper.DBHelper.Companion.COLUMN_NAME
import com.example.minesweeper.DBHelper.Companion.COLUMN_TIME
import com.example.minesweeper.DBHelper.Companion.TABLE_NAME
import kotlin.random.Random

class FieldActivity : AppCompatActivity() {

    private var n = 6
    private var m = 10
    private lateinit var game: MinesweeperGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val buttonRestart: Button = findViewById(R.id.btnRestart)
        val buttonExit: Button = findViewById(R.id.btnExit)
        val gameText: TextView = findViewById(R.id.gameText)
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        val sharedPref: SharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        n = sharedPref.getInt("field_size", 5)
        m = sharedPref.getInt("mine_count", 5)
        gridLayout.columnCount = n
        gridLayout.rowCount = n

        val dynamicPadding = (16 - (n / 2)).coerceAtLeast(4)
        gridLayout.setPadding(
            dynamicPadding.dpToPx(),
            dynamicPadding.dpToPx(),
            dynamicPadding.dpToPx(),
            dynamicPadding.dpToPx()
        )

        val totalSize = 350 - 2 * dynamicPadding
        val cellSize = totalSize / n

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        game = MinesweeperGame(this, gridLayout, n, m, cellSize)
        gameText.text = m.toString()
        game.onFlagCountChanged = { count ->
            runOnUiThread { gameText.text = count.toString() }
        }
        game.gameEndListener = { isVictory ->
            runOnUiThread {
                val elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                showSaveResultDialog(elapsedTime)
                showGameEndDialog(isVictory)
                chronometer.stop()
            }
        }

        buttonRestart.setOnClickListener{
            gameText.text = m.toString()
            game = MinesweeperGame(this, gridLayout, n, m, cellSize)
            game.onFlagCountChanged = { count ->
                runOnUiThread { gameText.text = count.toString() }
            }
            game.gameEndListener = { isVictory ->
                runOnUiThread { showGameEndDialog(isVictory) }
            }
        }

        buttonExit.setOnClickListener{
            finish()
        }
    }

    private fun showGameEndDialog(isVictory: Boolean) {
        val message = if (isVictory) "ПОБЕДА" else "ПОРАЖЕНИЕ"
        val gameText: TextView = findViewById(R.id.gameText)
        gameText.text = message
    }

    @SuppressLint("InflateParams", "DefaultLocale")
    private fun showSaveResultDialog(elapsedTimeMillis: Long) {

        val seconds = (elapsedTimeMillis / 1000) % 60
        val minutes = (elapsedTimeMillis / (1000 * 60)) % 60
        val time = String.format("%02d:%02d", minutes, seconds)

        val resultInfo = "Размер поля: $n\nМин: $m\n Время: $time"
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_result, null)
        val tvResultInfo = dialogView.findViewById<TextView>(R.id.tvResultInfo)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        tvResultInfo.text = resultInfo

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isNotEmpty()) {
                saveResult(this, name, n, m, time)
                Toast.makeText(this, "Результат сохранён", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun saveResult(context: Context, name: String, fieldSize: Int, mineCount: Int, time: String) {
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_FIELD_SIZE, fieldSize)
            put(COLUMN_MINE_COUNT, mineCount)
            put(COLUMN_TIME, time)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
}
