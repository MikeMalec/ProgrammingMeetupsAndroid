package com.example.programmingmeetups.business.interactors.event.user

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

class GetUserEvents(val eventCacheDataSource: EventCacheDataSource) {
    suspend fun getUserEvents(userId: String): List<ProgrammingEvent> {
        return eventCacheDataSource.getUserEvents(userId)
    }
}