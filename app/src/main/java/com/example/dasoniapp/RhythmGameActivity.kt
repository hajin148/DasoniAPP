package com.example.dasoniapp

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.media.MediaPlayer
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
import androidx.lifecycle.lifecycleScope
import com.example.dasoniapp.databinding.ActivityRhythmGameEasyPlayBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RhythmGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRhythmGameEasyPlayBinding


    private var score = 0
    private var wrongCount = 0
    private var isScoredAdded = false
    private var isButtonPressed = false
    private val handler = Handler(Looper.getMainLooper())

    private var fadeOutMillis: Long = 400 // 400 up 부터 더블클릭 안됨 --> 간격은 무조건 설정된 fadeOutMillis 보다 크게
    private var mediaPlayer = MediaPlayer()
    private var isWrongCounted = false

    private var fallDuration: Long = 4000
    private var songLoopCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rhythm_game_easy_play)
        val gameBackBtn = findViewById<ImageView>(R.id.game_back_btn)
        gameBackBtn.setOnClickListener {
            mediaPlayer.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            finish()
        }

        playGameBasedOnSong(intent.getStringExtra("songNumber"))
        playGame()
    }

    private fun playGameBasedOnSong(songNumber: String?) {
        when (songNumber) {
            "one" -> songOnePlay()
            "two" -> songTwoPlay()
            "three" -> songThreePlay()
            "four" -> songFourPlay()
            "five" -> songFivePlay()
            "six" -> songSixPlay()
        }
    }
    private fun releaseMediaPlayer() {
        mediaPlayer.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = MediaPlayer()
    }
    private fun playMusic(musicResourceId: Int) {
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(this, musicResourceId).apply {
            setOnCompletionListener {
                Intent().also { resultIntent ->
                    resultIntent.putExtra("score", score)
                    setResult(RESULT_OK, resultIntent)
                    releaseMediaPlayer()
                    finish()
                }
            }
            start()
        }
    }
    private var songLoopCount1 = 0 // Assuming you're using a different counter for song one

    // Initiates the sequence for song one play
    private fun songOnePlay() {
        playMusic(R.raw.twinkle5)
        fadeOutMillis = 700
        handler.postDelayed({
            songOnePlayHelperOne()
        }, 3300)
    }

    // Handles the first part of the sequence for song one
    private fun songOnePlayHelperOne() {
        if (songLoopCount1 < 3) {
            playSequence("1122338")
            val totalDelayForSequenceOne = "1122338".length * 1000 + 1000 // Adding 1000ms delay
            handler.postDelayed({
                songOnePlayHelperTwo()
            }, totalDelayForSequenceOne.toLong())
        }
    }

    // Handles the second part of the sequence for song one
    private fun songOnePlayHelperTwo() {
        playSequence("4433225")
        val totalDelayForSequenceTwo = "4433225".length * 1000 + 1000 // Adding 1000ms delay
        handler.postDelayed({
            songLoopCount1++
            if (songLoopCount1 < 3) {
                handler.postDelayed({ // Additional delay before repeating the sequence
                    songOnePlayHelperOne()
                }, 0)
            } else {
                songLoopCount1 = 0 // Reset after completing the loop
            }
        }, totalDelayForSequenceTwo.toLong())
    }




    // Initiates the sequence for song two play
    private fun songTwoPlay() {
        playMusic(R.raw.twinkle2)
        fadeOutMillis = 250
        handler.postDelayed({
            songTwoPlayHelperOne()
        }, 3300)
    }

    // Handles the first part of the sequence for song two
    private fun songTwoPlayHelperOne() {
        if (songLoopCount2 < 6) {
            playSongTwoSequence("116338")
            val totalDelayForSequenceOne = calculateTotalDelay("116338")
            handler.postDelayed({
                songTwoPlayHelperTwo()
            }, totalDelayForSequenceOne)
        }
    }

    // Initiates the second part of the sequence for song two
    private fun songTwoPlayHelperTwo() {
        playSongTwoSequence("447225")
        val totalDelayForSequenceTwo = calculateTotalDelay("447225")
        handler.postDelayed({
            songLoopCount2++
            if (songLoopCount2 < 6) {
                songTwoPlayHelperOne() // Repeat the sequence
            } else {
                songLoopCount2 = 0 // Reset after completing the loop
            }
        }, totalDelayForSequenceTwo)
    }

    // Calculate the total delay for a given sequence
    private fun calculateTotalDelay(sequence: String): Long {
        var totalDelay = 0L
        for (i in sequence.indices) {
            totalDelay += if (i % 3 == 2) 1000L else 490L
        }
        return totalDelay
    }

    // Handles playing a sequence with specific timing
    private fun playSongTwoSequence(sequence: String) {
        var cumulativeDelay = 0L

        sequence.forEachIndexed { i, char ->
            val actionDelay = if (i % 3 == 2) 1000L else 490L

            // Update last fall count here
            val currentFallCount = lastFall.getOrDefault(char, 0)
            lastFall[char] = currentFallCount + 1

            // Fetch the action based on the updated count
            val action: () -> Unit = when (char) {
                '1', '2', '3', '4' -> getFallAction(char, currentFallCount)
                '5' -> { { pressAnswerOneFall() } }
                '6' -> { { pressAnswerTwoFall() } }
                '7' -> { { pressAnswerThreeFall() } }
                '8' -> { { pressAnswerFourFall() } }
                else -> { {} }
            }

            handler.postDelayed(action, cumulativeDelay)
            cumulativeDelay += actionDelay
        }
    }

    private fun getFallAction(char: Char, fallCount: Int): () -> Unit {
        val index = fallCount % 4
        return when (char) {
            '1' -> when (index) {
                0 -> ::answerOneFall
                1 -> ::answerOneCopyOneFall
                2 -> ::answerOneCopyTwoFall
                else -> ::answerOneCopyThreeFall
            }
            '2' -> when (index) {
                0 -> ::answerTwoFall
                1 -> ::answerTwoCopyOneFall
                2 -> ::answerTwoCopyTwoFall
                else -> ::answerTwoCopyThreeFall
            }
            '3' -> when (index) {
                0 -> ::answerThreeFall
                1 -> ::answerThreeCopyOneFall
                2 -> ::answerThreeCopyTwoFall
                else -> ::answerThreeCopyThreeFall
            }
            '4' -> when (index) {
                0 -> ::answerFourFall
                1 -> ::answerFourCopyOneFall
                2 -> ::answerFourCopyTwoFall
                else -> ::answerFourCopyThreeFall
            }
            else -> { {} } // Return an empty lambda for any other character
        }
    }
    private val lastFall3 = mutableMapOf<Char, Int>()

    private fun songThreePlay() {
        playMusic(R.raw.twinkle3)
        fadeOutMillis = 0
        fallDuration = 2500
        songLoopCount = 0
        handler.postDelayed({
            lastFall3.clear()
            playSongThreeSequence("522744833611")
        }, 4400)
    }

    private fun playSongThreeSequence(sequence: String) {
        var cumulativeDelay = 0L

        sequence.forEachIndexed { index, char ->
            val action = getFallAction3(char, lastFall3.getOrDefault(char, 0))


            val actionDelay = when (char) {
                '2', '4', '3', '1' -> {
                    val count = lastFall3.getOrDefault(char, 0)
                    if (count % 2 == 0) 200L else 290L
                }
                else -> 500L
            }

            handler.postDelayed(action, cumulativeDelay)
            cumulativeDelay += actionDelay

            lastFall3[char] = lastFall3.getOrDefault(char, 0) + 1
        }


        handler.postDelayed({
            songLoopCount++
            if (songLoopCount < 12) {
                lastFall3.clear()
                playSongThreeSequence(sequence)
            } else {
                songLoopCount = 0 
            }
        }, cumulativeDelay)
    }

    private fun getFallAction3(char: Char, fallCount: Int): () -> Unit {
        val index = fallCount % 4 // Cycle through the original and three copies
        return when (char) {
            '5' -> ::pressAnswerOneFall
            '2' -> listOf(::answerTwoFall, ::answerTwoCopyOneFall, ::answerTwoCopyTwoFall, ::answerTwoCopyThreeFall)[index]
            '7' -> ::pressAnswerThreeFall
            '4' -> listOf(::answerFourFall, ::answerFourCopyOneFall, ::answerFourCopyTwoFall, ::answerFourCopyThreeFall)[index]
            '8' -> ::pressAnswerFourFall
            '3' -> listOf(::answerThreeFall, ::answerThreeCopyOneFall, ::answerThreeCopyTwoFall, ::answerThreeCopyThreeFall)[index]
            '6' -> ::pressAnswerTwoFall
            '1' -> listOf(::answerOneFall, ::answerOneCopyOneFall, ::answerOneCopyTwoFall, ::answerOneCopyThreeFall)[index]
            else -> { {} } // No operation for unmapped characters
        }
    }

    private var songLoopCount4 = 0

    private fun songFourPlay() {
        playMusic(R.raw.twinkle1)
        fadeOutMillis = 0
        fallDuration = 2500
        handler.postDelayed({ songFourPlayHelperOne() }, 2400)
    }

    private fun songFourPlayHelperOne() {
        val actions = listOf(
            Pair({ answerOneFall() }, 0L),
            Pair({ answerOneCopyOneFall() }, 250L),
            Pair({ answerOneCopyTwoFall() }, 500L),
            Pair({ answerOneCopyThreeFall() }, 750L),
            Pair({ answerTwoFall() }, 1150L),
            Pair({ answerTwoCopyTwoFall() }, 1550L),
            Pair({ songFourPlayHelperTwo() }, 1980L)
        )
        executeActionsSequentially(actions)
    }

    private fun songFourPlayHelperTwo() {
        val actions = listOf(
            Pair({ answerThreeFall() }, 0L),
            Pair({ answerThreeCopyOneFall() }, 250L),
            Pair({ answerThreeCopyTwoFall() }, 500L),
            Pair({ answerThreeCopyThreeFall() }, 750L),
            Pair({ answerFourFall() }, 1150L),
            Pair({ answerFourCopyTwoFall() }, 1550L),
            Pair({ checkAndLoopOrEnd() }, 1950L)
        )
        executeActionsSequentially(actions)
    }

    private fun checkAndLoopOrEnd() {
        songLoopCount4 += 1
        if (songLoopCount4 < 12) {
            songFourPlayHelperOne()
        }
    }

    private fun executeActionsSequentially(actions: List<Pair<() -> Unit, Long>>) {
        actions.forEach { action ->
            handler.postDelayed(action.first, action.second)
        }
    }


    private val sequenceForSongFive = buildSequenceForSongFive()
    private val lastFall5 = mutableMapOf<Char, Int>()

    private fun songFivePlay() {
        playMusic(R.raw.twinkle4)
        fadeOutMillis = 0
        fallDuration = 2500
        songLoopCount = 0
        handler.postDelayed({
            playSequence5(sequenceForSongFive)
            // Calculate the delay based on the sequence and individual delay, not a fixed 1000L
            val delayForNextRound = sequenceForSongFive.length * 249L // Adjust if your actions' timing changes
            scheduleNextRound(delayForNextRound)
        }, 4405)
    }

    private fun buildSequenceForSongFive(): String {
        return "1111222233334444"
    }

    private fun playSequence5(sequence: String) {
        lastFall5.clear() // Reset for each loop iteration
        var delay = 0L

        sequence.forEach { number ->
            handler.postDelayed({
                when (number) {
                    '1', '2', '3', '4' -> manageFallAction(number)
                }
                lastFall5[number] = lastFall5.getOrDefault(number, 0) + 1
            }, delay)
            delay += 250L
        }
    }

    private fun manageFallAction(number: Char) {
        val count = lastFall5.getOrDefault(number, 0) % 4
        when (number) {
            '1' -> listOf(::answerOneFall, ::answerOneCopyOneFall, ::answerOneCopyTwoFall, ::answerOneCopyThreeFall)[count]()
            '2' -> listOf(::answerTwoFall, ::answerTwoCopyOneFall, ::answerTwoCopyTwoFall, ::answerTwoCopyThreeFall)[count]()
            '3' -> listOf(::answerThreeFall, ::answerThreeCopyOneFall, ::answerThreeCopyTwoFall, ::answerThreeCopyThreeFall)[count]()
            '4' -> listOf(::answerFourFall, ::answerFourCopyOneFall, ::answerFourCopyTwoFall, ::answerFourCopyThreeFall)[count]()
        }
    }

    private fun scheduleNextRound(delayForNextRound: Long) {
        handler.postDelayed({
            if (songLoopCount < 11) {
                playSequence5(sequenceForSongFive)
                scheduleNextRound(delayForNextRound)
            } else {
                songLoopCount = 0
            }
            songLoopCount++
        }, delayForNextRound)
    }



    private fun playSequence(sequence: String) {
        var delay = 0L
        val lastFall = mutableMapOf<Char, Int>()

        sequence.forEach { number ->
            handler.postDelayed({
                when (number) {
                    '1' -> {
                        when (lastFall.getOrDefault(number, 0) % 4) {
                            0 -> answerOneFall()
                            1 -> answerOneCopyOneFall()
                            2 -> answerOneCopyTwoFall()
                            else -> answerOneCopyThreeFall()
                        }
                    }
                    '2' -> {
                        when (lastFall.getOrDefault(number, 0) % 4) {
                            0 -> answerTwoFall()
                            1 -> answerTwoCopyOneFall()
                            2 -> answerTwoCopyTwoFall()
                            else -> answerTwoCopyThreeFall()
                        }
                    }
                    '3' -> {
                        when (lastFall.getOrDefault(number, 0) % 4) {
                            0 -> answerThreeFall()
                            1 -> answerThreeCopyOneFall()
                            2 -> answerThreeCopyTwoFall()
                            else -> answerThreeCopyThreeFall()
                        }
                    }
                    '4' -> {
                        when (lastFall.getOrDefault(number, 0) % 4) {
                            0 -> answerFourFall()
                            1 -> answerFourCopyOneFall()
                            2 -> answerFourCopyTwoFall()
                            else -> answerFourCopyThreeFall()
                        }
                    }
                    '5' -> pressAnswerOneFall()
                    '6' -> pressAnswerTwoFall()
                    '7' -> pressAnswerThreeFall()
                    '8' -> pressAnswerFourFall()

                }
                // Increment or reset the fall count
                lastFall[number] = lastFall.getOrDefault(number, 0) + 1
            }, delay)
            delay += 1000 // Adjust the interval as needed
        }
    }


    private fun songSixPlay() {
        playMusic(R.raw.frenchfolksong)
        fadeOutMillis = 700
        handler.postDelayed({
            songSixPlayHelperOne()
        }, 3300)
    }

    private var songLoopCount2 = 0
    private val lastFall = mutableMapOf<Char, Int>()

    private fun songSixPlayHelperOne() {
        playSequence("123432123444")
        val totalDelayForSequenceOne = "123432123444".length * 1000
        handler.postDelayed({
            songSixPlayHelperTwo()
        }, totalDelayForSequenceOne.toLong())
    }

    private fun songSixPlayHelperTwo() {
        if (songLoopCount2 < 2) {
            playSequence("432123432111")
        }
        val totalDelayForSequenceTwo = "432123432111".length * 1000

        songLoopCount2++
        if (songLoopCount2 < 3) {
            handler.postDelayed({
                songSixPlayHelperOne()
            }, totalDelayForSequenceTwo.toLong())
        } else {
            songLoopCount2 = 0
        }
    }


    private fun playGame() {
        // initialization regular answer node
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        val answerFour = findViewById<ImageView>(R.id.answer_four)

        // copies
        // lane 1
        val answerOneCopyOne = findViewById<ImageView>(R.id.answer_one_copy_one)
        val answerOneCopyTwo = findViewById<ImageView>(R.id.answer_one_copy_two)
        val answerOneCopyThree = findViewById<ImageView>(R.id.answer_one_copy_three)
        // lane 2
        val answerTwoCopyOne = findViewById<ImageView>(R.id.answer_two_copy_one)
        val answerTwoCopyTwo = findViewById<ImageView>(R.id.answer_two_copy_two)
        val answerTwoCopyThree = findViewById<ImageView>(R.id.answer_two_copy_three)
        // lane 3
        val answerThreeCopyOne = findViewById<ImageView>(R.id.answer_three_copy_one)
        val answerThreeCopyTwo = findViewById<ImageView>(R.id.answer_three_copy_two)
        val answerThreeCopyThree = findViewById<ImageView>(R.id.answer_three_copy_three)
        // lane 4
        val answerFourCopyOne = findViewById<ImageView>(R.id.answer_four_copy_one)
        val answerFourCopyTwo = findViewById<ImageView>(R.id.answer_four_copy_two)
        val answerFourCopyThree = findViewById<ImageView>(R.id.answer_four_copy_three)

        // initialization for long press answer node
        val pressAnsOne = findViewById<ImageView>(R.id.press_one_top)
        val pressAnsTwo = findViewById<ImageView>(R.id.press_two_top)
        val pressAnsThree = findViewById<ImageView>(R.id.press_three_top)
        val pressAnsFour = findViewById<ImageView>(R.id.press_four_top)

        val btnOne = findViewById<ImageView>(R.id.btn_one)
        val btnTwo = findViewById<ImageView>(R.id.btn_two)
        val btnThree = findViewById<ImageView>(R.id.btn_three)
        val btnFour = findViewById<ImageView>(R.id.btn_four)

        // button pressing handler
        btnPressHelper(
            btnOne,
            answerOne,
            answerOneCopyOne,
            answerOneCopyTwo,
            answerOneCopyThree,
            pressAnsOne,
            "one"
        ) // lane 1
        btnPressHelper(
            btnTwo,
            answerTwo,
            answerTwoCopyOne,
            answerTwoCopyTwo,
            answerTwoCopyThree,
            pressAnsTwo,
            "two"
        ) // lane 2
        btnPressHelper(
            btnThree,
            answerThree,
            answerThreeCopyOne,
            answerThreeCopyTwo,
            answerThreeCopyThree,
            pressAnsThree,
            "three"
        ) // lane 3
        btnPressHelper(
            btnFour,
            answerFour,
            answerFourCopyOne,
            answerFourCopyTwo,
            answerFourCopyThree,
            pressAnsFour,
            "four"
        ) // lane 4
    }

    // func gets called from constructor for each button to detect press
    private fun btnPressHelper(
        btnNode: ImageView,
        ansRegNode: ImageView,
        ansRegNodeCopyOne: ImageView,
        ansRegNodeCopyTwo: ImageView,
        ansRegNodeCopyThree: ImageView,
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
                    recursiveCheckAnswer(
                        btnNode,
                        ansRegNode,
                        ansRegNodeCopyOne,
                        ansRegNodeCopyTwo,
                        ansRegNodeCopyThree,
                        ansPressNode
                    )
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
                    isWrongCounted = false
                    isButtonPressed = false
                    isScoredAdded = false
                    // initialize answerText for conditional expression used in other function
                    if (veryGoodText.visibility == View.VISIBLE ) {
                        fadeOutAnimationHelper(veryGoodText, fadeOutMillis)
                    }
                    if (answerText.visibility == View.VISIBLE) {
                        fadeOutAnimationHelper(answerText, fadeOutMillis)
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
        ansRegNodeCopyOne: ImageView,
        ansRegNodeCopyTwo: ImageView,
        ansRegNodeCopyThree: ImageView,
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
            else if (isClickPerfectOverlap(btnNode, ansRegNode) || isClickPerfectOverlap(
                    btnNode,
                    ansRegNodeCopyOne
                ) || isClickPerfectOverlap(btnNode, ansRegNodeCopyTwo) || isClickPerfectOverlap(
                    btnNode,
                    ansRegNodeCopyThree
                )
            ) {
                perfectScoreHelper(veryGoodText, answerText, scoreText)
                return
            }
            // if semi-correct with short answer node
            else if (isOverlap(btnNode, ansRegNode) || isOverlap(
                    btnNode,
                    ansRegNodeCopyOne
                ) || isOverlap(btnNode, ansRegNodeCopyTwo) || isOverlap(
                    btnNode,
                    ansRegNodeCopyThree
                )
            ) {
                semiScoreHelper(veryGoodText, answerText, scoreText)
                return
            } else {
                answerText.text = "다시 도전해 보세요!"
                veryGoodText.visibility = View.GONE
                answerText.visibility = View.VISIBLE
                // decrement heart by wrong
                if (!isWrongCounted) {
                    isWrongCounted = true
                    val heartLife = findViewById<ImageView>(R.id.heart_life)
                    wrongCount += 1
                    if (wrongCount == 5) {
                        heartLife.setImageResource(R.drawable.heart_two)
                    } else if (wrongCount == 10) {
                        heartLife.setImageResource(R.drawable.heart_one)
                    } else if (wrongCount == 15) {
                        mediaPlayer.let {
                            if (it.isPlaying) {
                                it.stop()
                            }
                            it.release()
                        }

                        val resultIntent = Intent()
                        resultIntent.putExtra("score", score)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }

            handler.postDelayed({
                recursiveCheckAnswer(
                    btnNode,
                    ansRegNode,
                    ansRegNodeCopyOne,
                    ansRegNodeCopyTwo,
                    ansRegNodeCopyThree,
                    ansPressNode
                )
            }, 100)
        }
    }



    private fun semiScoreHelper(
        veryGoodText: ImageView,
        answerText: TextView,
        scoreText: TextView
    ) {
        answerText.text = "정말 잘했어요!!"
        // if for displaying only one for short node
        if (veryGoodText.visibility != View.VISIBLE && answerText.visibility != View.VISIBLE) {
            answerText.visibility = View.VISIBLE

            if (!isScoredAdded) {
                score += 5
                scoreText.text = "$score"
                isScoredAdded = true
            }
            fadeOutAnimationHelper(answerText, fadeOutMillis)
        }
    }

    private fun perfectScoreHelper(
        veryGoodText: ImageView,
        answerText: TextView,
        scoreText: TextView
    ) {
        // for displaying only one for reg node
        if (answerText.visibility != View.VISIBLE && veryGoodText.visibility != View.VISIBLE) {
            veryGoodText.visibility = View.VISIBLE
            if (!isScoredAdded) {
                score += 10
                scoreText.text = "$score"
                isScoredAdded = true
            }
            fadeOutAnimationHelper(veryGoodText, fadeOutMillis)
        }
    }

    private fun isClickPerfectOverlap(btnNode: ImageView, answerNode: ImageView): Boolean {
        val answerLoc = IntArray(2)
        answerNode.getLocationOnScreen(answerLoc)

        val btnLoc = IntArray(2)
        btnNode.getLocationOnScreen(btnLoc)

        val answerOffset = answerNode.height / 4
        val answerRectForPerfect = Rect(
            answerLoc[0],
            answerLoc[1] + answerOffset,
            answerLoc[0] + answerNode.width,
            answerLoc[1] + 3 * answerOffset
        )

        val btnRectForPerfect = Rect(
            btnLoc[0],
            btnLoc[1],
            btnLoc[0] + btnNode.width,
            btnLoc[1] + btnNode.height
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

            override fun onAnimationRepeat(animation: Animation) {
            }
        })

        // making it visible
        node.startAnimation(fadeOutAnimation)
    }

    // -------------- functions for node falling --------------------------

    private fun getWindowHeight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    private fun fallAnimation(answerNode: ImageView, currentScore: Int?) {
        val pressTop = findViewById<ImageView>(R.id.press_one_top)
        val screenHeight = (getWindowHeight(this) + pressTop.height).toFloat()
        val animator = answerNode.animate()
            .translationYBy(screenHeight) // 3000f - y length screen
            .setDuration(fallDuration) // 4000 secs

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

    // -------------- press answer node fall functions ----------------------

    private fun pressAnswerOneFall() {
        val currentScore = score

        val pressAnsOne = findViewById<ImageView>(R.id.press_answer_one)
        val pressAnsOneTop = findViewById<ImageView>(R.id.press_one_top)

        pressAnsOneTop.visibility = View.VISIBLE
        fallAnimation(pressAnsOne, null)
        fallAnimation(pressAnsOneTop, currentScore)
    }

    private fun pressAnswerTwoFall() {
        val currentScore = score
        val pressAnsTwo = findViewById<ImageView>(R.id.press_answer_two)
        val pressAnsTwoTop = findViewById<ImageView>(R.id.press_two_top)

        fallAnimation(pressAnsTwo, null)
        fallAnimation(pressAnsTwoTop, currentScore)
    }

    private fun pressAnswerThreeFall() {
        val currentScore = score
        val pressAnsThree = findViewById<ImageView>(R.id.press_answer_three)
        val pressAnsThreeTop = findViewById<ImageView>(R.id.press_three_top)

        fallAnimation(pressAnsThree, null)
        fallAnimation(pressAnsThreeTop, currentScore)
    }

    private fun pressAnswerFourFall() {
        val currentScore = score
        val pressAnsFour = findViewById<ImageView>(R.id.press_answer_four)
        val pressAnsFourTop = findViewById<ImageView>(R.id.press_four_top)

        fallAnimation(pressAnsFour, null)
        fallAnimation(pressAnsFourTop, currentScore)
    }

    // ----------- regular answer node fall functions -----------------

    private fun answerOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_one)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerOneCopyOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_one_copy_one)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerOneCopyTwoFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_one_copy_two)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerOneCopyThreeFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_one_copy_three)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerTwoFall() {
        val currentScore = score
        val answerTwo = findViewById<ImageView>(R.id.answer_two)
        fallAnimation(answerTwo, currentScore)
    }

    private fun answerTwoCopyOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_two_copy_one)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerTwoCopyTwoFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_two_copy_two)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerTwoCopyThreeFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_two_copy_three)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerThreeFall() {
        val currentScore = score
        val answerThree = findViewById<ImageView>(R.id.answer_three)
        fallAnimation(answerThree, currentScore)
    }

    private fun answerThreeCopyOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_three_copy_one)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerThreeCopyTwoFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_three_copy_two)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerThreeCopyThreeFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_three_copy_three)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerFourFall() {
        val currentScore = score
        val answerFour = findViewById<ImageView>(R.id.answer_four)
        fallAnimation(answerFour, currentScore)
    }

    private fun answerFourCopyOneFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_four_copy_one)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerFourCopyTwoFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_four_copy_two)
        fallAnimation(answerOne, currentScore)
    }

    private fun answerFourCopyThreeFall() {
        val currentScore = score
        val answerOne = findViewById<ImageView>(R.id.answer_four_copy_three)
        fallAnimation(answerOne, currentScore)
    }
}