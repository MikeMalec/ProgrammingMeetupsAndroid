package com.example.programmingmeetups.framework.utils.localization

class FakeLocalizationDispatcherImpl : LocalizationDispatcherInterface {
    override fun getAddress(latitude: Double, longitude: Double): String? {
        return "address"
    }
}