package com.example.dasoniapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class ReadNoteActivity : AppCompatActivity() {
    private var currNoteIndex = -1
    private var mediaPlayer = MediaPlayer()

    // for upper note
    private val noteMarginTop =
        listOf<Int>(180, 128, 82, 40, 0, -37, -75, -115, -155, -195, -230, -280)
    private val noteList =
        listOf<String>("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G")
    private val ansNoteMarginTop = mutableMapOf<String, Int>(
        "C" to noteMarginTop[0],
        "D" to noteMarginTop[1],
        "E" to noteMarginTop[2],
        "F" to noteMarginTop[3],
        "G" to noteMarginTop[4],
        "A" to noteMarginTop[5],
        "B" to noteMarginTop[6]
    )
    private val noteSoundList = listOf(
        R.raw.ptdo,
        R.raw.ptre,
        R.raw.ptmi,
        R.raw.ptfa,
        R.raw.ptsol,
        R.raw.ptla,
        R.raw.ptsi,
        R.raw.ptdo2,
        R.raw.ptre2,
        R.raw.ptmi2,
        R.raw.ptfa2,
        R.raw.ptsol2
    )
    private val highAnsSoundMap = mapOf(
        "C" to R.raw.ptdo,
        "D" to R.raw.ptre,
        "E" to R.raw.ptmi,
        "F" to R.raw.ptfa,
        "G" to R.raw.ptsol,
        "A" to R.raw.ptla,
        "B" to R.raw.ptsi
    )

    // for lower note
    private val noteMarginBottom =
        listOf<Int>(-2320, -2170, -2030, -1888, -1740, -1600, -1450)
    private val lowNoteList = listOf<String>("C", "D", "E", "F", "G", "A", "B")
    private val ansLowNoteMarginTop = mutableMapOf<String, Int>(
        "C" to noteMarginBottom[0],
        "D" to noteMarginBottom[1],
        "E" to noteMarginBottom[2],
        "F" to noteMarginBottom[3],
        "G" to noteMarginBottom[4],
        "A" to noteMarginBottom[5],
        "B" to noteMarginBottom[6]
    )

    private val lowAnsSoundMap = mapOf(
        "C" to R.raw.ptdo0,
        "D" to R.raw.ptre0,
        "E" to R.raw.ptmi0,
        "F" to R.raw.ptfa0,
        "G" to R.raw.ptsol0,
        "A" to R.raw.ptla0,
        "B" to R.raw.ptsi0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openGameMenu()
    }

    private fun openGameMenu() {
        setContentView(R.layout.activity_score_game_menu)

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            val menu = Intent(this, MainActivity::class.java)
            startActivity(menu)
        }

        // practice game
        val practiceGameView = findViewById<ImageView>(R.id.score_game_menu_one)
        practiceGameView.setOnClickListener {
            setContentView(R.layout.activity_score_game_practice)
            gamePlay()
        }

        // speed game
        val scoreGameView = findViewById<ImageView>(R.id.score_game_menu_two)
        scoreGameView.setOnClickListener {
            val scoreGame = Intent(this, ScoreGameActivity::class.java)
            startActivity(scoreGame)
        }
    }

    private fun gamePause(notePlaceStr: String) {
        setContentView(R.layout.activity_score_game_pause)

        val continueBtn: ImageView = findViewById(R.id.continue_btn)
        continueBtn.setOnClickListener {
            setContentView(R.layout.activity_score_game_practice)
            if(notePlaceStr == "high") {
                val noteImg: ImageView = findViewById(R.id.note_img)
                moveNote(noteImg, noteMarginTop[currNoteIndex], 0)
                highGamePlayHelper()
            }
            else {
                val noteImg: ImageView = findViewById(R.id.note_img)
                moveNote(noteImg, 0, noteMarginBottom[currNoteIndex])
                lowGamePlayHelper()
            }
        }

        val replayBtn: ImageView = findViewById(R.id.replay_btn)
        replayBtn.setOnClickListener {
            setContentView(R.layout.activity_score_game_practice)
            gamePlay()
        }

        val menuBtn: ImageView = findViewById(R.id.main_menu_btn)
        menuBtn.setOnClickListener {
            val menu = Intent(this, MainActivity::class.java)
            startActivity(menu)
        }
    }

    private fun moveNote(img: ImageView, marginTop: Int, marginBottom: Int) {
        val layoutParams = img.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marginTop, 0, marginBottom)

        img.layoutParams = layoutParams
    }

    private fun playSoundHelper(rightSound: Int?, wrongSound: Int?, wrongNoteStr: String?) {
        mediaPlayer.release()
        if (wrongNoteStr == null) {
            if (rightSound != null) {
                mediaPlayer = MediaPlayer.create(this, rightSound)
            }
        }
        else {
            if(wrongSound != null) {
                mediaPlayer = MediaPlayer.create(this, wrongSound)
            }
        }
        mediaPlayer.start()
    }

    private fun playSound(notePlace: String, wrongNoteStr: String?) {
        mediaPlayer.release()

        if(notePlace == "high") {
            playSoundHelper(noteSoundList[currNoteIndex], highAnsSoundMap[wrongNoteStr], wrongNoteStr)
        }
        else {
            playSoundHelper(lowAnsSoundMap[lowNoteList[currNoteIndex]], lowAnsSoundMap[wrongNoteStr], wrongNoteStr)
        }

        mediaPlayer.start()
    }


    private fun gamePlay() {
        // random choose to play low note or high note
        when (Random.nextInt(2)) { //0~1
            0 -> highGamePlay()
            1 -> lowGamePlay()
        }
    }

    private fun highGamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(noteList.size) // 0~10
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        if (currNoteIndex == 0) {
            val noteLine: TextView = findViewById(R.id.note_line)
            noteLine.visibility = View.VISIBLE
        }
        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex], 0)
        playSound("high", null)

        highGamePlayHelper()
    }

    private fun highGamePlayHelper() {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("high")
        }

        noteBtnListner(::highCheckAnswer)
    }

    private fun lowGamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(lowNoteList.size) // 0~6
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, 0, noteMarginBottom[currNoteIndex])
        playSound("low", null)

        lowGamePlayHelper()
    }

    private fun lowGamePlayHelper() {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("low")
        }

        noteBtnListner(::lowCheckAnswer)
    }

    private fun noteBtnListner(checkAnswer: (String) -> Unit) {
        val cBtn: ImageView = findViewById(R.id.c_btn)
        val dBtn: ImageView = findViewById(R.id.d_btn)
        val eBtn: ImageView = findViewById(R.id.e_btn)
        val fBtn: ImageView = findViewById(R.id.f_btn)
        val gBtn: ImageView = findViewById(R.id.g_btn)
        val aBtn: ImageView = findViewById(R.id.a_btn)
        val bBtn: ImageView = findViewById(R.id.b_btn)

        val btnList = mapOf<String, ImageView>(
            "C" to cBtn,
            "D" to dBtn,
            "E" to eBtn,
            "F" to fBtn,
            "G" to gBtn,
            "A" to aBtn,
            "B" to bBtn
        )

        for ((btnStr, btnElem) in btnList) {
            btnElem.setOnClickListener {
                checkAnswer(btnStr)
                for (btnEntry in btnList) {
                    // disabling all buttons to be clickable
                    btnEntry.value.isClickable = false
                }
            }
        }
    }

    private fun highCheckAnswer(noteStr: String) {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.isClickable = false
        val answerTxt: TextView = findViewById(R.id.answer_txt)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE

        if (noteList[currNoteIndex] == noteStr) {
            playSound("high", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, noteMarginTop[currNoteIndex], 0)

            // if user pressed lower C note, then show line on answer note
            if (currNoteIndex == 0) {
                answerNoteLine.setTextColor(0xFF30D5D8.toInt())
                answerNoteLine.visibility = View.VISIBLE
            }
        } else {
            playSound("high", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 틀렸습니다!"
            moveNote(answerNoteImg, ansNoteMarginTop[noteStr] as Int, 0)

            // if user pressed C, then show line on answer note
            if (noteStr == "C") {
                answerNoteLine.setTextColor(0xFFFF46A9.toInt())
                answerNoteLine.visibility = View.VISIBLE
            }
        }

        // wait for 1.5 seconds to run
        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE

            // hide all the lines on note
            if (currNoteIndex == 0) {
                val noteLine: TextView = findViewById(R.id.note_line)
                noteLine.visibility = View.INVISIBLE
            }
            answerNoteLine.visibility = View.INVISIBLE

            gamePlay()
        }, 1500)
    }

    private fun lowCheckAnswer(noteStr: String) {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.isClickable = false

        val answerTxt: TextView = findViewById(R.id.answer_txt)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE

        if (lowNoteList[currNoteIndex] == noteStr) {
            playSound("low", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, 0, noteMarginBottom[currNoteIndex])
        } else {
            playSound("low", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 틀렸습니다!"
            moveNote(answerNoteImg, 0, ansLowNoteMarginTop[noteStr] as Int)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE

            gamePlay()
        }, 1500)
    }

}