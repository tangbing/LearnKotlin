package com.example.roomtest

import android.widget.Toast
import kotlin.time.Duration

//class ToastUtil {
//    companion object {
//        fun String.show
//    }
//}

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}