package com.example.musicplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestNotificationPermission()

        val play = bind.btnPlay
        val pause = bind.btnPause
        val stopBtn = bind.stopBtn

        stopBtn.setOnClickListener {
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
            val stopIntent = Intent(this, MusicService::class.java).apply {
                action = "STOP"
            }
            stopService(stopIntent)
        }

        play.setOnClickListener {
            Log.d("charu", "Play button clicked")
            play.visibility = View.GONE
            pause.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val startIntent = Intent(this, MusicService::class.java).apply {
                    action = "START"
                }
                startForegroundService(startIntent) // 🚀 FIX: Ensures service starts on Android 8+
            } else {
                startService(Intent(this, MusicService::class.java))
            }
        }
        pause.setOnClickListener {
            Log.d("charu", "Pause button clicked")
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
            val pauseIntent = Intent(this, MusicService::class.java).apply {
                action = "PAUSE"
            }
            startForegroundService(pauseIntent)
        }

    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

}