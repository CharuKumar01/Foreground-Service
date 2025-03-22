package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
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
//    private val PERMISSIONS = arrayOf(
//        Manifest.permission.CAMERA,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//    private val REQUEST_CODE = 101

    private lateinit var bind: ActivityMainBinding
    @SuppressLint("ObsoleteSdkInt")
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
//        checkPermissions()
        val serviceIntent = Intent(this, MusicService::class.java)

        val play = bind.btnPlay
        val pause = bind.btnPause

        play.setOnClickListener {
            Log.d("charu", "Play button clicked")
            play.visibility = View.GONE
            pause.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent) // 🚀 FIX: Ensures service starts on Android 8+
            } else {
                startService(serviceIntent)
            }
        }
        pause.setOnClickListener {
            Log.d("charu", "Pause button clicked")
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
            stopService(Intent(this, MusicService::class.java))
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

//    private fun checkPermissions(){
//        if (PERMISSIONS.any{ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED}){
//            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE)
//        }
//    }

}