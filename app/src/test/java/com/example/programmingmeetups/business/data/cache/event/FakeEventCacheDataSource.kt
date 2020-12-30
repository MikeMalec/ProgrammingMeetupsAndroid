package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.mappers.EventCacheMapper
import com.example.programmingmeetups.framework.datasource.network.event.mappers.EventNetworkMapper

class FakeEventCacheDataSource() :
    EventCacheDataSource {

    var events = mutableListOf<ProgrammingEvent>()

    override suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent) {
        events.add(programmingEvent)
    }

    override suspend fun getEvents(): List<ProgrammingEvent> {
        return events
    }

    override suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent) {
        val event = events.first { it.id == programmingEvent.id }
        events.remove(event)
    }

    override suspend fun getUserEvents(userId: String): List<ProgrammingEvent> {
        return events.filter {
            it.participants!!.firstOrNull { user -> user.id == userId } != null
        }
    }

    override suspend fun updateEvent(programmingEvent: ProgrammingEvent) {
        events = events.filter { it.id!! != programmingEvent.id!! }.toMutableList()
        events.add(programmingEvent)
    }
}