package com.example.webviewtest

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object HttpUtil {
    fun sendHttpRequest(adress: String, callbackListener: HttpCallbackListener) {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL(adress)
                connection = url.openConnection() as HttpURLConnection
                connection.readTimeout = 8000
                connection.connectTimeout = 8000
                val input = connection.getInputStream()
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                callbackListener.onFinish(response.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                callbackListener.onError(e)
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun sendOkHttpRequest(adress: String, callback: okhttp3.Callback) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(adress)
            .build()
        client.newCall(request).enqueue(callback)
    }
}


interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}


