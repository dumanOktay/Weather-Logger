package com.duman.weatherlogger.data.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duman.weatherlogger.data.WeatherDataSource
import com.duman.weatherlogger.data.WeatherRepository
import com.duman.weatherlogger.data.db.WeatherDbRepo
import com.duman.weatherlogger.data.model.LatLong
import com.duman.weatherlogger.data.model.WeatherData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class WeatherViewModel(app: Application) : AndroidViewModel(app) {
    private val dataSource: WeatherDataSource = WeatherRepository()
    private val dbRepo = WeatherDbRepo(app)

    var lastLoc = LatLong(0f, 0f)

    val weatherLiveData = MutableLiveData<WeatherData?>()

    init {
        val client = LocationServices.getFusedLocationProviderClient(app)
        client.lastLocation.addOnSuccessListener { location: Location? ->

            if (location != null) {
                lastLoc = LatLong(location.latitude.toFloat(), location.longitude.toFloat())
            }
            viewModelScope.launch {
                getWeatherData()
            }
        }

    }

    fun getWeatherList() = dbRepo.getWeatherList()
    private fun getWeatherData() {
        dataSource.getTempService(
            latLong = lastLoc,
            dataCallback = object : WeatherDataSource.TempDataCallback {
                override fun onLoadData(data: WeatherData) {
                    weatherLiveData.postValue(data)
//                    dbRepo.addWeatherData(data)
                }

                override fun onFail(e: Exception) {
//                    weatherLiveData.postValue("Data")
                }
            })
    }

    fun saveDataBase(data: WeatherData) {
        dbRepo.addWeatherData(data)
        weatherLiveData.postValue(null)
    }
}