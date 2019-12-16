package com.duman.weatherlogger.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.duman.weatherlogger.R
import com.duman.weatherlogger.data.viewmodel.WeatherViewModel
import com.duman.weatherlogger.databinding.FragmentListBinding
import com.duman.weatherlogger.view.adapters.WeatherItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : BottomSheetDialogFragment() {

    private var binding: FragmentListBinding? = null
    lateinit var mModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        mModel = ViewModelProviders.of(requireActivity()).get(WeatherViewModel::class.java)
    }

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

        val adapter = WeatherItemAdapter(mutableListOf(), mModel) {
            dismissAllowingStateLoss()
        }

        weather_list.adapter = adapter
        mModel.getWeatherList()?.observe(viewLifecycleOwner, Observer {
            adapter.update(it.reversed())
        })
    }
}
