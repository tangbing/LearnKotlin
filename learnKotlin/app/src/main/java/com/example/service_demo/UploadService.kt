package com.example.service_demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class UploadService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ServiceDemo", "Service 创建了")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ServiceDemo", "Service开始工作")

        Thread {
            for (i in 1..5) {
                Log.d("ServiceDemo", "正在上传文件 $i/5")
            }
            stopSelf()
        }.start()
        // 返回值很重要！决定 Service 被系统杀死后怎么办
        return START_STICKY // 被杀死后系统尝试重启，但 Intent 可能为空
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ServiceDemo", "Service 销毁了")
    }

    override fun onBind(intent: Intent): IBinder? {
        TODO("Return the communication channel to the service.")
        return null // 普通服务不需要绑定
    }
}