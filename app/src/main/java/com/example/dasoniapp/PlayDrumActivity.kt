package com.example.dasoniapp;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ImageButton
import android.media.MediaPlayer
import android.animation.ObjectAnimator

class PlayDrumActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    private var isHiHatPedalOn = false

    private var soundMap = mutableMapOf(
        R.id.imageView133 to R.raw.hihatc,
        R.id.imageView134 to R.raw.symbs,
        R.id.imageView136 to R.raw.symbm,
        R.id.imageView129 to R.raw.tm1,
        R.id.imageView130 to R.raw.tm2,
        R.id.imageView132 to R.raw.tm3,
        R.id.imageView131 to R.raw.snare,
        R.id.imageView128 to R.raw.kick
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drum)

        setClickListeners()

        val hiHatPedal = findViewById<ImageView>(R.id.imageView137)
        hiHatPedal.setOnClickListener {
            isHiHatPedalOn = !isHiHatPedalOn
            hiHatPedal.setImageResource(if (isHiHatPedalOn) R.drawable.hihat_pedal_on else R.drawable.hihat_pedal_off)
            resetSoundMap()
            setClickListeners()
        }

        val exitButton = findViewById<ImageButton>(R.id.imageButton24)
        exitButton.setOnClickListener {
            finish()
        }
    }

    private fun resetSoundMap() {
        soundMap = mutableMapOf(
            R.id.imageView133 to if (isHiHatPedalOn) R.raw.hihato else R.raw.hihatc,
            R.id.imageView134 to R.raw.symbs,
            R.id.imageView136 to R.raw.symbm,
            R.id.imageView129 to R.raw.tm1,
            R.id.imageView130 to R.raw.tm2,
            R.id.imageView132 to R.raw.tm3,
            R.id.imageView131 to R.raw.snare,
            R.id.imageView128 to R.raw.kick
        )
    }

    private fun setClickListeners() {
        soundMap.forEach { (imageViewId, soundResourceId) ->
            val imageView = findViewById<ImageView>(imageViewId)
            imageView.setOnClickListener {
                playSound(soundResourceId)
                showAndFadeOutImageView(imageView)
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
        imageView.visibility = ImageView.VISIBLE

        ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f).apply {
            duration = 1000
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
