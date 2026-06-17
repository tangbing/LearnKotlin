package com.example.sunnyweather.Logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.Logic.model.Place
import com.example.sunnyweather.SunnyWeatherApplication
import com.google.gson.Gson

object PlaceDao {

    fun savePlace(place: Place) {
        sharePreferences().edit() {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharePreferences().getString("place", "")
        return Gson().fromJson<Place>(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharePreferences().contains("place")

    private fun sharePreferences() = SunnyWeatherApplication.context.getSharedPreferences(
        "sunny_weather",  Context.MODE_PRIVATE
    )
}