package com.example.programmingmeetups.framework.presentation.map

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.databinding.MapFragmentBinding
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import com.example.programmingmeetups.framework.utils.MAP_ZOOM
import com.example.programmingmeetups.framework.utils.MARKER_GLIDE
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import com.example.programmingmeetups.framework.utils.permissions.PermissionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
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
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener,
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
            map!!.setOnCameraMoveListener(this)
            map!!.setOnCameraIdleListener(this)
            observeEvents()
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            mapViewModel!!.events.observe(viewLifecycleOwner, Observer {
                showMarkers(it)
            })
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
            withContext(Main) {
                userPosition?.also { setUserPosition(it) }
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

    override fun onCameraMove() {
        fetchEvents()
    }

    override fun onCameraIdle() {
        fetchEvents()
    }

    private fun fetchEvents() {
        val positionAndRadius = calculateRadius()
        if (positionAndRadius != null) {
            mapViewModel?.fetchEvents(positionAndRadius.first, positionAndRadius.second)
        }
    }

    private fun calculateRadius(): Pair<LatLng, Double>? {
        if (map != null) {
            val position = map!!.cameraPosition.target
            val visibleRegion = map!!.projection.visibleRegion
            val distanceWidth = FloatArray(1)
            val distanceHeight = FloatArray(1)
            val farRight = visibleRegion.farRight
            val farLeft = visibleRegion.farLeft
            val nearLeft = visibleRegion.nearLeft
            Location.distanceBetween(
                farLeft.latitude,
                farLeft.longitude,
                nearLeft.latitude,
                nearLeft.longitude,
                distanceHeight
            )
            Location.distanceBetween(
                farLeft.latitude,
                farLeft.longitude,
                farRight.latitude,
                farRight.longitude,
                distanceHeight
            )
            val radius = Math.sqrt(
                (Math.pow(
                    distanceHeight[0].toDouble(),
                    2.0
                )) + Math.pow(distanceWidth[0].toDouble(), 2.0)
            ) / 2
            return Pair(position, radius)
        }
        return null
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        markers[marker]?.also { showEvent(it) }
        return false
    }

    private fun showEvent(event: ProgrammingEvent) {
        hideMap()
        MapFragmentDirections.actionMapFragmentToEventFragment(event).run {
            findNavController().navigate(this)
        }
    }

    private var userPosition: LatLng? = null
    private var positionSet = false
    private fun setPosition() {
        mapViewModel!!.position.observe(viewLifecycleOwner, Observer { latLng ->
            if (!positionSet) {
                positionSet = true
                moveCameraToSpecificPosition(latLng)
                userPosition = latLng
            }
        })
    }

    private fun moveCameraToSpecificPosition(position: LatLng) {
        showMap()
        if (mapViewModel!!.cameraPosition != null) {
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(mapViewModel?.cameraPosition))
        } else {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM))
        }
    }

    private fun setUserPosition(latLng: LatLng) {
        val markerOptions = MarkerOptions().title("You are here!").position(latLng)
        val marker = map?.addMarker(markerOptions)
        marker?.showInfoWindow()
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
        map?.also {
            mapViewModel?.cameraPosition = it.cameraPosition
        }
        mapViewModel?.stop()
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