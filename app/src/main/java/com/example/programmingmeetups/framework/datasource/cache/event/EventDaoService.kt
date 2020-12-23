package com.example.programmingmeetups.framework.datasource.cache.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent

interface EventDaoService {
    suspend fun saveProgrammingEvent(programmingEventDto: ProgrammingEvent)

    suspend fun deleteProgrammingEvent(programmingEventDto: ProgrammingEvent)

    suspend fun getEvents(): List<ProgrammingEvent>

    suspend fun getUserEvents(userId: String): List<ProgrammingEvent>
}