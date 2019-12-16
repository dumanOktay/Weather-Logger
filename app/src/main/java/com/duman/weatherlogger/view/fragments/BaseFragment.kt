package com.duman.weatherlogger.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel

open class BaseFragment : Fragment() {
    internal lateinit var mModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModel = ViewModelProviders.of(requireActivity()).get(WeatherViewModel::class.java)
    }
}