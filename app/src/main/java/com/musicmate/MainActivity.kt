package com.dasoniapp.musicmate;

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class MainActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private var currentUser: UserAccount? = null
    private var accountName = null
    private var userName = "";
    private var rhythmScore = "";
    private lateinit var database: DatabaseReference


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

        val goToSetupRankPage = intent.getBooleanExtra("goToSetupRankPage", false)

        if (goToSetupRankPage) {
            setupRankPage()
        }

        val goToSetupMyPage = intent.getBooleanExtra("goToSetupMyPage", false)

        if (goToSetupMyPage) {
            setupMyPage()
        }

        val goToSetupMainPage = intent.getBooleanExtra("goToSetupMainPage", false)

        if (goToSetupMainPage) {
            setupMainPage()
        }
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
            if(currentUser == null) {
                var a = UserAccount("test@gmail.com", "01011111234", "test", "123123")
                currentUser = a
            }
            intent.putExtra("UserAccount", currentUser)
            intent.putExtra("userName", currentUser?.name ?: "Unknown")
            startActivity(intent)
        }

        val rhythmGameBtn: ImageView = findViewById(R.id.rhythm_game_btn)
        rhythmGameBtn.setOnClickListener {
            val intent = Intent(this, RhythmMenuActivity::class.java)
            if(currentUser == null) {
                var a = UserAccount("test@gmail.com", "01011111234", "test", "123123")
                currentUser = a
            }
            intent.putExtra("userName", currentUser?.name ?: "Unknown")
            intent.putExtra("UserAccount", currentUser)
            startActivity(intent)
        }

        val musicPlayBtn: ImageView = findViewById(R.id.music_play_btn)
        musicPlayBtn.setOnClickListener {
            setContentView(R.layout.activity_instrument_menu)
            val pianoPlayBtn: ImageView = findViewById(R.id.imageView98)
            pianoPlayBtn.setOnClickListener {
                val intent = Intent(this, PlayMusicActivity::class.java)
                startActivity(intent)
            }

            val drumPlayBtn: ImageView = findViewById(R.id.imageView101)
            drumPlayBtn.setOnClickListener {
                val intent = Intent(this, PlayDrumActivity::class.java)
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
            if (currentUser?.name.isNullOrEmpty()) {
                // User name is null or empty, navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // User name is not null or empty, proceed to setupMyPage
                setupMyPage()
            }
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank)
        rankPageButton.setOnClickListener {
            if (currentUser?.name.isNullOrEmpty()) {
                // User name is null or empty, navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // User name is not null or empty, proceed to setupRankPage
                setupRankPage()
            }
        }
    }

    private fun setupMyPage() {
        setContentView(R.layout.activity_mypage)
        // Update the TextViews with the user's name
        val nameView: TextView = findViewById(R.id.textView23)
        nameView.text = currentUser?.name.toString()

        val nameView1: TextView = findViewById(R.id.textView33)
        nameView1.text = currentUser?.name.toString() + " " + nameView1.text

        val nameView2: TextView = findViewById(R.id.textView32)
        nameView2.text = currentUser?.name.toString() + " " + nameView2.text

        val nameView3: TextView = findViewById(R.id.textView36)
        nameView3.text = currentUser?.name.toString() + " " + nameView3.text

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

                // Wait for a short delay or use a more sophisticated method to ensure logout completion
                Handler(Looper.getMainLooper()).postDelayed({
                    // Update UI or go back to login page after logout
                    val LoginPage = Intent(this, LoginActivity::class.java)
                    startActivity(LoginPage)
                    finish()
                }, 1000)
            }
        }

        // Other setup code for your MyPage
        val homeButton: ImageButton = findViewById(R.id.main_menu_home)
        homeButton.setOnClickListener {
            setupMainPage()
        }

        val profileBtn: ImageView = findViewById(R.id.imageView8)
        profileBtn.setOnClickListener {
            setUpProfilePage()
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank)
        rankPageButton.setOnClickListener {
            val logoutTextView: TextView = findViewById(R.id.textView31)
            if(logoutTextView.text != "로그아웃") {
                currentUser = null
            }
            setupRankPage()
        }

        val LoginBtn: ImageView = findViewById(R.id.imageView10)
        LoginBtn.setOnClickListener {
            val LoginPage = Intent(this, LoginActivity::class.java)
            startActivity(LoginPage)
        }
    }

    private fun setUpProfilePage() {
        setContentView(R.layout.activity_profile_edit)
        val backPageButton: ImageButton = findViewById(R.id.imageButton5)
        backPageButton.setOnClickListener {
            setupMyPage()
        }
        val nick_name_view: TextView = findViewById(R.id.nick_name)
        nick_name_view.text = currentUser?.name.toString()

        val phone_view: TextView = findViewById(R.id.phone)
        phone_view.text = currentUser?.phone.toString()

        val email_view: TextView = findViewById(R.id.email_address)
        email_view.text = currentUser?.email.toString()

        val user = FirebaseAuth.getInstance().currentUser
        var userID = user?.uid ?: "Unknown"
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
        var userRef = database.child(userID)

        val edit_name: ImageView = findViewById(R.id.profile_edit_arrow_one)
        edit_name.setOnClickListener {
            setContentView(R.layout.activity_nickname_edit)
            val backPageButton: ImageButton = findViewById(R.id.back_btn)
            backPageButton.setOnClickListener {
                setUpProfilePage()
            }
            val nick_name_edit_view: TextView = findViewById(R.id.nick_name_edit)
            nick_name_edit_view.text = currentUser?.name.toString()
            val change_btn: ImageView = findViewById(R.id.change_btn)
            change_btn.setOnClickListener {
                val newName = nick_name_edit_view.text.toString()
                if (newName.isNotBlank()) {
                    userRef.child("name").setValue(newName).addOnSuccessListener {
                        // Handle successful update, e.g., show a confirmation message
                        Toast.makeText(this, "닉네임 업데이트", Toast.LENGTH_SHORT).show()
                        currentUser?.name = newName
                        setupMyPage()
                    }.addOnFailureListener {
                        // Handle the failure, e.g., show an error message
                        Toast.makeText(this, "닉네임 업데이트 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the case when the new name is blank
                    Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val edit_password: ImageView = findViewById(R.id.profile_edit_arrow_two)
        edit_password.setOnClickListener {
            setContentView(R.layout.activity_password_edit)
            val backPageButton: ImageButton = findViewById(R.id.back_btn)
            backPageButton.setOnClickListener {
                setUpProfilePage()
            }

            val edit_password: TextView = findViewById(R.id.password_edit)
            val change_btn: ImageView = findViewById(R.id.change_btn)

            change_btn.setOnClickListener {
                val newPassword = edit_password.text.toString()
                if (newPassword.isNotBlank()) {
                    FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)
                        ?.addOnSuccessListener {
                            // Handle successful password update
                            Toast.makeText(this, "비밀번호 업데이트", Toast.LENGTH_SHORT).show()
                            currentUser?.password = newPassword
                            userRef.child("password").setValue(newPassword)
                            setupMyPage()
                        }
                        ?.addOnFailureListener { e ->
                            // Handle failure in password update
                            Toast.makeText(this, "비밀번호 업데이트 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun setupRankPage() {
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        else if (FirebaseAuth.getInstance().currentUser != null) {
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
        } else {
            // User is not logged in, redirect to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateRankPageUI(userAccount: UserAccount) {
        setContentView(R.layout.activity_rank_first)
        updateScoreboardUI("note")


        // Update UI elements with user data
        val bestNoteScoreTextView: TextView = findViewById(R.id.textView65)
        bestNoteScoreTextView.text = userAccount.bestNoteScore.toString()
        val userNameTextView: TextView = findViewById(R.id.textView63)
        userNameTextView.text = userAccount.name

        userName = userAccount.name
        rhythmScore = userAccount.bestRhythmScore.toString()

        setupRankPageListeners()
    }

    private fun updateScoreboardUI(scoreboardType: String) {
        val scoreboardRef = FirebaseDatabase.getInstance().getReference("DasoniAPP/scoreboard/$scoreboardType")
        scoreboardRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val scores = snapshot.children.mapNotNull { it.getValue(ScoreEntry::class.java) }

                for ((index, scoreEntry) in scores.withIndex()) {
                    val nameTextViewId = resources.getIdentifier("rank${index + 1}_name", "id", packageName)
                    val scoreTextViewId = resources.getIdentifier("rank${index + 1}_score", "id", packageName)

                    findViewById<TextView>(nameTextViewId).text = scoreEntry.name
                    findViewById<TextView>(scoreTextViewId).text = scoreEntry.score.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    data class ScoreEntry(val name: String = "", val score: Int = 0)

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
            if (currentUser?.name.isNullOrEmpty()) {
                // User name is null or empty, navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // User name is not null or empty, proceed to setupMyPage
                setupMyPage()
            }
        }

        val rankPageButton: ImageButton = findViewById(R.id.main_menu_rank_selected)
        rankPageButton.setOnClickListener {
            setupRankPage()
        }

        val textView30: TextView = findViewById(R.id.textView30)
        textView30.setOnClickListener {
            // Click on textView30, switch to activity_rank_second
            setContentView(R.layout.activity_rank_second)
            setupRankSecondListeners()
        }
    }

    private fun setupRankSecondListeners() {
        setContentView(R.layout.activity_rank_second)
        updateScoreboardUI("rhythm")

        if(currentUser != null) {
            val bestRhythmScoreTextView: TextView = findViewById(R.id.textView65)
            bestRhythmScoreTextView.text = rhythmScore
            val userNameTextView2: TextView = findViewById(R.id.textView63)
            userNameTextView2.text = userName
        }


        val textView29 = findViewById<TextView>(R.id.textView29)
        textView29.setOnClickListener {
            // Click on textView29, switch back to activity_rank_first
            setupRankPage()
        }

        val homeButton: ImageButton = findViewById(R.id.main_menu_home)
        homeButton.setOnClickListener {
            setupMainPage()
        }

        val myPageButton: ImageButton = findViewById(R.id.main_menu_mypage)
        myPageButton.setOnClickListener {
            if (currentUser?.name.isNullOrEmpty()) {
                // User name is null or empty, navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // User name is not null or empty, proceed to setupMyPage
                setupMyPage()
            }
        }
    }




}