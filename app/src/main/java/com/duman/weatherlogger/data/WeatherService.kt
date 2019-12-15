package com.duman.weatherlogger.data

import com.duman.weatherlogger.data.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather")
    suspend fun getWeatherDataByLocation(
        @Query("lat") lat: Double, @Query("lon") lon: Double, @Query(
            "appid"
        ) appId: String
    ): Response<WeatherData>

}