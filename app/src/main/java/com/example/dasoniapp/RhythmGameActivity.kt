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
import android.widget.ImageView
import android.widget.TextView

class RhythmGameActivity : AppCompatActivity() {

    private var score = 0
    private var wrongCount = 0
    private var isScoredAdded = false
    private var isWrong = false
    private var isButtonPressed = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    isButtonPressed = false
                    isScoredAdded = false // initialize again
                    // initialize answerText for conditional expression used in other function
                    answerText.text = ""
                    if(veryGoodText.visibility == View.VISIBLE) {
                        fadeOutAnimationHelper(veryGoodText, 1000)
                    }
                    if(answerText.visibility == View.VISIBLE) {
                        fadeOutAnimationHelper(answerText, 1000)
                    }

                    // for removing button animation
                    for (btn in btnAnimationList) {
                        fadeOutAnimationHelper(btn, 250)
                    }

                    // life decrement when wrong
                    if(isWrong) {
                        val heartLife: ImageView = findViewById(R.id.heart_life)
                        wrongCount += 1
                        if(wrongCount == 3) {
                            heartLife.setImageResource(R.drawable.heart_two)
                        } else if (wrongCount == 6) {
                            heartLife.setImageResource(R.drawable.heart_one)
                        } else if (wrongCount >= 9) {
                            finish()
                        }

                        // initialize isWrong again
                        isWrong = false
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
                answerText.visibility = View.GONE
                veryGoodText.visibility = View.VISIBLE
                score += 1
                scoreText.text = "$score"
            }
            // if perfect-correct with short answer node
            else if(isClickPerfectOverlap(btnNode, ansRegNode)) {
                // if for displaying only one for short node
                if(answerText.text != "정말 잘했어요") {
                    veryGoodText.visibility = View.VISIBLE
                }

                if(!isScoredAdded) {
                    score += 10
                    scoreText.text = "$score"
                    isScoredAdded = true
                }
            }
            // if semi-correct with short answer node
            else if (isOverlap(btnNode, ansRegNode)) {
                // if for displaying only one for short node
                if(veryGoodText.visibility != View.VISIBLE) {
                    answerText.text = "정말 잘했어요"
                    answerText.visibility = View.VISIBLE
                }

                if(!isScoredAdded) {
                    score += 5
                    scoreText.text = "$score"
                    isScoredAdded = true
                }
            }
            // if press but node not there
            else {
                veryGoodText.visibility = View.GONE
                answerText.text = "다시 시도해주세요"
                answerText.visibility = View.VISIBLE
                isWrong = true
            }

            handler.postDelayed({
                recursiveCheckAnswer(btnNode, ansRegNode, ansPressNode)
            }, 40)
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
    private fun fallAnimation(answerNode: ImageView) {
        val animator = answerNode.animate()
            .translationYBy(2700f) // 3000f
            .setDuration(4000) // 4000

        animator.interpolator = LinearInterpolator()

        animator.setListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {
                answerNode.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: android.animation.Animator) {
                answerNode.visibility = View.INVISIBLE
                answerNode.translationY = 0f
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {
            }
        })
    }

    // function to check missed node
    private fun recurCheckAnsBtnNotPressed(btnNode: ImageView, answerNode: ImageView) {
        val answerText = findViewById<TextView>(R.id.ans_text)
        if(!isButtonPressed) {
            if(isOverlap(btnNode, answerNode)){
                answerText.text = "Miss"
                answerText.visibility = View.VISIBLE
            }
            else if(answerText.text == "Miss"){
                fadeOutAnimationHelper(answerText, 1000)
                // bug?? why this showing twice
                return
            }
            handler.postDelayed({
                recurCheckAnsBtnNotPressed(btnNode, answerNode)
            }, 50)
        }
    }

    // -------------- press answer node fall functions ----------------------
    private fun pressAnswerOneFall() {
        val pressAnsOne = findViewById<ImageView>(R.id.press_answer_one)
        pressAnsOne.visibility = View.VISIBLE
        fallAnimation(pressAnsOne)

        val pressAnsOneTop = findViewById<ImageView>(R.id.press_one_top)
        pressAnsOneTop.visibility = View.VISIBLE
        fallAnimation(pressAnsOneTop)

        val btnOne = findViewById<ImageView>(R.id.btn_one)
        recurCheckAnsBtnNotPressed(btnOne, pressAnsOneTop)

        handler.postDelayed({
            pressAnswerTwoFall()
        }, 3000)
    }

    private fun pressAnswerTwoFall() {
        val pressAnsTwo = findViewById<ImageView>(R.id.press_answer_two)
        fallAnimation(pressAnsTwo)

        val pressAnsTwoTop = findViewById<ImageView>(R.id.press_two_top)
        fallAnimation(pressAnsTwoTop)


//        val btnTwo = findViewById<ImageView>(R.id.btn_two)
//        recurCheckAnsBtnNotPressed(btnTwo, pressAnsTwoTop)

        handler.postDelayed({
            pressAnswerThreeFall()
        }, 3000)
    }

    private fun pressAnswerThreeFall() {
        val pressAnsThree = findViewById<ImageView>(R.id.press_answer_three)
        fallAnimation(pressAnsThree)

        val pressAnsThreeTop = findViewById<ImageView>(R.id.press_three_top)
        fallAnimation(pressAnsThreeTop)

        // recursion check for missing node
        val btnThree = findViewById<ImageView>(R.id.btn_three)
        recurCheckAnsBtnNotPressed(btnThree, pressAnsThreeTop)

        handler.postDelayed({
            pressAnswerFourFall()
        }, 3000)
    }

    private fun pressAnswerFourFall() {
        val pressAnsFour = findViewById<ImageView>(R.id.press_answer_four)
        fallAnimation(pressAnsFour)

        val pressAnsFourTop = findViewById<ImageView>(R.id.press_four_top)
        fallAnimation(pressAnsFourTop)

//        // recursion check for missing node
//        val btnFour = findViewById<ImageView>(R.id.btn_four)
//        recurCheckAnsBtnNotPressed(btnFour, pressAnsFourTop)

        handler.postDelayed({
            answerOneFall()
        }, 4000)
    }

    // ----------- regular answer node fall functions -----------------

    private fun answerOneFall() {
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        fallAnimation(answerOne)

        handler.postDelayed({
            answerTwoFall()
        }, 3000)
    }

    private fun answerTwoFall() {
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        fallAnimation(answerTwo)

        handler.postDelayed({
            answerThreeFall()
        }, 3000)
    }

    private fun answerThreeFall() {
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        fallAnimation(answerThree)

        handler.postDelayed({
            answerFourFall()
        }, 3000)
    }

    private fun answerFourFall() {
        val answerFour = findViewById<ImageView>(R.id.answer_four)
        fallAnimation(answerFour)
    }

}