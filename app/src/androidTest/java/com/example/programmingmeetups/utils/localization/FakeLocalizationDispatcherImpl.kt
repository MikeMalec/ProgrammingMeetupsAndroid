package com.example.programmingmeetups.utils.localization

import org.junit.Assert.*

class FakeLocalizationDispatcherImpl : LocalizationDispatcherInterface {
    override fun getAddress(latitude: Double, longitude: Double): String? {
        return "address"
    }
}