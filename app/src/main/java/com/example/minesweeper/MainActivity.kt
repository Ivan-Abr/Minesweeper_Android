package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)

        button1.setOnClickListener {
            getToFieldActivity()
        }
    }

    private fun getToFieldActivity(){
        val intent = Intent(this, FieldActivity::class.java)
        startActivity(intent)
    }
}
