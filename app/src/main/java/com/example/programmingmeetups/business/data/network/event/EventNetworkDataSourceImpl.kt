package com.example.programmingmeetups.business.data.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.EventNetworkService
import com.example.programmingmeetups.framework.datasource.network.event.model.*
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EventNetworkDataSourceImpl(private val eventNetworkService: EventNetworkService) :
    EventNetworkDataSource {
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
        return eventNetworkService.createEvent(
            token,
            image,
            icon,
            latitude,
            longitude,
            address,
            happensAt,
            tags,
            description
        )
    }

    override suspend fun fetchEvents(token: String): List<ProgrammingEvent> {
        return eventNetworkService.fetchEvents(token)
    }

    override suspend fun fetchEvents(
        token: String,
        position: LatLng,
        radius: Double
    ): List<ProgrammingEvent> {
        return eventNetworkService.fetchEvents(token, position, radius)
    }

    override suspend fun joinEvent(eventId: String, token: String): ProgrammingEvent {
        return eventNetworkService.joinEvent(eventId, token)
    }

    override suspend fun leaveEvent(eventId: String, token: String): ProgrammingEvent {
        return eventNetworkService.leaveEvent(eventId, token)
    }

    override suspend fun getEventComments(
        token: String,
        eventId: String,
        page: Int
    ): EventCommentResponse {
        return eventNetworkService.getEventComments(token, eventId, page)
    }

    override suspend fun deleteEvent(token: String, eventId: String): GenericResponse {
        return eventNetworkService.deleteEvent(token, eventId)
    }

    override suspend fun isParticipant(token: String, eventId: String): IsParticipantResponse {
        return eventNetworkService.isParticipant(token, eventId)
    }

    override suspend fun getEventUsers(token: String, eventId: String, page: Int): UsersResponse {
        return eventNetworkService.getEventUsers(token, eventId, page)
    }

    override suspend fun getAmountOfEventUsers(
        token: String,
        eventId: String
    ): UsersAmountResponse {
        return eventNetworkService.getAmountOfEventUsers(token, eventId)
    }

    override suspend fun getOwnEvents(token: String): List<ProgrammingEvent> {
        return eventNetworkService.getOwnEvents(token)
    }

    override suspend fun getUserEvents(token: String, userId: String, page: Int): UserEventsPaginationResponse {
        return eventNetworkService.getUserEvents(token,userId,page)
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
        return eventNetworkService.updateEvent(token, id, happensAt, tags, description, image, icon)
    }
}