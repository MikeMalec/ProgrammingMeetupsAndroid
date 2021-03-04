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
import com.example.programmingmeetups.framework.utils.extensions.view.gone
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import com.example.programmingmeetups.framework.utils.permissions.PermissionManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MapFragment(var mapViewModel: MapViewModel? = null) : Fragment(R.layout.map_fragment),
    EasyPermissions.PermissionCallbacks, GoogleMap.OnMapClickListener,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Named(MARKER_GLIDE)
    @Inject
    lateinit var requestManager: RequestManager

    private val viewModel: MapViewModel
        get() = mapViewModel!!

    private lateinit var binding: MapFragmentBinding

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        setViewModel()
        checkPermissions()
        setBinding(view)
        setMap()
        setUserPositionButton()
    }

    private fun setViewModel() {
        mapViewModel =
            mapViewModel ?: ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
    }

    private fun checkPermissions() {
        permissionManager.requestLocationPermissions(this)
        requestPosition()
    }

    private fun requestPosition() {
        viewModel.requestPosition()
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
            map!!.setOnCameraIdleListener(this)
            observeEvents()
        }
    }

    private fun setUserPositionButton() {
        binding.ivUserPosition.setOnClickListener {
            if (viewModel.lastPosition != null) {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(viewModel.lastPosition, MAP_ZOOM)
                )
            }
        }
    }

    private var dispatchJob: Job? = null
    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.observe(viewLifecycleOwner, Observer {
                dispatchEvents(it)
            })
        }
    }

    private fun dispatchEvents(events: List<Pair<Boolean, ProgrammingEvent>>) {
        lifecycleScope.launch {
            dispatchJob?.join()
            dispatchJob = lifecycleScope.launch(IO) {
                events.forEach {
                    val toAdd = it.first
                    val event = it.second
                    when (toAdd) {
                        // new marker
                        true -> addMarker(event)
                        // to delete
                        false -> removeMarker(event)
                    }
                }
            }
        }
    }

    private val markers = mutableMapOf<Marker, ProgrammingEvent>()

    private suspend fun addMarker(event: ProgrammingEvent) {
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

    private suspend fun removeMarker(event: ProgrammingEvent) {
        val toRemove = markers.filterValues { it.id == event.id }.keys
        toRemove.forEach {
            withContext(Main) {
                markers.remove(it)
                it.remove()
            }
        }
    }

    private fun hideMap() {
        binding.mapView.hide()
        binding.mapView.gone()
    }

    private fun showMap() {
        lifecycleScope.launch {
            delay(200)
            binding.mapView.show()
        }
    }

    override fun onMapClick(latLng: LatLng?) {
        latLng?.run {
            hideMap()
            MapFragmentDirections.actionMapFragmentToCreateEventFragment(this).also {
                findNavController().navigate(it)
            }
        }
    }

    override fun onCameraIdle() {
        fetchEvents()
    }

    private fun fetchEvents() {
        if (canCalculate) {
            val positionAndRadius = calculateRadius()
            if (positionAndRadius != null) {
                mapViewModel?.fetchEvents(positionAndRadius.first, positionAndRadius.second)
            }
        }
    }

    private var canCalculate = false
    private fun calculateRadius(): Pair<LatLng, Double>? {
        if (map != null) {
            val position = map!!.cameraPosition.target
            val visibleRegion = map!!.projection.visibleRegion
            val distance = FloatArray(1)
            val farLeft = visibleRegion.farLeft
            val nearRight = visibleRegion.nearRight
            Location.distanceBetween(
                farLeft.latitude,
                farLeft.longitude,
                nearRight.latitude,
                nearRight.longitude,
                distance
            )
            val dst = distance[0].toDouble() / 2
            return Pair(position, dst)
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
        lifecycleScope.launchWhenStarted {
            viewModel.position.observe(viewLifecycleOwner, Observer { latLng ->
                if (!positionSet) {
                    viewModel.lastPosition = latLng
                    moveCameraToSpecificPosition(latLng)
                    positionSet = true
                    setUserPosition(latLng)
                    userPosition = latLng
                    firstCalculate()
                }
            })
        }
    }

    private fun firstCalculate() {
        lifecycleScope.launch {
            delay(1000)
            canCalculate = true
            fetchEvents()
        }
    }

    private var canSaveCameraPosition = true
    private fun moveCameraToSpecificPosition(position: LatLng) {
        showMap()
        canSaveCameraPosition = false
        if (viewModel.cameraPosition != null) {
            map?.animateCamera(
                CameraUpdateFactory.newCameraPosition(viewModel.cameraPosition),
                500,
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        canSaveCameraPosition = true
                        showUserPositionButton()
                    }

                    override fun onCancel() {}

                })
        } else {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM),
                500,
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        canSaveCameraPosition = true
                        showUserPositionButton()
                    }

                    override fun onCancel() {}
                })
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
        if (canSaveCameraPosition) {
            map?.also {
                viewModel.cameraPosition = it.cameraPosition
            }
        }
        canSaveCameraPosition = true
        viewModel.stop()
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

    private fun showUserPositionButton() {
        binding.ivUserPosition.show()
    }

    private fun hideUserPositionButton() {
        binding.ivUserPosition.hide()
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