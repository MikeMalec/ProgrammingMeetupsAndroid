package com.example.programmingmeetups.framework.datasource.network.event.utils

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import javax.inject.Inject

class EventValidator @Inject constructor() {
    fun validateEvent(
        event: ProgrammingEvent,
        successCallback: () -> Unit,
        errorCallback: () -> Unit
    ) {
        if (event.latitude != null && event.longitude != null && event.address != null && event.happensAt != null && event.image != null && event.icon != null && event.description != null) {
            successCallback()
        } else {
            errorCallback()
        }
    }
}