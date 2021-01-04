package com.example.programmingmeetups.business.data.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

interface EventCacheDataSource {
    suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent)
    suspend fun getEvents(happensAt: Long): List<ProgrammingEvent>
    suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent)
    suspend fun updateEvent(programmingEvent: ProgrammingEvent)
    suspend fun getAllEvents(): List<ProgrammingEvent>
}