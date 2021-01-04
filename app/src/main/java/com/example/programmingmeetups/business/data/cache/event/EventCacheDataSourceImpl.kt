package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.EventDaoService

class EventCacheDataSourceImpl(private val eventDaoService: EventDaoService) :
    EventCacheDataSource {
    override suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDaoService.saveProgrammingEvent(programmingEvent)
    }

    override suspend fun getEvents(happensAt:Long): List<ProgrammingEvent> {
        return eventDaoService.getEvents(happensAt)
    }

    override suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDaoService.deleteProgrammingEvent(programmingEvent)
    }

    override suspend fun updateEvent(programmingEvent: ProgrammingEvent) {
        eventDaoService.updateEvent(programmingEvent)
    }

    override suspend fun getAllEvents(): List<ProgrammingEvent> {
        return eventDaoService.getAllEvents()
    }
}