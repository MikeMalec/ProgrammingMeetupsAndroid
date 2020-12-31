package com.example.programmingmeetups.framework.utils.localization

import android.location.Geocoder
import java.lang.Exception

class LocalizationDispatcher(val geocoder: Geocoder) :
    LocalizationDispatcherInterface {
    override fun getAddress(latitude: Double, longitude: Double): String? {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val tmpAddress = mutableListOf<String>()
                addresses[0].run {
                    thoroughfare?.also { tmpAddress.add(it) }
                    featureName?.also { tmpAddress.add(it) }
                    locality?.also { tmpAddress.add(it) }
                }
                tmpAddress.joinToString(" ")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

interface LocalizationDispatcherInterface {
    fun getAddress(latitude: Double, longitude: Double): String?
}