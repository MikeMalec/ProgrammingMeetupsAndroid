package com.example.programmingmeetups.business.data.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeEventNetworkDataSource : EventNetworkDataSource {

    val events = mutableListOf<ProgrammingEvent>()
    var throwsException = false

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
        if (throwsException) throw Exception()
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
        if (throwsException) throw Exception()
        return events
    }

    override suspend fun joinEvent(eventId: String, token: String): ProgrammingEvent {
        if (throwsException) throw Exception()
        return ProgrammingEvent(
            id = eventId,
            participants = mutableListOf(
                User(
                    "id",
                    "firstName",
                    "lastName",
                    "email",
                    "description",
                    "image"
                )
            )
        )
    }

    override suspend fun leaveEvent(eventId: String, token: String): ProgrammingEvent {
        if (throwsException) throw Exception()
        return ProgrammingEvent(
            id = eventId,
            participants = mutableListOf()
        )
    }
}