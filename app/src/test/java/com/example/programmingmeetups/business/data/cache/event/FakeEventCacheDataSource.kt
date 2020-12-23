package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.mappers.EventCacheMapper
import com.example.programmingmeetups.framework.datasource.network.event.mappers.EventNetworkMapper

class FakeEventCacheDataSource() :
    EventCacheDataSource {

    val events = mutableListOf<ProgrammingEvent>()

    override suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent) {
        events.add(programmingEvent)
    }

    override suspend fun getEvents(): List<ProgrammingEvent> {
        return events
    }

    override suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent) {
        events.remove(programmingEvent)
    }

    override suspend fun getUserEvents(userId: String): List<ProgrammingEvent> {
        return events.filter {
            it.participants!!.firstOrNull { user -> user.id == userId } != null
        }
    }
}