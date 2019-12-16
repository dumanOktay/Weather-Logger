package com.duman.weatherlogger.data

import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Exception
import kotlin.math.roundToInt

class WeatherRepository : WeatherDataSource {
    private val service = RetrofitFactory.makeWeatherService()
    override fun getTempService(
        dataCallback: WeatherDataSource.TempDataCallback?,
        latLong: LatLng
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherDataByLocation =
                    service.getWeatherDataByLocation(
                        lat = latLong.latitude,
                        lon = latLong.longitude,
                        appId = "da7e3c9bc5982d3f299888c5d7bf46fe"
                    )
                if (weatherDataByLocation.code() == 200 && weatherDataByLocation.isSuccessful) {
                    weatherDataByLocation.body()?.let {
                        it.iconUrl = it.weather?.get(0)?.icon ?: "03d"
                        it.coord.lat = latLong.latitude

                        it.coord.lon = latLong.longitude
                        it.utcTime = Calendar.getInstance().timeInMillis
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