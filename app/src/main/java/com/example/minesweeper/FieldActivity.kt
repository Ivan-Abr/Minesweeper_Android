package com.example.minesweeper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class FieldActivity : AppCompatActivity() {

    private var n = 6
    private var m = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_field)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

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

        val game = MinesweeperGame(this, gridLayout, n, m, cellSize)
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
