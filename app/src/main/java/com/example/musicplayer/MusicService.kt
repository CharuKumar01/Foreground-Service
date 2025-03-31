package com.example.musicplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MusicService::WakeLock")
        wakeLock?.acquire()

        Log.d("charu", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
        }

        val notification = createNotification()
        startForeground(1, notification)
        Log.d("charu", "Service Started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        Log.d("charu", "Service Stopped")
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.char_kadam)
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
        Log.d("charu", "Music Started")
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        Log.d("charu", "Music Paused")
    }

    private fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying){
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }

        wakeLock?.let {
            if (it.isHeld){
                it.release()
            }
            wakeLock = null
        }

        stopSelf()
    }

    private fun createNotification(): Notification {
        val startIntent = Intent(this, MusicService::class.java).apply { action = "START" }
        val startPendingIntent = PendingIntent.getService(
            this, 1, startIntent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val pauseIntent = Intent(this, MusicService::class.java).apply { action = "PAUSE" }
        val pausePendingIntent = PendingIntent.getService(
            this, 2, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, MusicService::class.java).apply { action = "STOP" }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "music_channel")
            .setContentTitle("Music Player")
            .setContentText("Playing Music...")
            .setSmallIcon(R.drawable.ic_music)
            .addAction(R.drawable.ic_play, "Play", startPendingIntent)
            .addAction(R.drawable.ic_pause, "Pause",pausePendingIntent)
            .addAction(R.drawable.ic_pause, "Stop", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}