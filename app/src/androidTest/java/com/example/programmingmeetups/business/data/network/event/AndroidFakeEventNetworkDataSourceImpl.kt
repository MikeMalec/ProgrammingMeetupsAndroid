package com.example.programmingmeetups.business.data.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AndroidFakeEventNetworkDataSourceImpl : EventNetworkDataSource {
    val events = mutableListOf<ProgrammingEvent>()

    var createEventThrowsException = false
    var fetchEventsThrowsException = false

    override suspend fun createEvent(
        token: String,
        image: MultipartBody.Part,
        icon: MultipartBody.Part,
        latitude: RequestBody,
        longitude: RequestBody,
        address: RequestBody,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody
    ): ProgrammingEvent {
        if (createEventThrowsException) throw Exception()
        val programmingEvent = ProgrammingEvent(
            id = token,
            image = "image",
            icon = "icon",
            latitude = 1.1,
            longitude = 1.1,
            address = "test",
            happensAt = 1,
            tags = mutableListOf("test1", "test2"),
            description = "test"
        )
        return programmingEvent
    }

    override suspend fun fetchEvents(token: String): List<ProgrammingEvent> {
        if (fetchEventsThrowsException) throw Exception()
        return events
    }
}