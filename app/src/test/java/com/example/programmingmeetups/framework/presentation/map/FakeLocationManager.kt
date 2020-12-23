package com.example.programmingmeetups.framework.presentation.map

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

class FakeLocationManager : LocationManagerInterface {
    override var position: LiveData<LatLng> = object : LiveData<LatLng>() {

    }

    override fun getLocation() {}
}