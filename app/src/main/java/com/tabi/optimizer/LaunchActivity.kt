package com.tabi.optimizer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    // Roblox's real Android package name
    private val ROBLOX_PACKAGE = "com.roblox.client"

    private var advanced = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val videoView = findViewById<VideoView>(R.id.launchVideoView)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.intro_video}")
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = false
            videoView.start()
        }

        videoView.setOnCompletionListener { launchRoblox() }
        videoView.setOnErrorListener { _, _, _ ->
            launchRoblox()
            true
        }
    }

    private fun launchRoblox() {
        if (advanced) return
        advanced = true

        val intent = packageManager.getLaunchIntentForPackage(ROBLOX_PACKAGE)
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Roblox isn't installed — opening the Play Store", Toast.LENGTH_LONG).show()
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$ROBLOX_PACKAGE")))
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$ROBLOX_PACKAGE")
                    )
                )
            }
        }
        finish()
    }
}
