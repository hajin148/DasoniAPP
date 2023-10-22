package com.example.dasoniapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class ScoreGameActivity : AppCompatActivity() {
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

    // initialize other varibles
    private var currNoteIndex = -1
    private var mediaPlayer = MediaPlayer()
    private lateinit var countDownTimer: CountDownTimer
    private var timeCount: Long = 20000
    private var timeRemaining: Long = timeCount
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game_rank)
        gamePlay()
    }

    // ------- timer code start -------
    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisRemaining: Long) {
                timeRemaining = millisRemaining
                val secsRemaining = timeRemaining / 1000

                val timeText: TextView = findViewById(R.id.time_text)
                timeText.text = "$secsRemaining"
                // Update your UI with the remaining time, e.g., TextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                endGame("fail")
            }
        }
        // Start the timer
        countDownTimer.start()
    }

    private fun endGame(resultStr: String) {
        pauseTimer()
        setContentView(R.layout.activity_score_game_end)
        val resultText: TextView = findViewById(R.id.result_txt)
        if (resultStr == "success") {
            resultText.text = "정말 잘했어요!"
        } else {
            resultText.text = "실수해도 괜찮아요!"
        }

        val backBtn: ImageView = findViewById(R.id.go_back_btn)
        backBtn.setOnClickListener {
            val gameMenu = Intent(this, ReadNoteActivity::class.java)
            startActivity(gameMenu)
        }
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
    }

    private fun resumeTimer() {
        initCountDownTimer()
        countDownTimer.start()
    }

    // ------- timer code end -------

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
        initCountDownTimer()

        when (Random.nextInt(2)) {
            0 -> highGamePlay()
            1 -> lowGamePlay()
        }
    }

    private fun lowGamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(lowNoteList.size) // 0~6
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, 0, noteMarginBottom[currNoteIndex])

        lowGamePlayHelper()
    }

    private fun lowGamePlayHelper() {
        playSound("low", null)
        noteBtnListner(::lowCheckAnswer)
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

        highGamePlayHelper()
    }

    private fun highGamePlayHelper() {
        playSound("high", null)
        noteBtnListner(::highCheckAnswer)
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

    private fun correctAnswerTimer(notePlaceStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        pauseTimer()
        score += 10
        val scoreText: TextView = findViewById(R.id.score_text)
        scoreText.text = "Score: $score"

        // wait for 1.5 seconds to run
        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE
            answerTxtLine.visibility = View.INVISIBLE

            // hide all the lines on note
            if (currNoteIndex == 0 && notePlaceStr == "high") {
                val noteLine: TextView = findViewById(R.id.note_line)
                noteLine.visibility = View.INVISIBLE
                answerNoteLine.visibility = View.INVISIBLE
            }

            // change time
            timeCount -= 1000
            if (timeCount == 4000L) {
                endGame("success")
            } else {
                timeRemaining = timeCount
                gamePlay()
            }
        }, 1500)
    }

    private fun wrongAnswerTimer(notePlaceStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)

        pauseTimer()
        // wait for 1.5 seconds to run
        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE
            answerTxtLine.visibility = View.INVISIBLE

            if (notePlaceStr == "high") {
                answerNoteLine.visibility = View.INVISIBLE
                highGamePlayHelper()
            } else {
                lowGamePlayHelper()
            }
            resumeTimer()
        }, 1500)
    }

    private fun highCheckAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE
        answerTxtLine.visibility = View.VISIBLE

        // right answer
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

            correctAnswerTimer("high")
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

            wrongAnswerTimer("high")
        }
    }

    private fun lowCheckAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE
        answerTxtLine.visibility = View.VISIBLE

        if (lowNoteList[currNoteIndex] == noteStr) {
            playSound("low", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, 0, noteMarginBottom[currNoteIndex])

            correctAnswerTimer("low")
        } else {
            playSound("low", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 틀렸습니다!"
            moveNote(answerNoteImg, 0, ansLowNoteMarginTop[noteStr] as Int)

            wrongAnswerTimer("low")
        }
    }

}