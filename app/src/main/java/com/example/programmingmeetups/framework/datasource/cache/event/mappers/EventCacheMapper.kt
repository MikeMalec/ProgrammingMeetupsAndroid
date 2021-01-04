package com.example.programmingmeetups.framework.datasource.cache.event.mappers

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.model.ProgrammingEventCacheEntity
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventCacheMapper @Inject constructor() {

    fun mapToEntity(programmingEvent: ProgrammingEvent): ProgrammingEventCacheEntity {
        return ProgrammingEventCacheEntity(
            programmingEvent.id!!,
            programmingEvent.happensAt!!,
            programmingEvent
        )
    }

    fun mapFromEntity(eventCacheEntity: ProgrammingEventCacheEntity): ProgrammingEvent {
        return eventCacheEntity.programmingEvent
    }

    fun mapFromEntities(eventCacheEntities: List<ProgrammingEventCacheEntity>): List<ProgrammingEvent> {
        return eventCacheEntities.map { it.programmingEvent }
    }
}