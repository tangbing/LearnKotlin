package com.example.firstactivity.chat

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstactivity.R

class ChatRecyclerActivity : AppCompatActivity(), View.OnClickListener {

    private val msgList = ArrayList<Msg>()
    private lateinit var adapter: MsgAdapter
    var recyclerView: RecyclerView? = null
    var send: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_recycler)
        initMsg()

        recyclerView = findViewById(R.id.chat_recyclerView)
        val layoutMessage = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutMessage
        if (!::adapter.isInitialized) { // 判断是否已经初始化，防止多次初始化
            adapter = MsgAdapter(msgList)
        }
        recyclerView?.adapter = adapter
        send = findViewById(R.id.chat_send)
        send?.setOnClickListener(this)

    }

    private fun initMsg() {
        val msg1 = Msg("Hello guy.", Msg.TYPE_RECEIVED)
        msgList.add(msg1)

        val msg2 = Msg("Hello Who is that?", Msg.TYPE_SEND)
        msgList.add(msg2)

        val msg3 = Msg("That is tom, Nick talking to you.", Msg.TYPE_RECEIVED)
        msgList.add(msg3)

    }

    override fun onClick(v: View?) {
        when (v) {
            send -> {
                val inputText: EditText = findViewById(R.id.chat_inputText)
                val content = inputText.text.toString()
                if (content.isNotEmpty()) {
                    val msg = Msg(content, Msg.TYPE_SEND)
                    msgList.add(msg)
                    adapter.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新 RecyclerView 中的显示
                    recyclerView?.scrollToPosition(msgList.size -1) // 将 RecyclerView 定位到最后一行
                    inputText.setText("") // 清空输入框中的内容
                }
            }
        }
    }
}