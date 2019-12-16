package com.duman.weatherlogger.view.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.duman.weatherlogger.R
import com.duman.weatherlogger.checkLocationPermission
import com.duman.weatherlogger.data.model.Coord
import com.duman.weatherlogger.data.model.WeatherData
import com.duman.weatherlogger.toCelcisus
import com.duman.weatherlogger.view.adapters.WeatherItemAdapter
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_map.*


/**
 * A simple [Fragment] subclass.
 */
class MapFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), getString(R.string.access_token))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map.onCreate(savedInstanceState)
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

        val adapter = WeatherItemAdapter(mutableListOf(), mModel) { }
        mModel.getWeatherList()?.observe(viewLifecycleOwner, Observer {
            adapter.update(it.reversed())
        })

        tv_history.setOnClickListener {
            ListFragment().show(childFragmentManager, "s")
        }
        tv_save.setOnClickListener {
            mModel.weatherLiveData.value?.let { it1 -> mModel.saveDataBase(it1) }
        }

    }


    private fun initView(it: WeatherData?) {
        initMap(it?.coord)

        temp_tv.text = getCelsius()
        city_tv.text = it?.name
        temp_max.text = it?.main?.temp_max?.toCelcisus()
        temp_min.text = it?.main?.temp_min?.toCelcisus()
        mModel.selectedData = null
    }

    private fun initMap(coord: Coord?) {


        map.getMapAsync {
            it.setStyle(Style.OUTDOORS)


            val latLng = LatLng(coord?.lat ?: 0.0, coord?.lon ?: 0.0)
            val position = CameraPosition.Builder()
                .target(latLng) // Sets the new camera position
                .zoom(10.0) // Sets the zoom
                .bearing(0.0) // Rotate the camera
                .tilt(30.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder


            it.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position), 7000
            )

            it.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getCelsius())
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION && permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mModel.fetchLastLocation(requireContext())
        } else {
            mModel.setErrorMessage("The app need your location info.")
        }

    }

    private fun getCelsius() = "" + mModel.selectedData?.main?.temp?.toCelcisus()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map?.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        if (mModel.selectedData == null) {
            mModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
                mModel.selectedData = it
                initView(it)

            })
        } else {
            initView(mModel.selectedData)
        }

    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    companion object {
        private const val REQUEST_LOCATION = 108
    }

}
