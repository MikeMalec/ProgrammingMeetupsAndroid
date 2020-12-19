package com.example.programmingmeetups.framework.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.MapFragmentBinding
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.map_fragment.*

class MapFragment : Fragment(R.layout.map_fragment) {

    private lateinit var binding: MapFragmentBinding

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding(view)
        mapView.onCreate(savedInstanceState)
        setMap()
    }

    private fun setBinding(view: View) {
        binding = MapFragmentBinding.bind(view)
    }

    private fun setMap() {
        binding.mapView.getMapAsync {
            map = it
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}