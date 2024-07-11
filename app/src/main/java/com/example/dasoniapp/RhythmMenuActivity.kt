package com.example.dasoniapp;
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RhythmMenuActivity : AppCompatActivity() {
    //added
    private lateinit var database: DatabaseReference
    private lateinit var userID: String
    private lateinit var userNoteScore: String
    private lateinit var userName: String

    private var songNumStr = ""
    companion object {
        const val GAME_REQUEST_CODE = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        userName = intent.getStringExtra("userName") ?: "Unknown"
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
                val finalScore = it.getIntExtra("score", 0)
                userRef.child("bestRhythmScore").get().addOnSuccessListener { dataSnapshot ->
                    val bestScore = dataSnapshot.getValue(Int::class.java) ?: 0
                    highscoreText.text = bestScore.toString()

                    if (finalScore > bestScore) {
                        userRef.child("bestRhythmScore").setValue(finalScore)
                        checkAndUpdateScoreboard(finalScore, userName,"rhythm")
                    }
                    scoreText.text = finalScore.toString()
                }.addOnFailureListener {
                    // Handle the failure
                }
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

    data class ScoreEntry(var name: String = "", var score: Int = 0)

    private fun checkAndUpdateScoreboard(newScore: Int, userName: String, scoreType: String) {
        val scoreboardRef = FirebaseDatabase.getInstance().getReference("DasoniAPP/scoreboard/$scoreType")

        scoreboardRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val scores = snapshot.children.mapNotNull { it.getValue(ScoreEntry::class.java) }
                    .toMutableList()

                // Check if user is already on the scoreboard
                val existingUserIndex = scores.indexOfFirst { it.name == userName }

                if (existingUserIndex != -1) {
                    // User is already on the board, update score if new score is higher
                    if (existingUserIndex != -1 && scores[existingUserIndex].score < newScore) {
                        scores[existingUserIndex] = ScoreEntry(userName, newScore)
                    }
                } else if (scores.size < 10 || scores.any { it.score < newScore }) {
                    // User not on board, but qualifies for the scoreboard
                    val newEntry = ScoreEntry(userName, newScore)
                    scores.add(newEntry)
                }

                // Sort and keep top 10 scores
                scores.sortByDescending { it.score }
                if (scores.size > 10) scores.removeAt(scores.size - 1)

                // Update the scoreboard
                val updatedScores = scores.mapIndexed { index, scoreEntry ->
                    index.toString() to scoreEntry
                }.toMap()

                scoreboardRef.setValue(updatedScores)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
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
            val songWriterNameId = "song_${songNumString[i]}_writer"
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