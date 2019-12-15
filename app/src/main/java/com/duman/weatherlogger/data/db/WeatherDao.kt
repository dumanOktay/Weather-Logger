package com.duman.weatherlogger.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duman.weatherlogger.data.model.WeatherData

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherData)

    @Query("select * from weather_table")
    fun getWeatherList(): LiveData<List<WeatherData>>
}