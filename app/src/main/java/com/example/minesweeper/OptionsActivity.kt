package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val inputN = findViewById<EditText>(R.id.input_n)
        val inputM = findViewById<EditText>(R.id.input_m)
        val btnSave = findViewById<Button>(R.id.btn_save)

        val sharedPref: SharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        btnSave.setOnClickListener {
            val n = inputN.text.toString().toIntOrNull()
            val m = inputM.text.toString().toIntOrNull()

            if (n == null || m == null || n < 3 || n > 10 || m < 1 || m >= n * n) {
                Toast.makeText(this, "Введите корректные значения!", Toast.LENGTH_SHORT).show()
            } else {
                sharedPref.edit().putInt("field_size", n).apply()
                sharedPref.edit().putInt("mine_count", m).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}