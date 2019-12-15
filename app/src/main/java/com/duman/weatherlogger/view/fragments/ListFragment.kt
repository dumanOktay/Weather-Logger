package com.duman.weatherlogger.view.fragments


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.checkLocationPermisson
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.duman.weatherlogger.databinding.FragmentListBinding
import com.duman.weatherlogger.view.adapters.WeatherItemAdapter
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null

    private lateinit var mModel: WeatherViewModel

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

        mModel = ViewModelProviders.of(requireActivity()).get(WeatherViewModel::class.java)

        binding?.apply {
            model = mModel
        }
        binding?.lifecycleOwner = this

        if (activity?.checkLocationPermisson() != true) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                108
            )
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


}
