package com.duman.weatherlogger.data

import com.duman.weatherlogger.data.model.LatLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Exception

class WeatherRepository : WeatherDataSource {
    private val service = RetrofitFactory.makeWeatherService()
    override fun getTempService(
        dataCallback: WeatherDataSource.TempDataCallback?,
        latLong: LatLong
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherDataByLocation =
                    service.getWeatherDataByLocation(
                        lat = latLong.lat,
                        lon = latLong.long,
                        appId = "da7e3c9bc5982d3f299888c5d7bf46fe"
                    )
                if (weatherDataByLocation.code() == 200 && weatherDataByLocation.isSuccessful) {
                    weatherDataByLocation.body()?.let {
                        it.utcTime= Calendar.getInstance().timeInMillis
                        dataCallback?.onLoadData(it)
                        return@launch
                    }
                }

                dataCallback?.onFail(Exception(weatherDataByLocation.errorBody()?.string()))
            } catch (e: Exception) {
                e.printStackTrace()
                dataCallback?.onFail(e)
            }
        }
    }
}