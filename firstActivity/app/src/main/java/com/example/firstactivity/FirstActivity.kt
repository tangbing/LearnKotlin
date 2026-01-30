package com.example.firstactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firstactivity.chat.ChatRecyclerActivity
import com.example.firstactivity.customListView.CustomListViewActivity
import com.example.firstactivity.horRecyclerView.HorRecyclerViewActivity
import com.example.firstactivity.recyclerView.RecyclerViewActivity

class FirstActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_layout)



        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "click me", Toast.LENGTH_SHORT).show()

            /// 显式调用
//            val intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)

            // 隐式调用
//            val intent = Intent("com.example.firstactivity.ACTION_START")
//            intent.addCategory("com.example.firstactivity.MY_CATEGORY")
//            startActivity(intent)

//            // 更多隐式Intent的用法
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse("https:www.baidu.com")
//            startActivity(intent)


//            // 向下一个界面传递数据
//            val data = "Hello SecondActivity"
//            val intent = Intent(this, SecondActivity::class.java)
//            intent.putExtra("extra_data", data)
//            startActivity(intent)


            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, 1)


        }

        val finishBtn: Button = findViewById(R.id.finishBtn)
        finishBtn.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
           // finish()
        }

        // 系统 listview
        val listViewBtn: Button = findViewById(R.id.listViewBtn)
        listViewBtn.setOnClickListener {
            val intent = Intent(this, ListViewTest::class.java)
            startActivity(intent)
        }

        // 自定义 cell custom listview
        val customListViewBtn: Button = findViewById(R.id.customListViewBtn)
        customListViewBtn.setOnClickListener {
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

        // 自定义 cell custom listview
        val horRecyclerViewBtn: Button = findViewById(R.id.hor_RecyclerViewBtn)
        horRecyclerViewBtn.setOnClickListener {
            val intent = Intent(this, HorRecyclerViewActivity::class.java)
            startActivity(intent)
        }


        val chat_RecyclerViewBtn: Button = findViewById(R.id.chat_RecyclerViewBtn)
        chat_RecyclerViewBtn.setOnClickListener {
            val intent = Intent(this, ChatRecyclerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                val returnedData = data?.getStringExtra("data_return")
                Log.d("=============FirstActivity", "returned data is $returnedData")
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_item -> Toast.makeText(this, "you clicked Add", Toast.LENGTH_SHORT).show()

            R.id.remove_item -> Toast.makeText(this,"You clicked Remove", Toast.LENGTH_SHORT).show()
        }
        return  true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return  true
    }
}