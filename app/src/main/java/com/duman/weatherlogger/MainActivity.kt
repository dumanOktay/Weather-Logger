package com.duman.weatherlogger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 108

    lateinit var model: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Mapbox.getInstance(this, getString(R.string.access_token))

        model = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.adjust_height -> {
            }
            else -> {
                goToPickerActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Set up the PlacePickerOptions and startActivityForResult
     */
    private fun goToPickerActivity() {
        startActivityForResult(
            PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.access_token))
                .placeOptions(
                    PlacePickerOptions.builder()
                        .statingCameraPosition(
                            CameraPosition.Builder()
                                .target(model.lastLoc.value).zoom(16.0).build()
                        )
                        .build()
                )
                .build(this), REQUEST_CODE
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) { // Show the button and set the OnClickListener()
            /* val goToPickerActivityButton =
                 findViewById<Button>(R.id.go_to_picker_button)
             goToPickerActivityButton.visibility = View.VISIBLE
             goToPickerActivityButton.setOnClickListener { goToPickerActivity() }*/
        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) { // Retrieve the information from the selected location's CarmenFeature
            val carmenFeature = PlacePicker.getPlace(data)
            // Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {
                val point = carmenFeature.center()
                println("ççç " + point?.latitude())

                model.updateLocation(LatLng(point?.latitude() ?: 0.0, point?.longitude() ?: 0.0))
            }
        }
    }
}
