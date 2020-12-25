package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.mappers.EventNetworkMapper
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class EventNetworkServiceImpl(
    private val eventApi: EventApi,
    val networkMapper: EventNetworkMapper
) : EventNetworkService {
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
        return networkMapper.mapFromEntity(
            eventApi.createEvent(
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
        )
    }

    override suspend fun fetchEvents(token: String): List<ProgrammingEvent> {
        return eventApi.fetchEvents(token).map { networkMapper.mapFromEntity(it) }
    }

    override suspend fun joinEvent(eventId: String, token: String): ProgrammingEvent {
        return networkMapper.mapFromEntity(eventApi.joinEvent(token,eventId ))
    }

    override suspend fun leaveEvent(eventId: String, token: String): ProgrammingEvent {
        return networkMapper.mapFromEntity(eventApi.leaveEvent(token,eventId))
    }
}