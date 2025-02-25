package com.example.minesweeper

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val N = 5  // Размер поля

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.columnCount = N
        gridLayout.rowCount = N

        for (i in 0 until N * N) {
            val button = Button(this).apply {
                text = ""
                setOnClickListener {
                    text = if (text == "") "X"
                    else ""// Здесь можно добавить логику игры
                }
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                rowSpec = GridLayout.spec(i / N, 1f)
                columnSpec = GridLayout.spec(i % N, 1f)
            }
            button.layoutParams = params
            gridLayout.addView(button)
        }
    }
}