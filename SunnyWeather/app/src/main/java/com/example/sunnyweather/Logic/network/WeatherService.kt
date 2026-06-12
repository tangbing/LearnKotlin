package com.example.sunnyweather.Logic.network

import com.example.sunnyweather.Logic.model.DailyResponse
import com.example.sunnyweather.Logic.model.RealtimeResponse
import com.example.sunnyweather.SunnyWeatherApplication
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{Lng},{Lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String) : Call<RealtimeResponse>


    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{Lng},{Lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String) : Call<DailyResponse>
}