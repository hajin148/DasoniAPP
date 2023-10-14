package com.example.dasoniapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PlayMusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piano)

        val soundMap = mapOf(
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

        soundMap.forEach { (imageViewId, soundResourceId) ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                playSound(soundResourceId)
            }
        }
    }

    private fun playSound(soundResourceId: Int) {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(this, soundResourceId)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
