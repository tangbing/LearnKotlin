package com.example.activitylifecycletest

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.activitylifecycletest.ui.theme.ActivityLifeCycleTestTheme


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity======"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")

        setContentView(R.layout.activity_main)

        val startNormalBtn: Button = findViewById(R.id.startNormalActivityBtn)
        startNormalBtn.setOnClickListener({
            val intent = Intent(this, NormalActivity::class.java)
            startActivity(intent)
        })

        val startDialogActivityBtn: Button = findViewById(R.id.startDialogActivityBtn)
        startDialogActivityBtn.setOnClickListener({
            val intent = Intent(this, DialogActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "onRestart")
    }
}
