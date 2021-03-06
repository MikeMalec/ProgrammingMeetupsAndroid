package com.example.programmingmeetups.framework.datasource.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

interface EventDaoService {
    suspend fun updateEvent(programmingEvent: ProgrammingEvent)

    suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent)

    suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent)

    suspend fun getEvents(): List<ProgrammingEvent>

    suspend fun getUserEvents(userId: String): List<ProgrammingEvent>
}