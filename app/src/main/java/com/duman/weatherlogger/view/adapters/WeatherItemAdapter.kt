package com.duman.weatherlogger.view.adapters

import android.app.Activity
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duman.weatherlogger.R
import com.duman.weatherlogger.data.model.WeatherData
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.duman.weatherlogger.navigateDestination
import kotlinx.android.synthetic.main.item_weather.view.*
import java.util.*

class WeatherItemAdapter(
    private val weatherList: MutableList<WeatherData>,
    val viewModel: WeatherViewModel,
    val fromDatabase: Boolean = false
) :
    RecyclerView.Adapter<WeatherItemAdapter.WeatherHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        return WeatherHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_weather,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun update(list: List<WeatherData>) {
        weatherList.clear()
        weatherList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(weatherList[position])
    }


    inner class WeatherHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(data: WeatherData) = with(itemView) {
            temp_tv.text = ""+data.main.temp+" \u2103"
            city_tv.text = data.name
            val date = Date(data.utcTime)
            val dateFormat: java.text.DateFormat? = DateFormat.getLongDateFormat(context)
            val timeFormat: java.text.DateFormat? = DateFormat.getTimeFormat(context)

            date_tv.text = dateFormat?.format(date) + " : " + timeFormat?.format(date)
            save_btn.text = "Save"
            save_btn.visibility = if (fromDatabase) View.VISIBLE else View.GONE
            save_btn.setOnClickListener {
                viewModel.saveDataBase(data)
                save_btn.text = "Refresh"
                save_btn.setOnClickListener {
                    viewModel.getWeatherData()
                }
            }

            setOnClickListener {
                viewModel.selectedData = data
                val activity = context as Activity
                activity.navigateDestination(R.id.mapFragment)
            }
            val icon = data.iconUrl
            Glide.with(img).load("http://openweathermap.org/img/w/$icon.png").into(img)

        }
    }
}