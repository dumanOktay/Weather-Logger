package com.duman.weatherlogger.view.fragments


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.checkLocationPermission
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.duman.weatherlogger.databinding.FragmentListBinding
import com.duman.weatherlogger.view.adapters.WeatherItemAdapter
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : BaseFragment() {

    private var binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            model = mModel
        }
        binding?.lifecycleOwner = this

        if (activity?.checkLocationPermission() != true) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            mModel.fetchLastLocation(requireContext())
        }

        mModel.lastLoc.observe(viewLifecycleOwner, Observer {
            mModel.getWeatherData()
        })


        val adapter = WeatherItemAdapter(mutableListOf(), mModel, false)
        val liveAdapter = WeatherItemAdapter(mutableListOf(), mModel, true)

        weather_list_current.adapter = liveAdapter
        weather_list.adapter = adapter
        mModel.getWeatherList()?.observe(viewLifecycleOwner, Observer {
            adapter.update(it.reversed())
        })

        mModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                liveAdapter.update(listOf())
            } else {
                liveAdapter.update(listOf(it))
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION && permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mModel.fetchLastLocation(requireContext())
        }

    }

    companion object {
        private const val REQUEST_LOCATION = 108
    }


}
