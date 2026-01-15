package com.example.service_demo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicControlService : Service() {

    private val binder = LocalBinder()
    private var currentSong = "是是是"

    // 这个是一个控制器，Activity 通过它控制 Service
    inner class LocalBinder : Binder() {
        fun getService(): MusicControlService = this@MusicControlService
    }

    override fun onBind(intent: Intent?) : IBinder {
        return binder
    }


    // 提供给 Activity 调用的方法
    fun playMusic() {
        Log.d("MusicService", "开始播放: $currentSong")
    }

    fun nextSong() {
        currentSong = "稻香"
        Log.d("MusicService", "切换到: $currentSong")
    }

    fun getCurrentSong(): String = currentSong

}