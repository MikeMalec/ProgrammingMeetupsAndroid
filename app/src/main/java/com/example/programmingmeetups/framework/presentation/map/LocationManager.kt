package com.example.programmingmeetups.framework.presentation.map

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.programmingmeetups.utils.FASTEST_LOCATION_INTERVAL
import com.example.programmingmeetups.utils.LOCATION_UPDATE_INTERVAL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

class LocationManager constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationManagerInterface {
    private var _position: MutableLiveData<LatLng> = MutableLiveData()
    override var position: LiveData<LatLng> = _position

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                locations.forEach {
                    _position.postValue(LatLng(it.latitude, it.longitude))
                }
            }
        }
    }

    override fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun getLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    override fun getLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
            getLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

interface LocationManagerInterface {
    fun getLocation()
    fun stop()
    var position: LiveData<LatLng>
}