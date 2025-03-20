package com.example.minesweeper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
                chronometer.stop()
                showGameEndDialog(isVictory) }
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

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
