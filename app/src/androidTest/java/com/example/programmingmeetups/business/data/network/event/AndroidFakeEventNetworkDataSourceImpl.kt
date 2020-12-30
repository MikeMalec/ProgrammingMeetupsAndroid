package com.example.programmingmeetups.business.data.network.event

import android.util.Log
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.EventCommentResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.math.ceil

class AndroidFakeEventNetworkDataSourceImpl : EventNetworkDataSource {
    var events = mutableListOf<ProgrammingEvent>()

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

    override suspend fun updateEvent(
        token: String,
        id: String,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody,
        image: MultipartBody.Part?,
        icon: MultipartBody.Part?
    ): ProgrammingEvent {
        if (throwsException) throw Exception()
        val event = events.first { it.id == id }
        event.happensAt = 1L
        event.tags = mutableListOf("updated")
        event.description = "updated"
        if (image != null) {
            event.image = "new image"
        }
        if (icon != null) {
            event.icon = "new icon"
        }
        return event
    }

    override suspend fun deleteEvent(token: String, eventId: String): GenericResponse {
        if (throwsException) throw Exception()
        events = events.filter { it.id != eventId }.toMutableList()
        return GenericResponse()
    }

    override suspend fun fetchEvents(token: String): List<ProgrammingEvent> {
        if (throwsException) throw Exception()
        return events
    }

    override suspend fun joinEvent(eventId: String, token: String): ProgrammingEvent {
        if (throwsException) throw Exception()
        return ProgrammingEvent(
            id = eventId,
            organizer = User("1", "firstName", "lastName", "email", "desc", "image"),
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

    var comments = listOf<ProgrammingEventCommentDto>()

    override suspend fun getEventComments(
        token: String,
        eventId: String,
        page: Int
    ): EventCommentResponse {
        Log.d("XXX", "GET EFVENT COMMENTS $page")
        val index = (page - 1) * 10
        var counter = 0
        val eventComments = mutableListOf<ProgrammingEventCommentDto>()
        for (i in index until comments.size) {
            if (counter > 9) break
            eventComments.add(comments[i])
            counter++
        }
        val pages = ceil((comments.size.toDouble() / 10.0)).toInt()
        Log.d("XXX", "GET EFVENT COMMENTS $eventComments")
        return EventCommentResponse(pages = pages, comments = eventComments)
    }
}