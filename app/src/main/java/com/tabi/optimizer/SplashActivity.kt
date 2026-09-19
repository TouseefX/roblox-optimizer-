package com.tabi.optimizer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

/**
 * First screen the user sees: plays the branded intro video full-screen
 * with the "Optimizer by Tabi" label underneath, then hands off to the
 * optimization screen.
 */
class SplashActivity : AppCompatActivity() {

    private var advanced = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val videoView = findViewById<VideoView>(R.id.splashVideoView)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.intro_video}")
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = false
            videoView.start()
        }

        videoView.setOnCompletionListener { goToOptimize() }

        // If the video can't play for any reason, don't strand the user on a blank screen.
        videoView.setOnErrorListener { _, _, _ ->
            goToOptimize()
            true
        }
    }

    private fun goToOptimize() {
        if (advanced) return
        advanced = true
        startActivity(Intent(this, OptimizeActivity::class.java))
        finish()
    }
}
