package com.example.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpacktest.ui.theme.JetpackTestTheme

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var infoText: TextView
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycle.addObserver(MyObserver())

        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        // ViewModelProvider(<你的Activity或Fragment实例>).get(<你的ViewModel>::class.java)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 传递参数给 ViewModel
        viewModel = ViewModelProvider(this, MainViewModelFactory(countReserved))
            .get(MainViewModel::class.java)

        infoText = findViewById<TextView>(R.id.infoText)
        var plusOneBtn = findViewById<Button>(R.id.plusOneBtn)
        plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }

        var clearBtn = findViewById<Button>(R.id.clearBtn);
        clearBtn.setOnClickListener {
            viewModel.clear()
        }

        var getUserBtn = findViewById<Button>(R.id.getUserBtn);
        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.getUser(userId)
        }

        viewModel.user.observe(this, { user ->
            infoText.text = user.firstName
        })

        viewModel.counter.observe(this,  { count ->
             infoText.text = count.toString()
           }
        )
    }

    fun getUser(userId: String): LiveData<User> {
        return Repository.getUser(userId)
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}


