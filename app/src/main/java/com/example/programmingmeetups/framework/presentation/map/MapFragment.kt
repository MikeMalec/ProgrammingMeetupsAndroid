package com.example.programmingmeetups.framework.presentation.map

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.databinding.MapFragmentBinding
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import com.example.programmingmeetups.utils.IMAGES_URL
import com.example.programmingmeetups.utils.MAP_ZOOM
import com.example.programmingmeetups.utils.MARKER_GLIDE
import com.example.programmingmeetups.utils.extensions.view.hide
import com.example.programmingmeetups.utils.extensions.view.show
import com.example.programmingmeetups.utils.permissions.PermissionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MapFragment(var mapViewModel: MapViewModel? = null) : Fragment(R.layout.map_fragment),
    EasyPermissions.PermissionCallbacks, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Named(MARKER_GLIDE)
    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var binding: MapFragmentBinding

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        setViewModel()
        setBinding(view)
        setMap()
    }

    private fun setViewModel() {
        mapViewModel =
            mapViewModel ?: ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        permissionManager.requestLocationPermissions(this)
        requestPosition()
    }

    private fun requestPosition() {
        mapViewModel!!.requestPosition()
    }

    private fun setBinding(view: View) {
        binding = MapFragmentBinding.bind(view)
    }

    private fun setMap() {
        positionSet = false
        binding.mapView.getMapAsync {
            map = it
            setPosition()
            map!!.setOnMapClickListener(this)
            map!!.setOnMarkerClickListener(this)
            observeEvents()
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            mapViewModel!!.events.observe(viewLifecycleOwner, Observer {
                showMarkers(it)
            })
            mapViewModel!!.fetchEvents()
        }
    }

    private val markers = mutableMapOf<Marker, ProgrammingEvent>()

    private fun showMarkers(events: List<ProgrammingEvent>) {
        map?.clear()
        lifecycleScope.launch(IO) {
            events.forEach { event ->
                val position = LatLng(event.latitude!!, event.longitude!!)
                val marker = MarkerOptions().position(position)
                try {
                    val icon: Bitmap = requestManager.asBitmap().load("$IMAGES_URL${event.icon}")
                        .into(100, 100).get()
                    marker.icon(BitmapDescriptorFactory.fromBitmap(icon))
                } catch (e: Exception) {

                }
                withContext(Main) {
                    map?.addMarker(marker)?.also { marker ->
                        markers[marker] = event
                    }
                }
            }
        }
    }

    private fun hideMap() {
        binding.mapView.hide()
    }

    private fun showMap() {
        binding.mapView.show()
    }

    override fun onMapClick(latLng: LatLng?) {
        latLng?.run {
            hideMap()
            MapFragmentDirections.actionMapFragmentToCreateEventFragment(this).also {
                findNavController().navigate(it)
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        markers[marker]?.also { showEvent(it) }
        return false
    }

    private fun showEvent(event: ProgrammingEvent) {
        MapFragmentDirections.actionMapFragmentToEventFragment(event).run {
            findNavController().navigate(this)
        }
    }

    private var positionSet = false
    private fun setPosition() {
        mapViewModel!!.position.observe(viewLifecycleOwner, Observer { latLng ->
            if (!positionSet) {
                positionSet = true
                moveCameraToSpecificPosition(latLng)
            }
        })
    }

    private fun moveCameraToSpecificPosition(position: LatLng) {
        showMap()
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM))
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
        mapView?.run {
            onSaveInstanceState(outState)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        requestPosition()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            permissionManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}