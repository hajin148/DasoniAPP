package com.example.dasoniapp

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView

class RhythmGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rhythm_game_easy_play)

        playGame()
    }

    private fun playGame() {
        answerOneFall() // falling game start

        val answerOne = findViewById<ImageView>(R.id.answer_one)
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        val answerFour = findViewById<ImageView>(R.id.answer_four)

        val btnOne = findViewById<ImageView>(R.id.btn_one)
        val btnTwo = findViewById<ImageView>(R.id.btn_two)
        val btnThree = findViewById<ImageView>(R.id.btn_three)
        val btnFour = findViewById<ImageView>(R.id.btn_four)

        btnOne.setOnClickListener {
            checkAnswer(answerOne, btnOne)
        }

        btnTwo.setOnClickListener {
            checkAnswer(answerTwo, btnTwo)
        }

        btnThree.setOnClickListener {
            checkAnswer(answerThree, btnThree)
        }

        btnFour.setOnClickListener {
            checkAnswer(answerFour, btnFour)
        }
    }

    private fun answerOneFall() {
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        fallAnimation(answerOne)

        Handler(Looper.getMainLooper()).postDelayed({
            answerTwoFall()
        }, 3000)
    }

    private fun answerTwoFall() {
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        fallAnimation(answerTwo)

        Handler(Looper.getMainLooper()).postDelayed({
            answerThreeFall()
        }, 3000)
    }

    private fun answerThreeFall() {
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        fallAnimation(answerThree)

        Handler(Looper.getMainLooper()).postDelayed({
            answerFourFall()
        }, 3000)
    }

    private fun answerFourFall() {
        val answerFour = findViewById<ImageView>(R.id.answer_four)
        fallAnimation(answerFour)
    }

    private fun fallAnimation(answerNode: ImageView) {
        val animator = answerNode.animate()
            .translationYBy(2500f)
            .setDuration(4000)

        animator.setListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {
                answerNode.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: android.animation.Animator) {
                answerNode.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {
            }
        })
    }


    private fun checkAnswer(answerNode: ImageView, btnNode: ImageView) {
        if(isOverlapping(answerNode, btnNode)) {
            veryGoodAnimation()
        }
        else {
            wrongAnimation()
        }
    }

    private fun isOverlapping(answerNode: ImageView, btnNode: ImageView): Boolean {
        val answerLoc = IntArray(2)
        answerNode.getLocationOnScreen(answerLoc)

        val btnLoc = IntArray(2)
        btnNode.getLocationOnScreen(btnLoc)

        val answerRect = Rect(
            answerLoc[0],
            answerLoc[1],
            answerLoc[0] + answerNode.width,
            answerLoc[1] + answerNode.height
        )

        val btnRect = Rect(
            btnLoc[0],
            btnLoc[1],
            btnLoc[0] + btnNode.width,
            btnLoc[1] + btnNode.height
        )

        return answerRect.intersect(btnRect)
    }

    private fun veryGoodAnimation() {
        val veryGoodText: ImageView = findViewById(R.id.very_good_text)
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

    private fun wrongAnimation() {
        val wrontText: TextView = findViewById(R.id.wrong_text)
        wrontText.visibility = View.VISIBLE

        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
        fadeOutAnimation.duration = 1000 // 1 sec duration

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                wrontText.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Start animation
        Handler(Looper.getMainLooper()).postDelayed({
            wrontText.startAnimation(fadeOutAnimation)
        }, 100)
    }

}