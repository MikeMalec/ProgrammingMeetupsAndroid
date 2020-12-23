package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.EventDaoService
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto

class EventCacheDataSourceImpl(private val eventDaoService: EventDaoService) :
    EventCacheDataSource {
    override suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDaoService.saveProgrammingEvent(programmingEvent)
    }

    override suspend fun getEvents(): List<ProgrammingEvent> {
        return eventDaoService.getEvents()
    }

    override suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDaoService.deleteProgrammingEvent(programmingEvent)
    }

    override suspend fun getUserEvents(userId: String): List<ProgrammingEvent> {
        return eventDaoService.getUserEvents(userId)
    }
}