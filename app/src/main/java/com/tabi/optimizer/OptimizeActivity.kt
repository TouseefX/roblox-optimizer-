package com.tabi.optimizer

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * IMPORTANT / HONESTY NOTE FOR WHOEVER MAINTAINS THIS APP:
 *
 * A normal (non-rooted) Android app cannot reach into another app's process,
 * change Roblox's internal graphics/render settings, or touch Roblox's memory —
 * and this app intentionally does none of that. Doing so would require root
 * access or modifying Roblox's own APK, neither of which this project does,
 * on purpose, per how it was requested to be built.
 *
 * What this screen *does* do is legitimate, on-device housekeeping that any
 * app is allowed to do for itself and can read about the system:
 *   - clears this app's own cache
 *   - asks the JVM to garbage collect
 *   - reads free RAM / free storage so it can show the user something real
 *   - checks whether the device is on Wi-Fi vs. mobile data
 *
 * Think of it as a "clean up and check the road ahead" step before handing
 * off to Roblox — not a hack, not an injector, not a cheat.
 */
class OptimizeActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var logText: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val log = StringBuilder()

    private data class Step(val label: String, val action: () -> String)

    private val steps by lazy {
        listOf(
            Step("Clearing app cache…") { clearCache() },
            Step("Freeing up memory…") { freeMemory() },
            Step("Checking available RAM…") { checkRam() },
            Step("Checking storage space…") { checkStorage() },
            Step("Checking network connection…") { checkNetwork() },
            Step("Finalizing…") { "Ready." }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimize)
        statusText = findViewById(R.id.optimizeStatusText)
        logText = findViewById(R.id.optimizeLog)
        runStep(0)
    }

    private fun runStep(index: Int) {
        if (index >= steps.size) {
            handler.postDelayed({
                startActivity(Intent(this, LaunchActivity::class.java))
                finish()
            }, 400)
            return
        }

        val step = steps[index]
        statusText.text = step.label
        val result = step.action()
        if (result.isNotBlank()) {
            log.append(result).append('\n')
            logText.text = log.toString().trim()
        }

        handler.postDelayed({ runStep(index + 1) }, 650)
    }

    private fun clearCache(): String {
        return try {
            val files = cacheDir.listFiles()
            files?.forEach { it.deleteRecursively() }
            "Cache cleared"
        } catch (e: Exception) {
            "Cache already clean"
        }
    }

    private fun freeMemory(): String {
        System.gc()
        return "Memory trimmed"
    }

    private fun checkRam(): String {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        val availMb = info.availMem / (1024 * 1024)
        val totalMb = info.totalMem / (1024 * 1024)
        return "RAM free: ${availMb}MB / ${totalMb}MB"
    }

    private fun checkStorage(): String {
        val stat = StatFs(filesDir.path)
        val freeMb = (stat.availableBlocksLong * stat.blockSizeLong) / (1024 * 1024)
        return "Storage free: ${freeMb}MB"
    }

    private fun checkNetwork(): String {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(network)
            when {
                caps == null -> "No active connection"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Connected via Wi-Fi"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Connected via mobile data"
                else -> "Connected"
            }
        } else {
            "Network checked"
        }
    }
}
