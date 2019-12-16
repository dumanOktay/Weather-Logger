package com.duman.weatherlogger.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duman.weatherlogger.R
import com.duman.weatherlogger.toCelcisus
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
        initMap(savedInstanceState)
        temp_tv.text = getCelsius()
        city_tv.text = mModel.selectedData?.name
        temp_max.text= mModel.selectedData?.main?.temp_max?.toCelcisus()
        temp_min.text= mModel.selectedData?.main?.temp_min?.toCelcisus()
    }

    private fun initMap(savedInstanceState: Bundle?) {
        map.onCreate(savedInstanceState)

        map.getMapAsync {
            it.setStyle(Style.OUTDOORS)
            val coord = mModel.selectedData?.coord

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

    private fun getCelsius() = "" + mModel.selectedData?.main?.temp?.toCelcisus()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        map.onStart()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}
