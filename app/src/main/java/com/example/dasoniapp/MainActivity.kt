package com.example.dasoniapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var currentUser: UserAccount? = null
    private companion object {
        const val SPLASH_TIME_OUT: Long = 3000
    }

    private var onboardingStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mFirebaseAuth = FirebaseAuth.getInstance()

        // Initialize currentUser
        initializeCurrentUser()



        if (isFirstLaunch()) {
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({ setOnboardingView() }, SPLASH_TIME_OUT)
        } else {
            setupMainPage()
        }
        /*
        val shouldSetupMyPage = intent.getBooleanExtra("shouldSetupMyPage", false)
        currentUser = intent.getSerializableExtra("UserAccount") as UserAccount?
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
         */
    }
    private fun initializeCurrentUser() {
        val userId = mFirebaseAuth.currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
            databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    currentUser = dataSnapshot.getValue(UserAccount::class.java)
                    if (currentUser == null) {
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        } else {
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

        if(currentUser != null) {
            val nameView: TextView = findViewById(R.id.textView16)
            nameView.text = currentUser?.name.toString() + "님"
        }

        val musicPracticeBtn: ImageView = findViewById(R.id.music_practice_btn)
        musicPracticeBtn.setOnClickListener {
            val intent = Intent(this, ReadNoteActivity::class.java)
            intent.putExtra("UserAccount", currentUser)
            startActivity(intent)
        }

        val rhythmGameBtn: ImageView = findViewById(R.id.rhythm_game_btn)
        rhythmGameBtn.setOnClickListener {
            val intent = Intent(this, RhythmMenuActivity::class.java)
            intent.putExtra("UserAccount", currentUser)
            startActivity(intent)
        }

        val musicPlayBtn: ImageView = findViewById(R.id.music_play_btn)
        musicPlayBtn.setOnClickListener {
            setContentView(R.layout.activity_instrument_menu)
            val pianoPlayBtn: ImageView = findViewById(R.id.imageView98)
            pianoPlayBtn.setOnClickListener {
                val intent = Intent(this, PlayMusicActivity::class.java)
                intent.putExtra("UserAccount", currentUser)
                startActivity(intent)
            }

            val drumPlayBtn: ImageView = findViewById(R.id.imageView101)
            drumPlayBtn.setOnClickListener {
                val intent = Intent(this, PlayDrumActivity::class.java)
                intent.putExtra("UserAccount", currentUser)
                startActivity(intent)
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
            val nameView: TextView = findViewById(R.id.textView23)
            nameView.text = currentUser?.name.toString()

            val nameView1: TextView = findViewById(R.id.textView33)
            nameView1.text = currentUser?.name.toString() + " " + nameView1.text

            val nameView2: TextView = findViewById(R.id.textView32)
            nameView2.text = currentUser?.name.toString() + " " + nameView2.text
            val nameView3: TextView = findViewById(R.id.textView36)
            nameView3.text = currentUser?.name.toString() + " " + nameView3.text
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
        val userId = mFirebaseAuth.currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
            databaseReference.child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userAccount = dataSnapshot.getValue(UserAccount::class.java)
                    if (userAccount != null) {
                        updateRankPageUI(userAccount)
                    } else {
                        // Handle the case where user data is not found
                        setupDefaultRankPage()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                    setupDefaultRankPage()
                }
            })
        } else {
            // Handle case where user ID is null (user not logged in or other reasons)
            setupDefaultRankPage()
        }
    }

    private fun updateRankPageUI(userAccount: UserAccount) {
        setContentView(R.layout.activity_rank_first)

        // Update UI elements with user data
        val bestNoteScoreTextView: TextView = findViewById(R.id.textView65)
        bestNoteScoreTextView.text = userAccount.bestNoteScore.toString()
        val userNameTextView: TextView = findViewById(R.id.textView63)
        userNameTextView.text = userAccount.name

        setupRankPageListeners()
    }

    private fun setupDefaultRankPage() {
        setContentView(R.layout.activity_rank_first)

        // Setup default UI or show error message

        setupRankPageListeners()
    }

    private fun setupRankPageListeners() {
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