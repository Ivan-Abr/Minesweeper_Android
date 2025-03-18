package com.example.minesweeper

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MinesweeperGame(
    private val context: Context,
    private val gridLayout: GridLayout,
    private val n: Int,
    private val m: Int,
    private val cellSize: Int
) {
    private val numberColors = mapOf(
        1 to Color.BLUE,
        2 to Color.rgb(20, 135, 0),
        3 to Color.RED,
        4 to Color.rgb(0, 0, 139),
        5 to Color.rgb(255, 165, 0),
        6 to Color.CYAN,
        7 to Color.MAGENTA,
        8 to Color.BLACK
    )

    private val field: Array<Array<Int>> = generateField(n, m)
    private val cells: Array<Array<TextView>> = Array(n) { i -> Array(n) { j -> createCell(i, j) } }
    var gameOver: Boolean = false
        private set

    init {
        setupGrid()
    }

    private fun generateField(n: Int, m: Int): Array<Array<Int>> {
        val field = Array(n) { Array(n) { 0 } }
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

    private fun createCell(i: Int, j: Int): TextView {
        return TextView(context).apply {
            text = ""
            gravity = Gravity.CENTER
            textSize = (24 - n).coerceAtLeast(4).toFloat()
            setTypeface(null, Typeface.BOLD)
            setBackgroundResource(R.drawable.cell_background)
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
                cells[i][j].layoutParams = params
                gridLayout.addView(cells[i][j])
            }
        }
    }

    private fun onCellClicked(i: Int, j: Int) {
        if (gameOver || cells[i][j].isClickable.not()) return
        when (field[i][j]) {
            -1 -> {
                revealAllMines()
                gameOver = true
            }
            0 -> revealZeroCells(i, j)
            else -> revealCell(i, j)
        }
    }

    private fun revealCell(i: Int, j: Int) {
        if (cells[i][j].isClickable.not()) return
        cells[i][j].apply {
            text = if (field[i][j] > 0) field[i][j].toString() else ""
            val value = field[i][j]
            if (value > 0) {
                text = value.toString()
                setTextColor(numberColors[value] ?: Color.BLACK)
            }
            textSize = (cellSize * 0.4f).coerceAtLeast(10f)
            isEnabled = false
            isClickable = false
        }
    }

    private fun revealZeroCells(i: Int, j: Int) {
        if (i !in 0 until n || j !in 0 until n || cells[i][j].isClickable.not()) return
        revealCell(i, j)
        if (field[i][j] == 0) {
            for (di in -1..1) {
                for (dj in -1..1) {
                    if (di != 0 || dj != 0) {
                        revealZeroCells(i + di, j + dj)
                    }
                }
            }
        }
    }

    private fun revealAllMines() {
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (field[i][j] == -1) {
                    cells[i][j].apply {
                        text = ""
                        isSelected = true
                        isClickable = false

                        val mineDrawable = ContextCompat.getDrawable(context, R.drawable.mine_icon)?.apply {
                            setBounds(0, 0, cellSize * 2, cellSize * 2) // Размер 50% от клетки
                        }
                        setCompoundDrawables(null, null, null, mineDrawable)
                        compoundDrawablePadding = cellSize / 2 // Отступ, чтобы иконка не прилипала к краям
                        gravity = Gravity.CENTER // Центрирование

                    }
                } else {
                    cells[i][j].isClickable = false
                }
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
