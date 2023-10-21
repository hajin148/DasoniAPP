package com.example.dasoniapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView


class PlayMusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var textState = 0 // 0 - 피아노 / 1 - 첼로 / 2 - 바이올린 / 3 - 플룻
    private var currentState = 0 // state 0 center // state 1 left // state 2 right

    private var soundMap = mapOf(
        R.id.imageView106 to R.raw.ptdo,
        R.id.imageView107 to R.raw.ptre,
        R.id.imageView108 to R.raw.ptmi,
        R.id.imageView109 to R.raw.ptfa,
        R.id.imageView110 to R.raw.ptsol,
        R.id.imageView111 to R.raw.ptla,
        R.id.imageView112 to R.raw.ptsi,
        R.id.imageView113 to R.raw.ptdo2,
        R.id.imageView114 to R.raw.ptre2,
        R.id.imageView115 to R.raw.ptmi2,
        R.id.imageView116 to R.raw.ptfa2,
        R.id.imageView117 to R.raw.ptsol2,
        R.id.imageView118 to R.raw.ptla2,
        R.id.imageView119 to R.raw.ptsi2,
        R.id.imageView121 to R.raw.ptreb,
        R.id.imageView122 to R.raw.ptmib,
        R.id.imageView123 to R.raw.ptsolb,
        R.id.imageView135 to R.raw.ptlab,
        R.id.imageView138 to R.raw.ptsib,
        R.id.imageView139 to R.raw.ptreb2,
        R.id.imageView140 to R.raw.ptmib2,
        R.id.imageView141 to R.raw.ptsolb2,
        R.id.imageView142 to R.raw.ptlab2,
        R.id.imageView143 to R.raw.ptsib2
    )

    private var animationMap = mapOf(
        R.id.imageView106 to R.id.imageView144,
        R.id.imageView107 to R.id.imageView145,
        R.id.imageView108 to R.id.imageView146,
        R.id.imageView109 to R.id.imageView147,
        R.id.imageView110 to R.id.imageView148,
        R.id.imageView111 to R.id.imageView149,
        R.id.imageView112 to R.id.imageView150,
        R.id.imageView113 to R.id.imageView151,
        R.id.imageView114 to R.id.imageView152,
        R.id.imageView115 to R.id.imageView153,
        R.id.imageView116 to R.id.imageView154,
        R.id.imageView117 to R.id.imageView155,
        R.id.imageView118 to R.id.imageView156,
        R.id.imageView119 to R.id.imageView157
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piano)

        setClickListeners()

        val imageView = findViewById<ImageView>(R.id.imageView104)
        setImageViewTouchListener(imageView)

        val imageView105 = findViewById<ImageView>(R.id.imageView105)
        val textView133 = findViewById<TextView>(R.id.textView133)

        imageView105.setOnClickListener {
            textState = (textState + 1) % 4
            val text = when (textState) {
                0 -> "피아노"
                1 -> "첼로"
                2 -> "바이올린"
                3 -> "플룻"
                else -> throw IllegalStateException("오류")
            }
            textView133.text = text
            resetKeys()
        }
    }

    private fun setClickListeners() {
        soundMap.forEach { (imageViewId, soundResourceId) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                playSound(soundResourceId)

                animationMap[imageViewId]?.let { animImageViewId ->
                    val targetImageView = findViewById<ImageView>(animImageViewId)
                    showAndFadeOutImageView(targetImageView)
                }
            }
        }
    }

    private fun playSound(soundResourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResourceId)
        mediaPlayer?.start()
    }

    private fun showAndFadeOutImageView(imageView: ImageView) {
        imageView.alpha = 1.0f
        imageView.visibility = View.VISIBLE

        ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f).apply {
            duration = 1000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    imageView.visibility = View.GONE
                }
            })
            start()
        }
    }

    private fun setImageViewTouchListener(imageView: ImageView) {
        imageView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handleImageViewTouch(v, event)
                    true
                }
                else -> false
            }
        }
    }

    private fun handleImageViewTouch(v: View, event: MotionEvent) {
        if (event.x < v.width / 2) { // Left side clicked
            when (currentState) {
                2 -> updateImageViewState(R.drawable.piano_fullview_center, 0)
                else -> updateImageViewState(R.drawable.piano_fullview_left, 1)
            }
        } else { // Right side clicked
            when (currentState) {
                1 -> updateImageViewState(R.drawable.piano_fullview_center, 0)
                else -> updateImageViewState(R.drawable.piano_fullview_right, 2)
            }
        }
    }

    private fun updateImageViewState(drawableId: Int, state: Int) {
        findViewById<ImageView>(R.id.imageView104).setImageResource(drawableId)
        currentState = state
        resetKeys()
    }

    private fun resetKeys() {
        // Case Handling for 피아노
        if (textState == 0) {
            when (currentState) {
                0 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.ptdo,
                        R.id.imageView107 to R.raw.ptre,
                        R.id.imageView108 to R.raw.ptmi,
                        R.id.imageView109 to R.raw.ptfa,
                        R.id.imageView110 to R.raw.ptsol,
                        R.id.imageView111 to R.raw.ptla,
                        R.id.imageView112 to R.raw.ptsi,
                        R.id.imageView113 to R.raw.ptdo2,
                        R.id.imageView114 to R.raw.ptre2,
                        R.id.imageView115 to R.raw.ptmi2,
                        R.id.imageView116 to R.raw.ptfa2,
                        R.id.imageView117 to R.raw.ptsol2,
                        R.id.imageView118 to R.raw.ptla2,
                        R.id.imageView119 to R.raw.ptsi2,
                        R.id.imageView121 to R.raw.ptreb,
                        R.id.imageView122 to R.raw.ptmib,
                        R.id.imageView123 to R.raw.ptsolb,
                        R.id.imageView135 to R.raw.ptlab,
                        R.id.imageView138 to R.raw.ptsib,
                        R.id.imageView139 to R.raw.ptreb2,
                        R.id.imageView140 to R.raw.ptmib2,
                        R.id.imageView141 to R.raw.ptsolb2,
                        R.id.imageView142 to R.raw.ptlab2,
                        R.id.imageView143 to R.raw.ptsib2
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
                1 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.ptdo0,
                        R.id.imageView107 to R.raw.ptre0,
                        R.id.imageView108 to R.raw.ptmi0,
                        R.id.imageView109 to R.raw.ptfa0,
                        R.id.imageView110 to R.raw.ptsol0,
                        R.id.imageView111 to R.raw.ptla0,
                        R.id.imageView112 to R.raw.ptsi0,
                        R.id.imageView113 to R.raw.ptdo,
                        R.id.imageView114 to R.raw.ptre,
                        R.id.imageView115 to R.raw.ptmi,
                        R.id.imageView116 to R.raw.ptfa,
                        R.id.imageView117 to R.raw.ptsol,
                        R.id.imageView118 to R.raw.ptla,
                        R.id.imageView119 to R.raw.ptsi,
                        R.id.imageView121 to R.raw.ptreb0,
                        R.id.imageView122 to R.raw.ptmib0,
                        R.id.imageView123 to R.raw.ptsolb0,
                        R.id.imageView135 to R.raw.ptlab0,
                        R.id.imageView138 to R.raw.ptsib0,
                        R.id.imageView139 to R.raw.ptreb,
                        R.id.imageView140 to R.raw.ptmib,
                        R.id.imageView141 to R.raw.ptsolb,
                        R.id.imageView142 to R.raw.ptlab,
                        R.id.imageView143 to R.raw.ptsib
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
                2 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.ptdo2,
                        R.id.imageView107 to R.raw.ptre2,
                        R.id.imageView108 to R.raw.ptmi2,
                        R.id.imageView109 to R.raw.ptfa2,
                        R.id.imageView110 to R.raw.ptsol2,
                        R.id.imageView111 to R.raw.ptla2,
                        R.id.imageView112 to R.raw.ptsi2,
                        R.id.imageView113 to R.raw.ptdo3,
                        R.id.imageView114 to R.raw.ptre3,
                        R.id.imageView115 to R.raw.ptmi3,
                        R.id.imageView116 to R.raw.ptfa3,
                        R.id.imageView117 to R.raw.ptsol3,
                        R.id.imageView118 to R.raw.ptla3,
                        R.id.imageView119 to R.raw.ptsi3,
                        R.id.imageView121 to R.raw.ptreb2,
                        R.id.imageView122 to R.raw.ptmib2,
                        R.id.imageView123 to R.raw.ptsolb2,
                        R.id.imageView135 to R.raw.ptlab2,
                        R.id.imageView138 to R.raw.ptsib2,
                        R.id.imageView139 to R.raw.ptreb3,
                        R.id.imageView140 to R.raw.ptmib3,
                        R.id.imageView141 to R.raw.ptsolb3,
                        R.id.imageView142 to R.raw.ptlab3,
                        R.id.imageView143 to R.raw.ptsib3
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
            }
        }
        // Case Handling for 첼로
        else if (textState == 1) {

        }
        // Case Handling for 바이올린
        else if (textState == 2) {
            when (currentState) {
                0 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.vldo,
                        R.id.imageView107 to R.raw.vlre,
                        R.id.imageView108 to R.raw.vlmi,
                        R.id.imageView109 to R.raw.vlfa,
                        R.id.imageView110 to R.raw.vlsol,
                        R.id.imageView111 to R.raw.vlla,
                        R.id.imageView112 to R.raw.vlsi,
                        R.id.imageView113 to R.raw.vldo2,
                        R.id.imageView114 to R.raw.vlre2,
                        R.id.imageView115 to R.raw.vlmi2,
                        R.id.imageView116 to R.raw.vlfa2,
                        R.id.imageView117 to R.raw.vlsol2,
                        R.id.imageView118 to R.raw.vlla2,
                        R.id.imageView119 to R.raw.vlsi2,
                        R.id.imageView121 to R.raw.vlreb,
                        R.id.imageView122 to R.raw.vlmib,
                        R.id.imageView123 to R.raw.vlsolb,
                        R.id.imageView135 to R.raw.vllab,
                        R.id.imageView138 to R.raw.vlsib,
                        R.id.imageView139 to R.raw.vlreb2,
                        R.id.imageView140 to R.raw.vlmib2,
                        R.id.imageView141 to R.raw.vlsolb2,
                        R.id.imageView142 to R.raw.vllab2,
                        R.id.imageView143 to R.raw.vlsib2
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
                1 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.vldo0,
                        R.id.imageView107 to R.raw.vlre0,
                        R.id.imageView108 to R.raw.vlmi0,
                        R.id.imageView109 to R.raw.vlfa0,
                        R.id.imageView110 to R.raw.vlsol0,
                        R.id.imageView111 to R.raw.vlla0,
                        R.id.imageView112 to R.raw.vlsi0,
                        R.id.imageView113 to R.raw.vldo,
                        R.id.imageView114 to R.raw.vlre,
                        R.id.imageView115 to R.raw.vlmi,
                        R.id.imageView116 to R.raw.vlfa,
                        R.id.imageView117 to R.raw.vlsol,
                        R.id.imageView118 to R.raw.vlla,
                        R.id.imageView119 to R.raw.vlsi,
                        R.id.imageView121 to R.raw.vlreb0,
                        R.id.imageView122 to R.raw.vlmib0,
                        R.id.imageView123 to R.raw.vlsolb0,
                        R.id.imageView135 to R.raw.vllab0,
                        R.id.imageView138 to R.raw.vlsib0,
                        R.id.imageView139 to R.raw.vlreb,
                        R.id.imageView140 to R.raw.vlmib,
                        R.id.imageView141 to R.raw.vlsolb,
                        R.id.imageView142 to R.raw.vllab,
                        R.id.imageView143 to R.raw.vlsib
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
                2 -> {
                    soundMap = mapOf(
                        R.id.imageView106 to R.raw.vldo2,
                        R.id.imageView107 to R.raw.vlre2,
                        R.id.imageView108 to R.raw.vlmi2,
                        R.id.imageView109 to R.raw.vlfa2,
                        R.id.imageView110 to R.raw.vlsol2,
                        R.id.imageView111 to R.raw.vlla2,
                        R.id.imageView112 to R.raw.vlsi2,
                        R.id.imageView113 to R.raw.vldo3,
                        R.id.imageView114 to R.raw.vlre3,
                        R.id.imageView115 to R.raw.vlmi3,
                        R.id.imageView116 to R.raw.vlfa3,
                        R.id.imageView117 to R.raw.vlsol3,
                        R.id.imageView118 to R.raw.vlla3,
                        R.id.imageView119 to R.raw.vlsi3,
                        R.id.imageView121 to R.raw.vlreb2,
                        R.id.imageView122 to R.raw.vlmib2,
                        R.id.imageView123 to R.raw.vlsolb2,
                        R.id.imageView135 to R.raw.vllab2,
                        R.id.imageView138 to R.raw.vlsib2,
                        R.id.imageView139 to R.raw.vlreb3,
                        R.id.imageView140 to R.raw.vlmib3,
                        R.id.imageView141 to R.raw.vlsolb3,
                        R.id.imageView142 to R.raw.vllab3,
                        R.id.imageView143 to R.raw.vlsib3
                    )
                    animationMap = mapOf(
                        R.id.imageView106 to R.id.imageView144,
                        R.id.imageView107 to R.id.imageView145,
                        R.id.imageView108 to R.id.imageView146,
                        R.id.imageView109 to R.id.imageView147,
                        R.id.imageView110 to R.id.imageView148,
                        R.id.imageView111 to R.id.imageView149,
                        R.id.imageView112 to R.id.imageView150,
                        R.id.imageView113 to R.id.imageView151,
                        R.id.imageView114 to R.id.imageView152,
                        R.id.imageView115 to R.id.imageView153,
                        R.id.imageView116 to R.id.imageView154,
                        R.id.imageView117 to R.id.imageView155,
                        R.id.imageView118 to R.id.imageView156,
                        R.id.imageView119 to R.id.imageView157
                    )
                }
            }
        }
        // Case Handling for 플룻
        else if (textState == 3) {

        }

        soundMap.forEach { (imageViewId, soundResourceId) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                playSound(soundResourceId)

                val targetImageViewId = animationMap[imageViewId]
                targetImageViewId?.let {
                    val targetImageView = findViewById<ImageView>(it)
                    showAndFadeOutImageView(targetImageView)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
