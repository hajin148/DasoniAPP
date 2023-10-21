package com.example.dasoniapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton

class PlayDrumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drum)

        val exitButton = findViewById<ImageButton>(R.id.imageButton24)
        exitButton.setOnClickListener {
            finish()
        }
    }
}