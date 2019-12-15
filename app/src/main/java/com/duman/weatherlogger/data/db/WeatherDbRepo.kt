package com.duman.weatherlogger.data.db

import android.app.Application
import com.duman.weatherlogger.data.model.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WeatherDbRepo(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var weatherDao: WeatherDao? = null

    init {
        val db = WeatherDatabase.getDatabase(application)
        weatherDao = db?.getWeatherDao()


    }

    fun getWeatherList() = weatherDao?.getWeatherList()

    fun addWeatherData(data: WeatherData) {
        launch {
            insertWeatherData(data)
        }
    }

    private suspend fun insertWeatherData(data: WeatherData) {
        withContext(Dispatchers.IO) {
            weatherDao?.insertWeatherData(data)
        }
    }
}