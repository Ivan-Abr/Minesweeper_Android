package com.example.minesweeper

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout

class MinesweeperGame(
    private val context: Context,
    private val gridLayout: GridLayout,
    private val n: Int,
    private val m: Int,
    private val cellSize: Int
) {
    private val field: Array<Array<Int>> = generateField(n, m)
    private val buttons: Array<Array<Button>> = Array(n) { i -> Array(n) { j -> createButton(i, j) } }
    var gameOver: Boolean = false
        private set

    init {
        setupGrid()
    }

    private fun generateField(n: Int, m: Int):Array<Array<Int>>{
        val field =  Array(n) { Array(n) { 0 }}
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

    private fun createButton(i: Int, j: Int): Button {
        return Button(context).apply {
            text = ""
            gravity = Gravity.CENTER
            textSize = (24 - n).coerceAtLeast(4).toFloat()
            setTypeface(null, Typeface.BOLD)
            setOnClickListener { onCellClicked(i, j) }
        }
    }

    private fun setupGrid() {
        gridLayout.rowCount = n
        gridLayout.columnCount = n
        for (i in 0 until n) {
            for (j in 0 until n) {
                val params = GridLayout.LayoutParams().apply {
                    width = cellSize.dpToPx()
                    height = cellSize.dpToPx()
                    setMargins(0, 0, 0, 0)
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }
                buttons[i][j].layoutParams = params
                gridLayout.addView(buttons[i][j])
            }
        }
    }

    private fun onCellClicked(i: Int, j: Int) {
        if (gameOver || !buttons[i][j].isEnabled) return
        when (field[i][j]) {
            -1 -> {
                revealAllMines()
                gameOver = true
                // Можно вывести уведомление или диалог "Game Over"
            }
            0 -> revealZeroCells(i, j) // Рекурсивное открытие нулевых клеток
            else -> revealCell(i, j)     // Просто открываем число
        }
    }

    // Открытие клетки: устанавливаем текст и делаем кнопку неактивной
    private fun revealCell(i: Int, j: Int) {
        if (!buttons[i][j].isEnabled) return
        buttons[i][j].apply {
            text = if (field[i][j] > 0) field[i][j].toString() else ""
            isEnabled = false
        }
    }

    // Рекурсивное открытие клеток вокруг нулевой клетки
    private fun revealZeroCells(i: Int, j: Int) {
        // Проверка границ и состояния кнопки
        if (i !in 0 until n || j !in 0 until n || !buttons[i][j].isEnabled) return
        revealCell(i, j)
        if (field[i][j] == 0) {
            for (di in -1..1) {
                for (dj in -1..1) {
                    // Пропускаем саму клетку
                    if (di != 0 || dj != 0) {
                        revealZeroCells(i + di, j + dj)
                    }
                }
            }
        }
    }

    // Отображаем все мины и делаем кнопки неактивными
    private fun revealAllMines() {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (field[i][j] == -1) {
                    buttons[i][j].apply {
                        text = "M"  // или можно задать изображение мины
                        setBackgroundColor(Color.RED)
                        isEnabled = false
                    }
                } else {
                    buttons[i][j].isEnabled = false
                }
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}