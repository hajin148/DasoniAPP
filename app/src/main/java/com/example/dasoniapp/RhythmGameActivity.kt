package com.example.dasoniapp

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class RhythmGameActivity : AppCompatActivity() {

    private var score = 0
    private var wrongCount = 0
    private var isScoredAdded = false
    private var isButtonPressed = false
    private val handler = Handler(Looper.getMainLooper())
    private var isFadeAnimationRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_rhythm_game_easy_start) ^
//
//        val backButton = findViewById<ImageButton>(R.id.back_btn)
//        backButton.setOnClickListener {
//            finish()
//        }
//
//        val gameStartBtn = findViewById<ImageView>(R.id.game_start_btn)
//
//        gameStartBtn.setOnClickListener {
//           gameStart()
//        }

        gameStart()
    }



    private fun gameStart() {
        setContentView(R.layout.activity_rhythm_game_easy_play)
        playGame()
    }

    private fun playGame() {
        // initialization
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        val answerFour = findViewById<ImageView>(R.id.answer_four)

        val pressAnsOne = findViewById<ImageView>(R.id.press_one_top)
        val pressAnsTwo = findViewById<ImageView>(R.id.press_two_top)
        val pressAnsThree = findViewById<ImageView>(R.id.press_three_top)
        val pressAnsFour = findViewById<ImageView>(R.id.press_four_top)

        val btnOne = findViewById<ImageView>(R.id.btn_one)
        val btnTwo = findViewById<ImageView>(R.id.btn_two)
        val btnThree = findViewById<ImageView>(R.id.btn_three)
        val btnFour = findViewById<ImageView>(R.id.btn_four)

        // answer dropping node starts
        pressAnswerOneFall()

        // button pressing handler
        btnPressHelper(btnOne, answerOne, pressAnsOne, "one")
        btnPressHelper(btnTwo, answerTwo, pressAnsTwo, "two")
        btnPressHelper(btnThree, answerThree, pressAnsThree, "three")
        btnPressHelper(btnFour, answerFour, pressAnsFour, "four")
    }

    // func gets called from constructor for each button to detect press
    private fun btnPressHelper(
        btnNode: ImageView,
        ansClickNode: ImageView,
        ansPressNode: ImageView,
        btnNumStr: String
    ) {
        val veryGoodText = findViewById<ImageView>(R.id.very_good_text)
        val answerText = findViewById<TextView>(R.id.ans_text)
        val btnAnimationList = getCorrectBtnAnimation(btnNumStr)

        // button pressing event
        btnNode.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isButtonPressed = true
                    recursiveCheckAnswer(btnNode, ansClickNode, ansPressNode)
                    // making button Animation visible
                    var delayCount: Long = 0
                    for (btn in btnAnimationList) {
                        handler.postDelayed({
                            btn.visibility = View.VISIBLE
                        }, delayCount)
                        delayCount += 30
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // initialize again
                    isButtonPressed = false
                    isScoredAdded = false
                    isFadeAnimationRunning = false
                    // initialize answerText for conditional expression used in other function
                    if (!isFadeAnimationRunning) {
                        if (veryGoodText.visibility == View.VISIBLE) {
                            fadeOutAnimationHelper(veryGoodText, 1000)
                        }
                        if (answerText.visibility == View.VISIBLE) {
                            fadeOutAnimationHelper(answerText, 1000)
                        }
                    }

                    // for removing button animation
                    for (btn in btnAnimationList) {
                        fadeOutAnimationHelper(btn, 250)
                    }
                }
            }
            true
        }
    }

    // recursively check answer when button is being pressed
    private fun recursiveCheckAnswer(
        btnNode: ImageView,
        ansRegNode: ImageView,
        ansPressNode: ImageView
    ) {
        val veryGoodText = findViewById<ImageView>(R.id.very_good_text)
        val answerText = findViewById<TextView>(R.id.ans_text)
        val scoreText = findViewById<TextView>(R.id.game_score)

        if (isButtonPressed) {
            // if semi-correct with pressing answer node
            // this will be considered as perfect correct for long press node
            if (isOverlap(btnNode, ansPressNode)) {
//                answerText.visibility = View.GONE
                veryGoodText.visibility = View.VISIBLE
                score += 1
                scoreText.text = "$score"
            }
            // if perfect-correct with short answer node
            else if (isClickPerfectOverlap(btnNode, ansRegNode)) {
                // if for displaying only one for short node
                if (answerText.visibility != View.VISIBLE) {
                    veryGoodText.visibility = View.VISIBLE
                }

                if (!isScoredAdded) {
                    score += 10
                    scoreText.text = "$score"
                    isScoredAdded = true
                }
            }
            // if semi-correct with short answer node
            else if (isOverlap(btnNode, ansRegNode)) {
                // if for displaying only one for short node
                if (veryGoodText.visibility != View.VISIBLE) {
                    answerText.text = "정말 잘했어요!!"
                    answerText.visibility = View.VISIBLE
                }

                if (!isScoredAdded) {
                    score += 5
                    scoreText.text = "$score"
                    isScoredAdded = true
                }
            } else {
                if (veryGoodText.visibility == View.VISIBLE) {
                    isFadeAnimationRunning = true
                    fadeOutAnimationHelper(veryGoodText, 300)
                    return
                }
                if (answerText.visibility == View.VISIBLE) {
                    isFadeAnimationRunning = true
                    fadeOutAnimationHelper(answerText, 300)
                }
            }

            if(!isFadeAnimationRunning) {
                handler.postDelayed({
                    recursiveCheckAnswer(btnNode, ansRegNode, ansPressNode)
                }, 100)
            }
        }
    }

    private fun isClickPerfectOverlap(btnNode: ImageView, answerNode: ImageView): Boolean {
        val answerLoc = IntArray(2)
        answerNode.getLocationOnScreen(answerLoc)

        val btnLoc = IntArray(2)
        btnNode.getLocationOnScreen(btnLoc)

        val answerOffset = answerNode.height / 3
        val answerRectForPerfect = Rect(
            answerLoc[0],
            answerLoc[1] + answerOffset,
            answerLoc[0] + answerNode.width,
            answerLoc[1] + 2 * answerOffset
        )

        val btnOffset = btnNode.height / 7
        val btnRectForPerfect = Rect(
            btnLoc[0],
            btnLoc[1] + 3 * btnOffset,
            btnLoc[0] + btnNode.width,
            btnLoc[1] + 4 * btnOffset
        )

        return answerRectForPerfect.intersect(btnRectForPerfect)
    }

    // function for semi-overlap
    private fun isOverlap(btnNode: ImageView, answerNode: ImageView): Boolean {
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

    private fun getCorrectBtnAnimation(btnNumStr: String): List<ImageView> {
        // get resource id dynamically by btnNumStr
        val btnAniOneId = "ani_one_btn_$btnNumStr"
        val btnAniTwoId = "ani_two_btn_$btnNumStr"
        val btnAniThreeId = "ani_three_btn_$btnNumStr"

        val btnAniOne: ImageView =
            findViewById(resources.getIdentifier(btnAniOneId, "id", packageName))
        val btnAniTwo: ImageView =
            findViewById(resources.getIdentifier(btnAniTwoId, "id", packageName))
        val btnAniThree: ImageView =
            findViewById(resources.getIdentifier(btnAniThreeId, "id", packageName))

        return listOf(btnAniOne, btnAniTwo, btnAniThree)
    }

    private fun fadeOutAnimationHelper(node: View, duration: Long) {
        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
        fadeOutAnimation.duration = duration

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // fading out
                node.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // making it visible
        node.startAnimation(fadeOutAnimation)
    }

    // -------------- functions for node falling --------------------------
    private fun fallAnimation(answerNode: ImageView, currentScore: Int?) {
        val animator = answerNode.animate()
            .translationYBy(2700f) // 3000f - y length screen
            .setDuration(4000) // 4000 secs

        animator.interpolator = LinearInterpolator()

        animator.setListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {
                answerNode.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: android.animation.Animator) {
                answerNode.visibility = View.INVISIBLE
                answerNode.translationY = 0f

                // tracking for missed nodes
                if(currentScore != null && currentScore == score) {
                    val answerText = findViewById<TextView>(R.id.ans_text)
                    answerText.text = "Miss"
                    answerText.visibility = View.VISIBLE

                    // life decrement when wrong
                    val heartLife = findViewById<ImageView>(R.id.heart_life)
                    wrongCount += 1
                    if (wrongCount == 3) {
                        heartLife.setImageResource(R.drawable.heart_two)
                    }
                    else if(wrongCount == 6) {
                        heartLife.setImageResource(R.drawable.heart_one)
                    }
                    else if(wrongCount >= 9){
//                        setContentView(R.layout.activity_rhythm_game_easy_start) ^
                        finish()
                    }
                    fadeOutAnimationHelper(answerText, 2000)
                }
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {
            }
        })
    }

    // -------------- press answer node fall functions ----------------------
    private fun pressAnswerOneFall() {
        val currentScore = score

        val pressAnsOne = findViewById<ImageView>(R.id.press_answer_one)
        pressAnsOne.visibility = View.VISIBLE
        fallAnimation(pressAnsOne, null)

        val pressAnsOneTop = findViewById<ImageView>(R.id.press_one_top)
        pressAnsOneTop.visibility = View.VISIBLE
        fallAnimation(pressAnsOneTop, currentScore)

        handler.postDelayed({
            pressAnswerTwoFall()
        }, 3000)
    }

    private fun pressAnswerTwoFall() {
        val currentScore = score
        val pressAnsTwo = findViewById<ImageView>(R.id.press_answer_two)
        fallAnimation(pressAnsTwo, null)

        val pressAnsTwoTop = findViewById<ImageView>(R.id.press_two_top)
        fallAnimation(pressAnsTwoTop, currentScore)


        handler.postDelayed({
            pressAnswerThreeFall()
        }, 3000)
    }

    private fun pressAnswerThreeFall() {
        val currentScore = score
        val pressAnsThree = findViewById<ImageView>(R.id.press_answer_three)
        fallAnimation(pressAnsThree, null)

        val pressAnsThreeTop = findViewById<ImageView>(R.id.press_three_top)
        fallAnimation(pressAnsThreeTop, currentScore)

        handler.postDelayed({
            pressAnswerFourFall()
        }, 3000)
    }

    private fun pressAnswerFourFall() {
        val currentScore = score
        val pressAnsFour = findViewById<ImageView>(R.id.press_answer_four)
        fallAnimation(pressAnsFour, null)

        val pressAnsFourTop = findViewById<ImageView>(R.id.press_four_top)
        fallAnimation(pressAnsFourTop, currentScore)

        handler.postDelayed({
            answerOneFall()
        }, 4000)
    }

    // ----------- regular answer node fall functions -----------------

    private fun answerOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        fallAnimation(answerOne, currentScore)

        handler.postDelayed({
            answerTwoFall()
        }, 3000)
    }

    private fun answerTwoFall() {
        val currentScore = score
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        fallAnimation(answerTwo, currentScore)

        handler.postDelayed({
            answerThreeFall()
        }, 3000)
    }

    private fun answerThreeFall() {
        val currentScore = score
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        fallAnimation(answerThree, currentScore)

        handler.postDelayed({
            answerFourFall()
        }, 3000)
    }

    private fun answerFourFall() {
        val currentScore = score
        val answerFour = findViewById<ImageView>(R.id.answer_four)
        fallAnimation(answerFour, currentScore)
    }

}