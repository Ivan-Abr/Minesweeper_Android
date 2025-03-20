package com.example.minesweeper

data class Record(
    val id: Int,
    val name: String,
    val fieldSize: Int,
    val mineCount: Int,
    val time: String
){
    override fun toString(): String {
        return "Имя: $name\nПоле: $fieldSize, Мины: $mineCount\nВремя: $time"
    }
}
