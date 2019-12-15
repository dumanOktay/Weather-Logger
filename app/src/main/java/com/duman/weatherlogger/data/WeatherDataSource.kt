package com.duman.weatherlogger.data

import com.duman.weatherlogger.data.model.WeatherData
import com.mapbox.mapboxsdk.geometry.LatLng

interface WeatherDataSource {

    interface TempDataCallback {
        fun onLoadData(data: WeatherData)

        fun onFail(e: Exception)
    }

    fun getTempService(dataCallback: TempDataCallback?, latLong: LatLng)
}