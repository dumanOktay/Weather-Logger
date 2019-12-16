package com.duman.weatherlogger.data.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

@Entity(tableName = "weather_table")
class WeatherData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lk") val lk: Int,

    @Embedded
    val main: TempData,


    var iconUrl: String,

    val name: String,
    var utcTime: Long,

    val weather: List<Weather>? = null,

    @Embedded
    val coord: Coord

)

@Entity
data class Weather(val icon: String, val main: String)

data class Coord(var lat: Double, var lon: Double)
object WeatherConverter {
    private var gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toList(str: String?): List<Weather> {
        return if (str.isNullOrEmpty()) {
            emptyList()
        } else {
            val listType = object : TypeToken<List<Weather>>() {}.type
            gson.fromJson(str, listType)
        }
    }

    @TypeConverter
    @JvmStatic
    fun listToObject(list: List<Weather>): String {
        return gson.toJson(list)
    }

}
