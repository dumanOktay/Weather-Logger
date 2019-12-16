package com.duman.weatherlogger.data.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duman.weatherlogger.data.WeatherDataSource
import com.duman.weatherlogger.data.WeatherRepository
import com.duman.weatherlogger.data.db.WeatherDbRepo
import com.duman.weatherlogger.data.model.WeatherData
import com.google.android.gms.location.LocationServices
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.launch

class WeatherViewModel(app: Application) : AndroidViewModel(app) {
    private val dataSource: WeatherDataSource = WeatherRepository()
    private val dbRepo = WeatherDbRepo(app)

    var lastLoc = MutableLiveData<LatLng>()

    var selectedData: WeatherData? = null

    val weatherLiveData = MutableLiveData<WeatherData?>()



    fun getWeatherList() = dbRepo.getWeatherList()
    fun getWeatherData() {
        dataSource.getTempService(
            latLong = lastLoc.value ?: LatLng(0.0, 0.0),
            dataCallback = object : WeatherDataSource.TempDataCallback {
                override fun onLoadData(data: WeatherData) {
                    weatherLiveData.postValue(data)
//                    dbRepo.addWeatherData(data)
                }

                override fun onFail(e: Exception) {
                    e.printStackTrace()
//                    weatherLiveData.postValue("Data")
                }
            })
    }

    fun saveDataBase(data: WeatherData) {
        dbRepo.addWeatherData(data)
    }

    fun updateLocation(loc: LatLng) {
        lastLoc.postValue(loc)
    }

    fun fetchLastLocation(context: Context) {
        val client = LocationServices.getFusedLocationProviderClient(context)
        client.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                lastLoc.postValue(LatLng(location.latitude, location.longitude))
            }
            viewModelScope.launch {
                getWeatherData()
            }
        }

    }


}