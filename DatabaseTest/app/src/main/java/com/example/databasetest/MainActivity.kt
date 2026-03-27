package com.example.databasetest

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import com.example.databasetest.ui.theme.DatabaseTestTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val createButton = findViewById<Button>(R.id.createDatabase)
        val addDataBtn = findViewById<Button>(R.id.addDataBtn)
        val updateDataBtn = findViewById<Button>(R.id.updateDataBtn)
        val deleteDataBtn = findViewById<Button>(R.id.deleteDataBtn)
        val queryDataBtn = findViewById<Button>(R.id.queryDataBtn)
        val replaceDataBtn = findViewById<Button>(R.id.replaceDataBtn)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 2)

        createButton.setOnClickListener {
            dbHelper.writableDatabase

            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putString("name", "Tom")
                putInt("age", 28)
                putBoolean("married", false)
            }


        }

        addDataBtn.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values1 = ContentValues().apply {
                put("name", "THe Da Vinci Code")
                put("author", "Dan Brown")
                put("pages", 454)
                put("price", 16.96)
            }

            db.insert("Book", null, values1) // 插入第一条数据
//            val value2 = ContentValues().apply {
//                put("name", "The Lost Symbol")
//                put("author", "Dan Brown")
//                put("page", "510")
//                put("price", "19.95")
//            }
            val value2 = cvOf(
                "name" to "Game Of Thrones",
                "author" to "xxxx",
                "pages" to 720,
                "price" to 20.85)

            db.insert("Book", null, value2)
        }

        updateDataBtn.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("price", 10.99)
            db.update("Book", values, "name = ?", arrayOf("The Da Vinci Code"))
        }


        deleteDataBtn.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.delete("Book", "pages > ?", arrayOf("400"))
        }

        queryDataBtn.setOnClickListener {
            val db = dbHelper.writableDatabase
            // 查询 Book 表中的所有数据
            val cursor = db.query("Book", null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    // 遍历 Cursor 对象，取出数据并打印
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                        Log.d("MainActivity", "Book name is $name")
                        Log.d("MainActivity", "Book author is $author")
                        Log.d("MainActivity", "Book pages is $pages")
                        Log.d("MainActivity", "Book price is $price")

                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        replaceDataBtn.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.beginTransaction() // 开启事务
            try {
                db.delete("Book", null, null)
                if (true) {
                    // 手动抛出一个异常，让事务失败
                    throw NullPointerException()
                }
                val values = ContentValues().apply {
                    put("name", "Games of Thrones")
                    put("author", "George Martin")
                    put("pages", 720)
                    put("price", 20.85)
                }
                db.insert("Book", null, null)
                db.setTransactionSuccessful() // 事务已经执行成功
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction() // 结束事务
            }
        }

    }
}
