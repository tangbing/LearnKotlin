package com.example.sunnyweather.Logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private const val TAG = "SunnyWeatherNetwork"

    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create(WeatherService::class.java)


    suspend fun searchPlaces(query: String) = placeService.searchPlace(query).await()

    suspend fun getWeather(lng: String, lat: String) = weatherService.getWeather(lng, lat).await()
    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng, lat).await()
    suspend fun getRealtimeWeather(lng: String, lat: String) = weatherService.getRealtimeWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object: Callback<T> {
                override fun onResponse(
                    call: Call<T?>,
                    response: Response<T?>
                ) {
                    if (!response.isSuccessful) {
                        val errorBody = response.errorBody()?.string()
                        continuation.resumeWithException(
                            RuntimeException(
                                "HTTP ${response.code()} ${response.message()} " +
                                    "${call.request().url()}\n$errorBody"
                            )
                        )
                        return
                    }

                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException(
                            "response body is null: HTTP ${response.code()} " +
                                "${response.message()} ${call.request().url()}"
                        )
                    )
                }

                override fun onFailure(call: Call<T?>, t: Throwable) {
                    Log.e(TAG, "Request failed: ${call.request().url()}", t)
                    continuation.resumeWithException(t)
                }

            })
        }
    }


}
