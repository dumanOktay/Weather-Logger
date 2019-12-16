package com.duman.weatherlogger.view.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duman.weatherlogger.R
import com.duman.weatherlogger.data.model.WeatherData
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.duman.weatherlogger.toCelcisus
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.android.synthetic.main.item_weather.view.*
import java.util.*

class WeatherItemAdapter(
    private val weatherList: MutableList<WeatherData>,
    val viewModel: WeatherViewModel,
    val func: () -> Unit
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

    fun addData(data: WeatherData){
        if (weatherList.contains(data).not()){
            weatherList.add(0,data)
            notifyItemInserted(0)

        }
    }


    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(weatherList[position])
    }


    inner class WeatherHolder(v: View) : RecyclerView.ViewHolder(v) {

        var data: WeatherData? = null
        fun bind(data: WeatherData) = with(itemView) {
            this@WeatherHolder.data = data
            temp_tv.text = "" + data.main.temp.toCelcisus()
            city_tv.text = data.name
            val date = Date(data.utcTime)
            val dateFormat: java.text.DateFormat? = DateFormat.getLongDateFormat(context)
            val timeFormat: java.text.DateFormat? = DateFormat.getTimeFormat(context)

            date_tv.text = dateFormat?.format(date) + " : " + timeFormat?.format(date)

            setOnClickListener {
                viewModel.updateLocation(LatLng(data.coord.lat, data.coord.lon))
                func.invoke()

            }
            val icon = data.iconUrl
            Glide.with(img).load("http://openweathermap.org/img/w/$icon.png").into(img)

        }
    }
}