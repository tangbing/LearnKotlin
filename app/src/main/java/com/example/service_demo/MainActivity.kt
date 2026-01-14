package com.example.service_demo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.service_demo.ui.theme.Service_demoTheme
import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log


class MainActivity : AppCompatActivity() {

    private var musicControlService: MusicControlService? = null
    private var isBound = false


    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicControlService = (service as MusicControlService.LocalBinder).getService()
            isBound = true
            Log.d("MainActivity", "已经连接到 Service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicControlService = null
            isBound = false
            Log.d("MainActivity", "已经断开 Service")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<Button>(R.id.startService).setOnClickListener {
//            val intent = Intent(this, UploadService::class.java)
//            startService(intent)
//        }
//
//        findViewById<Button>(R.id.stopService).setOnClickListener {
//            val intent = Intent(this, UploadService::class.java)
//            stopService(intent)
//        }

//        // 检查并请求通知权限(Android 13/ 及以上)
//        requestNotificationPermission()
//
//        // 设置按钮点击事件
//        findViewById<Button>(R.id.startMusicService).setOnClickListener {
//            if (checkNotificationPermission()) {
//                startMusicService()
//            }
//        }

        findViewById<Button>(R.id.bindBtn).setOnClickListener {
            val intent = Intent(this, MusicControlService::class.java)
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        findViewById<Button>(R.id.playBtn).setOnClickListener {
            musicControlService?.playMusic()
        }

        findViewById<Button>(R.id.bindNextBtn).setOnClickListener {
            musicControlService?.nextSong()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 非常重要，不解绑会内存泄露
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    // 权限相关方法
    private fun checkNotificationPermission() : Boolean {
        return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //Android 13+ 检查 POSTNOTIFICATIONS 权限
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkNotificationPermission()) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限获取成功，可以启动服务
                // 可选: 弹出一个提示或自动启动服务
                Log.d("startMusicService", "权限获取成功，可以启动服务")
            }
        } else {
            Log.d("startMusicService", "权限被拒绝")
        }
    }

    // 启动服务方法
    private fun startMusicService() {
        Log.d("startMusicService", "startMusicService called")
        val serviceIntent = Intent(this, MusicService::class.java)

        // 必须使用 startForegroundService() 而不是 startService()
        // 因为你的服务会在 5 秒内调用 startForeground()。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0(Oreo) 及以上必须使用这个方法
            startForegroundService(serviceIntent)
        } else {
            // 低于 Android 8.0 的版本可以使用 startService()
            startService(serviceIntent)
        }

    }

}