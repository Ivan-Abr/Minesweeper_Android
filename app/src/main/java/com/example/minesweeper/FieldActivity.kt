package com.example.minesweeper

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class FieldActivity : AppCompatActivity() {

    private val n = 6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_field)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.columnCount = n
        gridLayout.rowCount = n

        for (i in 0 until n * n) {
            val button = Button(this).apply {
                text = ""
                setOnClickListener {
                    text = if (text == "") "X"
                    else ""
                }
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                rowSpec = GridLayout.spec(i / n, 1f)
                columnSpec = GridLayout.spec(i % n, 1f)
            }
            button.layoutParams = params
            gridLayout.addView(button)
        }
    }
}