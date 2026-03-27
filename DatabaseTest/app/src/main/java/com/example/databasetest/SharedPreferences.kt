package com.example.databasetest

import android.content.SharedPreferences

fun SharedPreferences.open(bloc: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.bloc()
    editor.apply()
}