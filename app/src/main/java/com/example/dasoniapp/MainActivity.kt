package com.example.dasoniapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth

    private companion object {
        const val SPLASH_TIME_OUT: Long = 3000
    }

    private var onboardingStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isFirstLaunch()) {
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                setOnboardingView()
            }, SPLASH_TIME_OUT)
        } else {
            setupMainPage()
        }

        val shouldSetupMyPage = intent.getBooleanExtra("shouldSetupMyPage", false)
        if (shouldSetupMyPage) {
            setupMyPage()  // Call setupMyPage directly
        } else {
            // Your existing logic for deciding the initial view
            if (isFirstLaunch()) {
                setContentView(R.layout.activity_splash)
                Handler(Looper.getMainLooper()).postDelayed({
                    setOnboardingView()
                }, SPLASH_TIME_OUT)
            } else {
                setupMainPage()
            }
        }
    }

    private fun isFirstLaunch(): Boolean {
        val prefs = getSharedPreferences("com.example.dasoniapp", MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)
        if (isFirstLaunch) {
            // If it's the first launch, update the flag
            prefs.edit().putBoolean("isFirstLaunch", false).apply()
        }
        return isFirstLaunch
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

        val musicPracticeBtn: ImageView = findViewById(R.id.music_practice_btn)
        musicPracticeBtn.setOnClickListener {
            val musicPractice = Intent(this, ReadNoteActivity::class.java)
            startActivity(musicPractice)
        }

        val rhythmGameBtn: ImageView = findViewById(R.id.rhythm_game_btn)
        rhythmGameBtn.setOnClickListener {
            val rhythmGame = Intent(this, RhythmGameActivity::class.java)
            startActivity(rhythmGame)
        }

        val musicPlayBtn: ImageView = findViewById(R.id.music_play_btn)
        musicPlayBtn.setOnClickListener {
            setContentView(R.layout.activity_instrument_menu)
            val pianoPlayBtn: ImageView = findViewById(R.id.imageView98)
            pianoPlayBtn.setOnClickListener {
                val musicPlay = Intent(this, PlayMusicActivity::class.java)
                startActivity(musicPlay)
            }

            val drumPlayBtn: ImageView = findViewById(R.id.imageView101)
            drumPlayBtn.setOnClickListener {
                val drumPlay = Intent(this, PlayDrumActivity::class.java)
                startActivity(drumPlay)
            }

            val exitButton = findViewById<ImageButton>(R.id.imageButton4)
            exitButton.setOnClickListener {
                setupMainPage()
            }
        }

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

        // Initialize FirebaseAuth instance
        mFirebaseAuth = FirebaseAuth.getInstance()

        // Get reference to the textView31
        val logoutTextView: TextView = findViewById(R.id.textView31)

        // Check if user is logged in
        if (mFirebaseAuth.currentUser != null) {
            // User is logged in, change text to "로그아웃"
            logoutTextView.text = "로그아웃"

            // Set OnClickListener to log out
            logoutTextView.setOnClickListener {
                // Log out the user
                mFirebaseAuth.signOut()

                // Update UI or go back to login page after logout
                val LoginPage = Intent(this, LoginActivity::class.java)
                startActivity(LoginPage)
                finish()
            }
        }

        // Other setup code for your MyPage
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

        val LoginBtn: ImageView = findViewById(R.id.imageView10)
        LoginBtn.setOnClickListener {
            val LoginPage = Intent(this, LoginActivity::class.java)
            startActivity(LoginPage)
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
