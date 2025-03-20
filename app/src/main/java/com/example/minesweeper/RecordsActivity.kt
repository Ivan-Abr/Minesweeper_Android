package com.example.minesweeper

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecordsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        val listViewRecords = findViewById<ListView>(R.id.listViewRecords)
        val tvTitle = findViewById<TextView>(R.id.tvRecordsTitle)

        val dbHelper = DBHelper(this)
        val records = dbHelper.getAllRecords()

        val adapter = ArrayAdapter<Record>(
            this,
            android.R.layout.simple_list_item_1,
            records
        )
        listViewRecords.adapter = adapter

        if (records.isEmpty()) {
            tvTitle.text = "Нет сохранённых рекордов"
        }
    }
}