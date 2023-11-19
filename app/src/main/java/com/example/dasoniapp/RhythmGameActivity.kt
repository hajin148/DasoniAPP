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
            buttonAnimation("one")
        }

        btnTwo.setOnClickListener {
            checkAnswer(answerTwo, btnTwo)
            buttonAnimation("two")
        }

        btnThree.setOnClickListener {
            checkAnswer(answerThree, btnThree)
            buttonAnimation("three")
        }

        btnFour.setOnClickListener {
            checkAnswer(answerFour, btnFour)
            buttonAnimation("four")
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
        val overlapStr: String = getOverlapString(answerNode, btnNode)
        if (overlapStr == "correct") {
            answerAnimation("very good")
        }
        else if (overlapStr == "semi correct"){
            answerAnimation("good")
        }
        else {
            answerAnimation("wrong")
        }
    }

    private fun getOverlapString(answerNode: ImageView, btnNode: ImageView): String {
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

        val answerOffset = answerNode.height / 3
        val answerRectForPerfect = Rect(
            answerLoc[0],
            answerLoc[1] + answerOffset,
            answerLoc[0] + answerNode.width,
            answerLoc[1] + 2*answerOffset
        )

        val btnOffset = btnNode.height / 7
        val btnRectForPerfect = Rect(
            btnLoc[0],
            btnLoc[1] + 3*btnOffset,
            btnLoc[0] + btnNode.width,
            btnLoc[1] + 4*btnOffset
        )

        if(answerRectForPerfect.intersect(btnRectForPerfect)) {
            return "correct"
        }
        else if(answerRect.intersect(btnRect)) {
            return "semi correct"
        }
        else {
            return "wrong"
        }
    }

    private fun buttonAnimation(btnNumStr: String) {
        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
        fadeOutAnimation.duration = 1000

        // get resource id dynamically by btnNumStr
        val ani_one_Id = "ani_one_btn_$btnNumStr"
        val ani_two_Id = "ani_two_btn_$btnNumStr"
        val ani_three_Id = "ani_three_btn_$btnNumStr"

        val ani_one: ImageView = findViewById(resources.getIdentifier(ani_one_Id, "id", packageName))
        val ani_two: ImageView = findViewById(resources.getIdentifier(ani_two_Id, "id", packageName))
        val ani_three: ImageView = findViewById(resources.getIdentifier(ani_three_Id, "id", packageName))

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                ani_one.visibility = View.GONE
                ani_two.visibility = View.GONE
                ani_three.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        ani_one.startAnimation(fadeOutAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            ani_two.startAnimation(fadeOutAnimation)
        }, 30)

        Handler(Looper.getMainLooper()).postDelayed({
            ani_three.startAnimation(fadeOutAnimation)
        }, 60)
    }

    private fun answerAnimation(state: String) {
        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
        fadeOutAnimation.duration = 1500

        val answerText: View?
        // correct text animation
        if (state == "very good") {
            answerText = findViewById<ImageView>(R.id.very_good_text)
        }
        // wrong text animation
        else {
            answerText = findViewById<TextView>(R.id.ans_text)
            if (state == "good") {
                answerText.text = "정말 잘했어요!!"
            }
            else {
                answerText.text = "다시 시도해주세요"
            }
        }
        answerText?.visibility = View.VISIBLE

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                answerText?.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Start animation
        Handler(Looper.getMainLooper()).postDelayed({
            answerText?.startAnimation(fadeOutAnimation)
        }, 50)
    }

}