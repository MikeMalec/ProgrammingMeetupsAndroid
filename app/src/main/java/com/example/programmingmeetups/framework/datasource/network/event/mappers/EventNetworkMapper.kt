package com.example.programmingmeetups.framework.datasource.network.event.mappers

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventNetworkMapper @Inject constructor() {
    fun mapFromEntity(programmingEventDto: ProgrammingEventDto): ProgrammingEvent {
        return ProgrammingEvent(
            id = programmingEventDto.id,
            tags = programmingEventDto.tags,
            image = programmingEventDto.image,
            icon = programmingEventDto.icon,
            organizer = programmingEventDto.organizer,
            latitude = programmingEventDto.latitude,
            longitude = programmingEventDto.longitude,
            address = programmingEventDto.address,
            happensAt = programmingEventDto.happensAt,
            description = programmingEventDto.description,
            participants = programmingEventDto.participants,
            createdAt = programmingEventDto.createdAt
        )
    }

    fun mapToEntity(event: ProgrammingEvent): ProgrammingEventDto {
        return ProgrammingEventDto(
            id = event.id!!,
            tags = event.tags!!,
            image = event.image!!,
            icon = event.icon!!,
            organizer = event.organizer!!,
            latitude = event.latitude!!,
            longitude = event.longitude!!,
            address = event.address!!,
            happensAt = event.happensAt!!,
            description = event.description!!,
            participants = event.participants!!,
            createdAt = event.createdAt!!
        )
    }
}