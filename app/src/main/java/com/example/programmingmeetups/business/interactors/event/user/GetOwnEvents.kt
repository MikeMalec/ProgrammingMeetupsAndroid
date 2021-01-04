package com.example.programmingmeetups.business.interactors.event.user

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

class GetOwnEvents(val eventCacheDataSource: EventCacheDataSource) {
    suspend fun getOwnEvents(happensAt: Long): List<ProgrammingEvent> {
        return eventCacheDataSource.getEvents(happensAt)
    }
}