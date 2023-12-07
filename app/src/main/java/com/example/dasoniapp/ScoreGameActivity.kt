package com.example.dasoniapp

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random


class ScoreGameActivity : AppCompatActivity() {

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

    // initialize other varibles
    private var currNoteIndex = -1
    private var mediaPlayer = MediaPlayer()
    private lateinit var countDownTimer: CountDownTimer
    private var timeCount: Long = 21000
    private var timeRemaining: Long = timeCount
    private var score = 0
    private var countCorrect = 0
    private lateinit var database: DatabaseReference
    private lateinit var userID: String
    private lateinit var userNoteScore: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game_rank)

        val user = FirebaseAuth.getInstance().currentUser
        userID = user?.uid ?: "Unknown"
        userNoteScore = intent.getStringExtra("bestNoteScore") ?: "Unknown"
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")

        countDownGamePlay()
    }

    private fun countDownGamePlay() {
        val noteImg: ImageView = findViewById(R.id.note_img)
        noteImg.visibility = View.INVISIBLE
        val answerText: TextView = findViewById(R.id.answer_text)

        val startTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisRemaining: Long) {
                val secsRemaining = millisRemaining / 1000 - 1

                if (secsRemaining > 0) {
                    answerText.text = "$secsRemaining"
                } else {
                    answerText.text = "시작!!!"
                }
            }

            override fun onFinish() {
                answerText.text = ""
                gamePlay()
            }
        }

        startTimer.start()
    }

    private fun gamePause(notePlaceStr: String) {
        pauseTimer()
        setContentView(R.layout.activity_score_game_pause)

        // stop media player
        mediaPlayer.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }

        val continueBtn: ImageView = findViewById(R.id.continue_btn)
        continueBtn.setOnClickListener {
            setContentView(R.layout.activity_score_game_rank)
            resumeTimer()
            val scoreText: TextView = findViewById(R.id.score_text)
            scoreText.text = "Score: $score"
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
            setContentView(R.layout.activity_score_game_rank)
            timeCount = 20000 // reset?
            timeRemaining = timeCount
            score = 0
            countDownGamePlay()
        }

        val menuBtn: ImageView = findViewById(R.id.main_menu_btn)
        menuBtn.setOnClickListener {
            finish()
        }
    }


    // ------- timer code start -------
    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisRemaining: Long) {
                timeRemaining = millisRemaining
                val secsRemaining = timeRemaining / 1000

                val timeText: TextView = findViewById(R.id.time_text)
                timeText.text = "$secsRemaining"
            }

            override fun onFinish() {
                endGame("fail")
            }
        }
        // Start the timer
        countDownTimer.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
    }

    private fun resumeTimer() {
        initCountDownTimer()
        countDownTimer.start()
    }

    // ------- timer code end -------
    private fun endGame(resultStr: String) {
        pauseTimer()

        val userRef = database.child(userID)

        // disable buttons
        val btnList = listOf<ImageView>(
            findViewById(R.id.c_btn),
            findViewById(R.id.d_btn),
            findViewById(R.id.e_btn),
            findViewById(R.id.f_btn),
            findViewById(R.id.g_btn),
            findViewById(R.id.a_btn),
            findViewById(R.id.b_btn)
        )
        for (btn in btnList) {
            btn.isClickable = false
        }
        val pauseBtn: ImageView = findViewById(R.id.score_pause_btn)
        pauseBtn.isClickable = false

        val blurLayout: FrameLayout = findViewById(R.id.blur_layout)
        val resultBox: ImageView = findViewById(R.id.result_box)
        blurLayout.visibility = View.VISIBLE
        resultBox.visibility = View.VISIBLE

        // Send Score to database
        userRef.child("bestNoteScore").get().addOnSuccessListener { dataSnapshot ->
            val bestScore = dataSnapshot.getValue(Int::class.java) ?: 0
            if (score > bestScore) {
                userRef.child("bestNoteScore").setValue(score)
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

        val resultText: TextView = findViewById(R.id.result_txt)
        resultText.visibility = View.VISIBLE

        if (resultStr == "success") {
            resultText.text = "정말 대단해요!!"
        } else {
            resultText.text = "실수해도 괜찮아요!"
        }

        val backBtn: ImageView = findViewById(R.id.go_back_btn)
        backBtn.visibility = View.VISIBLE
        backBtn.setOnClickListener {
            finish()
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
                // Remove the listener to ensure it's only called once
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
        val pauseBtn: ImageView = findViewById(R.id.score_pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("low")
        }

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
            Handler(Looper.getMainLooper()).postDelayed({
                noteLine.visibility = View.VISIBLE
            }, 100)
        }

        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex], 0)

        highGamePlayHelper()
    }

    private fun highGamePlayHelper() {
        val pauseBtn: ImageView = findViewById(R.id.score_pause_btn)
        pauseBtn.setOnClickListener {
            gamePause("high")
        }

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
        val scoreText: TextView = findViewById(R.id.score_text)
        val veryGoodText: ImageView = findViewById(R.id.very_good_text)

        pauseTimer()
        score += 10
        scoreText.text = "Score: $score"
        if (countCorrect < 2) {
            countCorrect += 1
        }

        if (countCorrect == 2) {
            veryGoodText.visibility = View.VISIBLE

            val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
            fadeOutAnimation.duration = 1500 // 1.5 duration

            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    veryGoodText.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

            // Start animation
            Handler(Looper.getMainLooper()).postDelayed({
                veryGoodText.startAnimation(fadeOutAnimation)
            }, 100)
        }

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
            if (timeCount == 5000L) {
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
        val pauseBtn: ImageView = findViewById(R.id.score_pause_btn)
        pauseBtn.isClickable = false

        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerTxtLine.visibility = View.VISIBLE

        // right answer
        if (noteList[currNoteIndex] == noteStr) {
            playSound("high", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정말 잘했어요!!"
            moveNote(answerNoteImg, noteMarginTop[currNoteIndex], 0)

            // if user pressed lower C note, then show line on answer note
            if (currNoteIndex == 0) {
                answerNoteLine.setTextColor(0xFF30D5D8.toInt())
                Handler(Looper.getMainLooper()).postDelayed({
                    answerNoteLine.visibility = View.VISIBLE
                }, 100)
            }

            correctAnswerTimer("high")
        } else {
            playSound("high", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 다시 시도해보세요"
            moveNote(answerNoteImg, ansNoteMarginTop[noteStr] as Int, 0)

            // if user pressed C, then show line on answer note
            if (noteStr == "C") {
                answerNoteLine.setTextColor(0xFFFF46A9.toInt())
                Handler(Looper.getMainLooper()).postDelayed({
                    answerNoteLine.visibility = View.VISIBLE
                }, 100)
            }

            wrongAnswerTimer("high")
        }
    }

    private fun lowCheckAnswer(noteStr: String) {
        val pauseBtn: ImageView = findViewById(R.id.score_pause_btn)
        pauseBtn.isClickable = false

        val answerTxt: TextView = findViewById(R.id.answer_text)
        val answerTxtLine: ImageView = findViewById(R.id.imageView48)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerTxtLine.visibility = View.VISIBLE

        if (lowNoteList[currNoteIndex] == noteStr) {
            playSound("low", null)
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정말 잘했어요!!"
            moveNote(answerNoteImg, 0, noteMarginBottom[currNoteIndex])

            correctAnswerTimer("low")
        } else {
            playSound("low", noteStr)
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 다시 시도해보세요"
            moveNote(answerNoteImg, 0, ansLowNoteMarginTop[noteStr] as Int)

            wrongAnswerTimer("low")
        }
    }

}