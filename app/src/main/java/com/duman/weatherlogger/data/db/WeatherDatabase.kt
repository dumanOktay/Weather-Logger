package com.duman.weatherlogger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.duman.weatherlogger.data.model.WeatherConverter
import com.duman.weatherlogger.data.model.WeatherData

@Database(entities = [WeatherData::class],version = 2,exportSchema = false)
@TypeConverters(WeatherConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {
        @Volatile
        var instance: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase? {
            if (instance == null) {
                synchronized(WeatherDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext, WeatherDatabase::class.java,
                            "weather_db"
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}