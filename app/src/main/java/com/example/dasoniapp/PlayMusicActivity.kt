package com.example.dasoniapp

import android.media.MediaPlayer
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PlayMusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piano)

        soundMap.forEach { (imageViewId, soundResourceId) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                playSound(soundResourceId)
            }
        }

        val imageView = findViewById<ImageView>(R.id.imageView104)
        setImageViewTouchListener(imageView)
    }

    private fun playSound(soundResourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResourceId)
        mediaPlayer?.start()
    }

    private fun setImageViewTouchListener(imageView: ImageView) {
        imageView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x < v.width / 2) { // Left side clicked
                        when (currentState) {
                            2 -> {
                                imageView.setImageResource(R.drawable.piano_fullview_center)
                                currentState = 0
                                resetKeys()
                            }
                            else -> {
                                imageView.setImageResource(R.drawable.piano_fullview_left)
                                currentState = 1
                                resetKeys()
                            }
                        }
                    } else { // Right side clicked
                        when (currentState) {
                            1 -> {
                                imageView.setImageResource(R.drawable.piano_fullview_center)
                                currentState = 0
                                resetKeys()
                            }
                            else -> {
                                imageView.setImageResource(R.drawable.piano_fullview_right)
                                currentState = 2
                                resetKeys()
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }

    }

    private fun resetKeys() {
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
            }
        }

        // Reattach the onClickListeners with the new sound resources
        soundMap.forEach { (imageViewId, soundResourceId) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                playSound(soundResourceId)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
