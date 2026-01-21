package com.example.firstactivity

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast


class TitleLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.title, this)

        val titleBack: Button = findViewById(R.id.titleBack)
        titleBack.setOnClickListener({
            // context就是一个Activity 实例
            val activity = context as Activity
            activity.finish()
        })

        val titleEdit: Button = findViewById(R.id.titleEdit)
        titleEdit.setOnClickListener({
            Toast.makeText(context, "edit successful!", Toast.LENGTH_SHORT).show()
        })

    }

}