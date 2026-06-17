package com.example.sunnyweather.Logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.Logic.dao.PlaceDao
import com.example.sunnyweather.Logic.model.Place
import com.example.sunnyweather.Logic.model.Weather
import com.example.sunnyweather.Logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Log.d("searchPlaces success", "placeResponse  response status is ${placeResponse.status}")
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status} "))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        val weatherResponse = SunnyWeatherNetwork.getWeather(lng, lat)
        Log.d("refreshWeather success", "weather response status is ${weatherResponse.status}")
        if (weatherResponse.status == "ok") {
            val weather = Weather(weatherResponse.result.realtime, weatherResponse.result.daily)
            Result.success(weather)
        } else {
            Result.failure(RuntimeException("weather response status is ${weatherResponse.status}"))
        }
    }

    private fun <T> fire(context: CoroutineContext, bloc: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            bloc()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}
