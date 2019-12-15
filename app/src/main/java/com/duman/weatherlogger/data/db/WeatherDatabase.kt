package com.duman.weatherlogger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duman.weatherlogger.data.model.WeatherData

@Database(entities = [WeatherData::class],version = 2,exportSchema = false)
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
                            "deneme"
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}