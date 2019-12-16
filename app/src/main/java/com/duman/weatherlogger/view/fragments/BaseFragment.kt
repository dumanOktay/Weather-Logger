package com.duman.weatherlogger.view.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel

open class BaseFragment : Fragment() {
    internal lateinit var mModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModel = ViewModelProviders.of(requireActivity()).get(WeatherViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mModel.errorMessage?.observe(viewLifecycleOwner, Observer {

            AlertDialog.Builder(requireContext())
                .setTitle("Warning!")
                .setMessage(it)
                .setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.show()
        })
    }
}