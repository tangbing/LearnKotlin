package com.example.sunnyweather.Logic.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"
    private const val TAG = "SunnyWeatherNetwork"
    private const val MAX_LOG_BYTES = 1024 * 1024L

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val startTime = System.currentTimeMillis()
            Log.d(TAG, "Request -> ${request.method()} ${request.url()}")
            try {
                val response = chain.proceed(request)
                val duration = System.currentTimeMillis() - startTime
                val responseText = response.peekBody(MAX_LOG_BYTES).string()
                Log.d(
                    TAG,
                    "Response <- ${response.code()} ${response.message()} (${duration}ms) " +
                        "${response.request().url()}\n$responseText"
                )
                response
            } catch (e: Exception) {
                val duration = System.currentTimeMillis() - startTime
                Log.e(TAG, "Request failed <- (${duration}ms) ${request.method()} ${request.url()}", e)
                throw e
            }
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
