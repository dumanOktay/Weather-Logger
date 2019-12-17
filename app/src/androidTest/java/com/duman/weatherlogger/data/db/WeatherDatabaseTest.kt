package com.duman.weatherlogger.data.db

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duman.weatherlogger.data.model.Coord
import com.duman.weatherlogger.data.model.TempData
import com.duman.weatherlogger.data.model.WeatherData
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {
    lateinit var weatherDao: WeatherDao
    lateinit var db: WeatherDatabase
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java).build()

        weatherDao = db.getWeatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun checkData() {
        val weatherData = WeatherData(
            5, TempData(8.8f, 1.8f, 1.7f), "deneme",
            "Riga",
            500L,
            emptyList(),
            Coord(0.0, 5.0)
        )

        weatherDao.insertWeatherData(weatherData)

        val d = weatherDao.getWeatherList().observeForever(Observer {
            it
        })
        Thread.sleep(500)
        assertEquals("Riga", d)
    }
}