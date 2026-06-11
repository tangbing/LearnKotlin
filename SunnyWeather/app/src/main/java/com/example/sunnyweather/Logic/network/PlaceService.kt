package com.example.sunnyweather.Logic.network

import com.example.sunnyweather.Logic.model.PlaceResponse
import com.example.sunnyweather.SunnyWeatherApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query") query: String): Call<PlaceResponse>
}
