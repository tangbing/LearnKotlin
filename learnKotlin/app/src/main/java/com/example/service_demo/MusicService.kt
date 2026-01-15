package com.example.service_demo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Android 8.0 以上必须先创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "音乐播放器",
                NotificationManager.IMPORTANCE_LOW // 低重要性，不会发出声音
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 创建通知
        val notification = NotificationCompat.Builder(
            this,
            "music_channel")
            .setContentTitle("正在播放")
            .setContentText("周杰伦-晴天")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}