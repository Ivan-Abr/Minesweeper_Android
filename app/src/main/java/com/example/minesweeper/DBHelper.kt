package com.example.minesweeper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "minesweeper.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "results"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_FIELD_SIZE = "field_size"
        const val COLUMN_MINE_COUNT = "mine_count"
        const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_FIELD_SIZE INTEGER,
                $COLUMN_MINE_COUNT INTEGER,
                $COLUMN_TIME TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveResult(context: Context, name: String, fieldSize: Int, mineCount: Int, time: String) {
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
