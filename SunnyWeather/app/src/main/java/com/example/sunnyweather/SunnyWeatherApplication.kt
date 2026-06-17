package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "2VERyVtoc8804KfT"
    }
//    https://api.caiyunapp.com/v2.5/BNBRvBveaD2VfHVI/114.298572,30.584355/realtime
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
