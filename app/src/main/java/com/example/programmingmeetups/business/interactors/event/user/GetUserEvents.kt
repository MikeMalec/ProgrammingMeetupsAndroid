package com.example.programmingmeetups.business.interactors.event.user

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

class GetUserEvents(val eventCacheDataSource: EventCacheDataSource) {
    suspend fun getUserEvents(happensAt: Long): List<ProgrammingEvent> {
        return eventCacheDataSource.getEvents(happensAt)
    }
}