package com.duman.weatherlogger.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
class WeatherData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lk") val lk: Int,

    @Embedded
    val main: TempData,
    val name: String,
    var utcTime: Long
)