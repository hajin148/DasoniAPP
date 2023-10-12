package com.example.dasoniapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private companion object {
        const val SPLASH_TIME_OUT: Long = 3000
    }

    private var onboardingStep = 1
    private var currNoteIndex = -1 // ^
    private val noteMarginTop = listOf<Int>(128, 82, 40, 0, -37, -75, -115, -155, -195, -230, -280)
    private val noteList = listOf<String>("D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // if this is initial play of an app from mobile (check the global check the global variable flag)
        Handler(Looper.getMainLooper()).postDelayed({
            setOnboardingView()
        }, SPLASH_TIME_OUT)

        // else directly to the activity_mainpage
        // setContentView(R.layout.activity_mainpage)
    }

    private fun setOnboardingView() {
        when (onboardingStep) {
            1 -> setContentView(R.layout.activity_onboarding_1)
            2 -> setContentView(R.layout.activity_onboarding_2)
            3 -> setContentView(R.layout.activity_onboarding_3)
            4 -> setContentView(R.layout.activity_onboarding_4)
            5 -> {
                //set global flag to true so that this phase won't be reached after the first click on application
                setupMainPage()
                return
            }
            else -> return
        }

        val nextImageView: ImageView = findViewById(R.id.imageView4)
        nextImageView.setOnClickListener {
            onboardingStep++
            setOnboardingView()
        }
    }

    // ----------- move to other page ^
    private fun showGamePlayPage() {
        setContentView(R.layout.activity_score_game_practice)
        gamePlay()
    }

    private fun gamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(11) // 0~10
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex])
        noteBtnListner();
    }

    private fun moveNote(noteImg: ImageView, marginTop: Int) {
        val layoutParams = noteImg.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marginTop, 0, 0)

        noteImg.layoutParams = layoutParams
    }

    private fun noteBtnListner() {
        val cBtn: ImageView = findViewById(R.id.c_btn)
        val dBtn: ImageView = findViewById(R.id.d_btn)
        val eBtn: ImageView = findViewById(R.id.e_btn)
        val fBtn: ImageView = findViewById(R.id.f_btn)
        val gBtn: ImageView = findViewById(R.id.g_btn)
        val aBtn: ImageView = findViewById(R.id.a_btn)
        val bBtn: ImageView = findViewById(R.id.b_btn)

        cBtn.setOnClickListener {
            checkAnswer("C")
        }

        dBtn.setOnClickListener {
            checkAnswer("D")
        }

        eBtn.setOnClickListener {
            checkAnswer("E")
        }

        fBtn.setOnClickListener {
            checkAnswer("F")
        }

        gBtn.setOnClickListener {
            checkAnswer("G")
        }

        aBtn.setOnClickListener {
            checkAnswer("A")
        }

        bBtn.setOnClickListener {
            checkAnswer("B")
        }
    }

    private fun checkAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_txt)

        if(noteList[currNoteIndex] == noteStr) {
            answerTxt.setText("$noteStr, 정답입니다!")
        } else {
            answerTxt.setText("$noteStr, 틀렸습니다!")
        }

        Handler(Looper.getMainLooper()).postDelayed({
            answerTxt.text = ""
            gamePlay()
        }, 1500)
    }
    //-------------------------

    private fun setupMainPage() {
        setContentView(R.layout.activity_mainpage)

        // --------- need to create activity ------------- ^ go to page
        val musicPracticeBtn: ImageView = findViewById(R.id.music_practice_btn)
        musicPracticeBtn.setOnClickListener {
            showGamePlayPage()
        }
        // ---------------------------------------

        val homeButton: ImageButton = findViewById(R.id.main_menu_home_selected)
        homeButton.setOnClickListener {
            setupMainPage()
        }

        val myPageButton: ImageButton = findViewById(R.id.main_menu_mypage)
        myPageButton.setOnClickListener {
            setupMyPage()
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank)
        rankPageButton.setOnClickListener {
            setupRankPage()
        }
    }

    private fun setupMyPage() {
        setContentView(R.layout.activity_mypage)

        val homeButton: ImageButton = findViewById(R.id.main_menu_home)
        homeButton.setOnClickListener {
            setupMainPage()
        }

        val myPageButton: ImageButton = findViewById(R.id.bottom_menu_mypage_selected)
        myPageButton.setOnClickListener {
            setupMyPage()
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank)
        rankPageButton.setOnClickListener {
            setupRankPage()
        }
    }

    private fun setupRankPage() {
        setContentView(R.layout.activity_rank_first)

        val homeButton: ImageButton = findViewById(R.id.main_menu_home)
        homeButton.setOnClickListener {
            setupMainPage()
        }

        val myPageButton: ImageButton = findViewById(R.id.main_menu_mypage)
        myPageButton.setOnClickListener {
            setupMyPage()
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank_selected)
        rankPageButton.setOnClickListener {
            setupRankPage()
        }
    }

}
