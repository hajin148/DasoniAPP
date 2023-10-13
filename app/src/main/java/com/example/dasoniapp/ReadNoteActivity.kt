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
    private var currNoteIndex = -1 // ^
    private val noteMarginTop = listOf<Int>(180, 128, 82, 40, 0, -37, -75, -115, -155, -195, -230, -280)
    private val noteList = listOf<String>("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G")
    private val ansNoteMarginTop = mutableMapOf<String, Int>(
        "C" to 180,
        "D" to 128,
        "E" to 82,
        "F" to 40,
        "G" to 0,
        "A" to -37,
        "B" to -75
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game_practice)

        gamePlay()
    }

    private fun gamePlay() {
        var randNoteIndex: Int
        do {
            randNoteIndex = Random.nextInt(11) // 0~10
        } while (randNoteIndex == currNoteIndex)
        currNoteIndex = randNoteIndex

        if(currNoteIndex == 0) {
            val noteLine: TextView = findViewById(R.id.note_line)
            noteLine.visibility = View.VISIBLE
        }
        val noteImg: ImageView = findViewById(R.id.note_img)
        moveNote(noteImg, noteMarginTop[currNoteIndex])
        noteBtnListner();
    }

    private fun moveNote(img: ImageView, marginTop: Int) {
        val layoutParams = img.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, marginTop, 0, 0)

        img.layoutParams = layoutParams
    }


    private fun noteBtnListner() {
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

    private fun checkAnswer(noteStr: String) {
        val answerTxt: TextView = findViewById(R.id.answer_txt)
        val answerNoteImg: ImageView = findViewById(R.id.answer_note_img)
        answerNoteImg.visibility = View.VISIBLE

        val answerNoteLine: TextView = findViewById(R.id.answer_note_line)

        if (noteList[currNoteIndex] == noteStr) {
            answerTxt.setText("$noteStr, 정답입니다!")
            moveNote(answerNoteImg, noteMarginTop[currNoteIndex])

            if(currNoteIndex == 0) {
                answerNoteLine.visibility = View.VISIBLE
            }
        } else {
            answerTxt.setText("$noteStr, 틀렸습니다!")
            moveNote(answerNoteImg, ansNoteMarginTop[noteStr] as Int)

            if(noteStr == "C") {
                answerNoteLine.visibility = View.VISIBLE
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            answerTxt.text = ""
            answerNoteImg.visibility = View.INVISIBLE
            if(currNoteIndex == 0) {
                val noteLine: TextView = findViewById(R.id.note_line)
                noteLine.visibility = View.INVISIBLE
            }
            answerNoteLine.visibility = View.INVISIBLE
            gamePlay()
        }, 1500)
    }
}