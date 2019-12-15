package com.duman.weatherlogger.data

import com.duman.weatherlogger.data.model.LatLong
import com.duman.weatherlogger.data.model.WeatherData

interface WeatherDataSource {

    interface TempDataCallback {
        fun onLoadData(data: WeatherData)

        fun onFail(e: Exception)
    }

    fun getTempService(dataCallback: TempDataCallback?, latLong: LatLong)
}