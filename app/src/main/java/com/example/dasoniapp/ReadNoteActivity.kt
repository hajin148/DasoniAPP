package com.example.dasoniapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class ReadNoteActivity : AppCompatActivity() {
    private var currNoteIndex = -1
    private var mediaPlayer = MediaPlayer()

    // for upper note
    private val noteMarginTop =
        listOf<Int>(60, 45, 30, 15, 0, -14, -28, -42, -57, -71, -85, -100)
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
        listOf<Int>(-842, -790, -736, -684, -632, -580, -520)
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
    private lateinit var currentUser: UserAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openGameMenu()
        currentUser =intent.getSerializableExtra("UserAccount") as UserAccount

    }

    private fun openGameMenu() {
        setContentView(R.layout.activity_score_game_menu)

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
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
            val scoreGameIntent = Intent(this, ScoreGameActivity::class.java)
            scoreGameIntent.putExtra("UserAccount", currentUser)
            scoreGameIntent.putExtra("userName", currentUser?.name ?: "Unknown")
            startActivity(scoreGameIntent)
        }
    }

    private fun gamePause(notePlaceStr: String) {
        setContentView(R.layout.activity_score_game_pause)

        // stop mediaplayer
        mediaPlayer.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }

        val continueBtn: ImageView = findViewById(R.id.continue_btn)
        continueBtn.setOnClickListener {
            setContentView(R.layout.activity_score_game_practice)
            if (notePlaceStr == "high") {
                val noteImg: ImageView = findViewById(R.id.note_img)
                moveNote(noteImg, noteMarginTop[currNoteIndex], 0)
                highGamePlayHelper()
            } else {
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
            openGameMenu()
        }
    }

    private fun moveNote(img: ImageView, marginTop: Int, marginBottom: Int) {
        val parent: ImageView = findViewById(R.id.imageView46)
        val layoutParams = img.layoutParams as ViewGroup.MarginLayoutParams
        val marginTopPx = dpToPx(marginTop, this)
        val marginBottomPx = dpToPx(marginBottom, this)
        img.visibility = View.INVISIBLE

        val parentViewTreeObserver = parent.viewTreeObserver
        parentViewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                parent.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val parentHeight = parent.height
                val calMarginTop = (parentHeight * marginTopPx / parentHeight).toInt()
                val calMarginBottom = (parentHeight * marginBottomPx / parentHeight).toInt()

                layoutParams.setMargins(0, calMarginTop, 0, calMarginBottom)
                img.layoutParams = layoutParams
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            img.visibility = View.VISIBLE
        }, 100)
    }


    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun playSoundHelper(rightSound: Int?, wrongSound: Int?, wrongNoteStr: String?) {
        mediaPlayer.release()
        if (wrongNoteStr == null) {
            if (rightSound != null) {
                mediaPlayer = MediaPlayer.create(this, rightSound)
            }
        } else {
            if (wrongSound != null) {
                mediaPlayer = MediaPlayer.create(this, wrongSound)
            }
        }
        mediaPlayer.start()
    }

    private fun playSound(notePlace: String, wrongNoteStr: String?) {
        mediaPlayer.release()

        if (notePlace == "high") {
            playSoundHelper(
                noteSoundList[currNoteIndex],
                highAnsSoundMap[wrongNoteStr],
                wrongNoteStr
            )
        } else {
            playSoundHelper(
                lowAnsSoundMap[lowNoteList[currNoteIndex]],
                lowAnsSoundMap[wrongNoteStr],
                wrongNoteStr
            )
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
            Handler(Looper.getMainLooper()).postDelayed({
                noteLine.visibility = View.VISIBLE
            }, 100)
        }
        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex], 0)

        highGamePlayHelper()
    }

    private fun highGamePlayHelper() {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("high")
        }

        playSound("high", null)
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

        lowGamePlayHelper()
    }

    private fun lowGamePlayHelper() {
        val pauseBtn: ImageView = findViewById(R.id.pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("low")
        }

        playSound("low", null)
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

        if (noteList[currNoteIndex] == noteStr) {
            playSound("high", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, noteMarginTop[currNoteIndex], 0)

            // if user pressed lower C note, then show line on answer note
            if (currNoteIndex == 0) {
                answerNoteLine.setTextColor(0xFF30D5D8.toInt())
                Handler(Looper.getMainLooper()).postDelayed({
                    answerNoteLine.visibility = View.VISIBLE
                }, 100)
            }
        } else {
            playSound("high", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            var correctAnswer = noteList[currNoteIndex]
            answerTxt.text = "$noteStr, 틀렸습니다! 정답은 $correctAnswer 입니다"
            moveNote(answerNoteImg, ansNoteMarginTop[noteStr] as Int, 0)

            // if user pressed C, then show line on answer note
            if (noteStr == "C") {
                answerNoteLine.setTextColor(0xFFFF46A9.toInt())
                Handler(Looper.getMainLooper()).postDelayed({
                    answerNoteLine.visibility = View.VISIBLE
                }, 100)
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

        if (lowNoteList[currNoteIndex] == noteStr) {
            playSound("low", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, 0, noteMarginBottom[currNoteIndex])
        } else {
            playSound("low", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            var correctAnswer = lowNoteList[currNoteIndex]
            answerTxt.text = "$noteStr, 틀렸습니다! 정답은 $correctAnswer 입니다"
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