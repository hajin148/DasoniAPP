package com.example.dasoniapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private companion object {
        const val SPLASH_TIME_OUT: Long = 3000
    }

    private var onboardingStep = 1

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

    private fun setupMainPage() {
        setContentView(R.layout.activity_mainpage)

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
