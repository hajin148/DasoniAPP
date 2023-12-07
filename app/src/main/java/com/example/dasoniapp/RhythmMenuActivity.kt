package com.example.dasoniapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class RhythmMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSongMenu()
    }

    private fun showSongMenu() {
        setContentView(R.layout.activity_rhythm_game_song)
        val backButton = findViewById<ImageView>(R.id.rhythm_back_btn)
        backButton.setOnClickListener {
            finish()
        }

        val songClickBox = listOf<ImageView>(
            findViewById<ImageView>(R.id.song_one_box),
            findViewById<ImageView>(R.id.song_two_box),
            findViewById<ImageView>(R.id.song_three_box),
            findViewById<ImageView>(R.id.song_four_box),
            findViewById<ImageView>(R.id.song_five_box),
            findViewById<ImageView>(R.id.song_six_box)
        )
        val songNumString = listOf<String>("one", "two", "three", "four", "five", "six")

        for ((i, song) in songClickBox.withIndex()) {
            val songNameId = "song_${songNumString[i]}_title"
            val songWriterNameId = "song_${songNumString[i]}_title"
            val songName =
                findViewById<TextView>(resources.getIdentifier(songNameId, "id", packageName))
            val songWriterName =
                findViewById<TextView>(resources.getIdentifier(songWriterNameId, "id", packageName))

            // songBox clicked
            song.setOnClickListener {
                songStartWindow(songNumString[i], songName, songWriterName)
            }
        }
    }

    private fun songStartWindow(
        songNumString: String,
        songName: TextView,
        songWriterName: TextView
    ) {
        setContentView(R.layout.activity_rhythm_game_easy_start)
        val startSongName = findViewById<TextView>(R.id.start_song_name)
        val startSongWriterName = findViewById<TextView>(R.id.start_writer_name)

        startSongName.text = songName.text
        startSongWriterName.text = songWriterName.text

        val gameStartButton = findViewById<ImageView>(R.id.game_start_btn)
        gameStartButton.setOnClickListener {
            // activity call
            val intent = Intent(this, RhythmGameActivity::class.java)
            intent.putExtra("songNumber", songNumString)
            startActivity(intent)
        }

        val songBackButton = findViewById<ImageView>(R.id.back_btn)
        songBackButton.setOnClickListener {
            showSongMenu()
        }
    }
}