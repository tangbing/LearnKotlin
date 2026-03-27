package com.example.filepersistencetest

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import androidx.core.app.AppComponentFactory
import com.example.filepersistencetest.ui.theme.FilePersistenceTestTheme
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val button = findViewById<Button>(R.id.writeCacheButton)
        button.setOnClickListener {
           // Toast.makeText(this, "", )
            val editText = findViewById<EditText>(R.id.editText)
            val inputText = editText.text.toString()
            save(inputText)
        }

        val readbutton = findViewById<Button>(R.id.readCacheButton)
        readbutton.setOnClickListener {
            // Toast.makeText(this, "", )
            val contentText = findViewById<TextView>(R.id.contentText)
             contentText.text = read()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun read(): String {
        val content = StringBuilder()
        try {
            val input = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }

    fun save(inputText: String) {
        try {
            // 系统会自动到/data/data/<package name>/files/目录下生成这个文件
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            // 保证在Lambda 表达式中的代码全部执行完之后自动将外层的流关闭
            writer.use {
                it.write(inputText)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

