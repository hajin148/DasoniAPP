package com.example.dasoniapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RhythmMenuActivity : AppCompatActivity() {
    //added
    private lateinit var database: DatabaseReference
    private lateinit var userID: String
    private lateinit var userNoteScore: String

    private var songNumStr = ""
    companion object {
        const val GAME_REQUEST_CODE = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        userID = user?.uid ?: "Unknown"
        userNoteScore = intent.getStringExtra("bestNoteScore") ?: "Unknown"
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
        showSongMenu()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is from ActivityB and is successful
        if (requestCode == GAME_REQUEST_CODE && resultCode == RESULT_OK) {
            setContentView(R.layout.activity_rhythm_game_end)

            val userRef = database.child(userID)

            val scoreText = findViewById<TextView>(R.id.curr_score_txt)
            val highscoreText = findViewById<TextView>(R.id.textView129)
            val endStar = findViewById<ImageView>(R.id.end_star)

            data?.let {
                // end score
                val finalScore = it.getIntExtra("score", 0)
                userRef.child("bestRhythmScore").get().addOnSuccessListener { dataSnapshot ->
                    val bestScore = dataSnapshot.getValue(Int::class.java) ?: 0
                    highscoreText.text = bestScore.toString()
                    if (finalScore > bestScore) {
                        userRef.child("bestRhythmScore").setValue(finalScore)
                    }
                }.addOnFailureListener {
                }
                scoreText.text = finalScore.toString()
            }

            when(songNumStr){
                "one" -> endStar.setImageResource(R.drawable.rhythm_game_end_onestar)
                "two" -> endStar.setImageResource(R.drawable.rhythm_game_end_twostar)
                "three" -> endStar.setImageResource(R.drawable.rhythm_game_end_twostar)
                "four" -> endStar.setImageResource(R.drawable.rhythm_game_end_threestar)
                "five" -> endStar.setImageResource(R.drawable.rhythm_game_end_threestar)
                "six" -> endStar.setImageResource(R.drawable.rhythm_game_end_onestar)
            }

            val songMenuButton = findViewById<ImageView>(R.id.song_menu_button)
            songMenuButton.setOnClickListener {
                showSongMenu()
            }


        }
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
                songNumStr = songNumString[i]
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
            startActivityForResult(intent, GAME_REQUEST_CODE)

        }

        val songBackButton = findViewById<ImageView>(R.id.back_btn)
        songBackButton.setOnClickListener {
            showSongMenu()
        }
    }
}