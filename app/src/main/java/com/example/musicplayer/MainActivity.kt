package com.example.musicplayer

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private val PERMISSIONS = arrayOf(
//        Manifest.permission.CAMERA,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//    private val REQUEST_CODE = 101

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
//        checkPermissions()

        val play = bind.btnPlay
        val pause = bind.btnPause

        play.setOnClickListener {
            play.visibility = View.GONE
            pause.visibility = View.VISIBLE
        }
        pause.setOnClickListener {
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
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