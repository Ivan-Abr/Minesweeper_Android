package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getToFieldActivity()
    }

    private fun getToFieldActivity(){
        val intent = Intent(this, FieldActivity::class.java)
        startActivity(intent)
    }
}