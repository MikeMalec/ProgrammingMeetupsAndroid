package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

interface EventCacheDataSource {
    suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent)
    suspend fun getEvents(): List<ProgrammingEvent>
    suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent)
    suspend fun getUserEvents(userId: String): List<ProgrammingEvent>
    suspend fun updateEvent(programmingEvent: ProgrammingEvent)
}