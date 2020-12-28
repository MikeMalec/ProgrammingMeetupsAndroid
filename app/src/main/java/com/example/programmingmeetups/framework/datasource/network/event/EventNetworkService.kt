package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.EventCommentResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface EventNetworkService {
    suspend fun createEvent(
        token: String,
        image: MultipartBody.Part,
        icon: MultipartBody.Part,
        latitude: RequestBody,
        longitude: RequestBody,
        address: RequestBody,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody
    ): ProgrammingEvent

    suspend fun fetchEvents(token: String): List<ProgrammingEvent>

    suspend fun joinEvent(eventId: String, token: String): ProgrammingEvent
    suspend fun leaveEvent(eventId: String, token: String): ProgrammingEvent
    suspend fun getEventComments(token: String, eventId: String, page: Int): EventCommentResponse
}