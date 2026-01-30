package com.example.firstactivity.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstactivity.R

class MsgAdapter(val msgList: List<Msg>): RecyclerView.Adapter<MsgViewHolder>() {

    // 返回当前 position 对应的消息类型
    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return  msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Msg.TYPE_RECEIVED) {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item, parent, false)
       LeftViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item, parent, false)
        RightViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MsgViewHolder,
        position: Int
    ) {
        val msg = msgList[position]
        when (holder) {
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
        }
    }

    override fun getItemCount(): Int = msgList.size

}