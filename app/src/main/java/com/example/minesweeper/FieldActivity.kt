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

        val field = generateField(n, m)

        for (i in 0 until n ) {
            for (j in 0 until n) {
            val button = Button(this).apply {
                text = ""
                gravity = Gravity.CENTER
                textSize = (24 - n).coerceAtLeast(4).toFloat()
                setTypeface(null, Typeface.BOLD)
                setOnClickListener {
                    text = field[i][j].toString()
                }
            }

            val params = GridLayout.LayoutParams().apply {
                width = cellSize.dpToPx()
                height = cellSize.dpToPx()
                setMargins(0,0,0,0)
                rowSpec = GridLayout.spec(i % n, 1f)
                columnSpec = GridLayout.spec(j % n, 1f)
            }
            button.layoutParams = params
            gridLayout.addView(button)
            }
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun generateField(n: Int, m: Int):Array<Array<Int>>{
        val field =  Array(n) { Array(n) { 0 }}
        //var count = m
        (0 until n * n).shuffled().take(m).forEach { index ->
            val i = index / n
            val j = index % n
            field[i][j] = -1
        }

        val offsets = listOf(-1, 0, 1)
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (field[i][j] != -1) {
                    field[i][j] = offsets.flatMap { di ->
                        offsets.map { dj ->
                            val ni = i + di
                            val nj = j + dj
                            if (ni in 0 until n && nj in 0 until n && field[ni][nj] == -1) 1 else 0
                        }
                    }.sum()
                }
            }
        }
        return field
    }
}
