package com.example.dasoniapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class ReadNoteActivity : AppCompatActivity() {
    private var currNoteIndex = -1

    // for upper note
    private val noteMarginTop = listOf<Int>(180, 128, 82, 40, 0, -37, -75, -115, -155, -195, -230, -280)
    private val noteList = listOf<String>("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G")
    private val ansNoteMarginTop = mutableMapOf<String, Int>(
        "C" to noteMarginTop[0],
        "D" to noteMarginTop[1],
        "E" to noteMarginTop[2],
        "F" to noteMarginTop[3],
        "G" to noteMarginTop[4],
        "A" to noteMarginTop[5],
        "B" to noteMarginTop[6]
    )

    // for lower note
    private val noteMarginBottom = listOf<Int>(-2900, -2763, -2603, -2460, -2320, -2170, -2030, -1888, -1740, -1600, -1450)
    private val lowNoteList = listOf<String>("F", "G", "A", "B", "C", "D", "E", "F", "G", "A", "B")
    private val ansLowNoteMarginTop = mutableMapOf<String, Int>(
        "C" to noteMarginBottom[4],
        "D" to noteMarginBottom[5],
        "E" to noteMarginBottom[6],
        "F" to noteMarginBottom[7],
        "G" to noteMarginBottom[8],
        "A" to noteMarginBottom[9],
        "B" to noteMarginBottom[10]
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game_practice)

        gamePlay()
    }

    private fun moveNote(img: ImageView, marginTop: Int, marginBottom: Int) {
        val layoutParams = img.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marginTop, 0, marginBottom)

        img.layoutParams = layoutParams
    }

    private fun gamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(11) // 0~10
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        // random choose to play low note or high note
        when(Random.nextInt(2)) { //0~1
            0 -> highGamePlay()
            1 -> lowGamePlay()
        }
    }

    private fun lowGamePlay() {
        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, 0, noteMarginBottom[currNoteIndex])
        noteBtnListner(::lowCheckAnswer)
    }

    private fun highGamePlay() {
        if(currNoteIndex == 0) {
            val noteLine: TextView = findViewById(R.id.note_line)
            noteLine.visibility = View.VISIBLE
        }
        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex], 0)
        noteBtnListner(::highCheckAnswer)
    }

    private fun noteBtnListner(checkAnswer: (String) -> Unit) {
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

    private fun highCheckAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_txt)
        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE

        if (noteList[currNoteIndex] == noteStr) {
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, noteMarginTop[currNoteIndex],0)

            // if user pressed lower C note, then show line on answer note
            if(currNoteIndex == 0) {
                answerNoteLine.setTextColor(0xFF30D5D8.toInt())
                answerNoteLine.visibility = View.VISIBLE
            }
        } else {
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 틀렸습니다!"
            moveNote(answerNoteImg, ansNoteMarginTop[noteStr] as Int, 0)

            // if user pressed C, then show line on answer note
            if(noteStr == "C") {
                answerNoteLine.setTextColor(0xFFFF46A9.toInt())
                answerNoteLine.visibility = View.VISIBLE
            }
        }

        // wait for 1.5 seconds to run
        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE

            // hide all the lines on note
            if(currNoteIndex == 0) {
                val noteLine: TextView = findViewById(R.id.note_line)
                noteLine.visibility = View.INVISIBLE
            }
            answerNoteLine.visibility = View.INVISIBLE

            gamePlay()
        }, 1500)
    }

    private fun lowCheckAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_txt)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE

        if(lowNoteList[currNoteIndex] == noteStr) {
            answerNoteImg.setImageResource(R.drawable.note_right)
            answerTxt.text = "$noteStr, 정답입니다!"
            moveNote(answerNoteImg, 0,noteMarginBottom[currNoteIndex])
        }
        else {
            answerNoteImg.setImageResource(R.drawable.note_wrong)
            answerTxt.text = "$noteStr, 틀렸습니다!"
            moveNote(answerNoteImg, 0, ansLowNoteMarginTop[noteStr] as Int)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // hide answer text
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE

            gamePlay()
        }, 1500)
    }

}