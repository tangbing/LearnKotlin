package com.example.broadcastbestpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat


open class BaseActivity : AppCompatActivity() {

    lateinit var receiver: ForceOfflineReceiver
    companion object {
        const val ACTION_FORCE_OFFLINE = "com.example.broadcastbestpractice.FORCE_OFFLINE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityController.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.removeActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_FORCE_OFFLINE)
        receiver = ForceOfflineReceiver()
        // Register dynamically to receive in-app broadcast
        if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.registerReceiver(
                this,
                receiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        } else {
           // registerReceiver(receiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(receiver)
        } catch (_: Exception) {
            // ignore if not registered
        }
    }

    inner class ForceOfflineReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Use the Activity context to show a non-cancelable dialog
            AlertDialog.Builder(this@BaseActivity)
                .setTitle("Warning")
                .setMessage("You are forced to be offline. Please try to login again.")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    ActivityController.finishAll()
                    val i = Intent(this@BaseActivity, LoginActivity::class.java)
                    startActivity(i)
                }
                .show()
        }
    }

}
