package com.example.firstactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val extraData = intent.getStringExtra("extra_data")
        Log.d("-------------------SecondActivity", "extra data is $extraData")

        val buttton2 = findViewById<Button>(R.id.backReturnButton)
        buttton2.setOnClickListener({
            val intent = Intent()
            intent.putExtra("data_return", "Hello firstActivity")
            setResult(RESULT_OK, intent)
            finish()
        })
    }
}