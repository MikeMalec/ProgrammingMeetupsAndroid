package com.example.programmingmeetups.framework.datasource.cache.event

import android.util.Log
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.cache.event.database.EventDao
import com.example.programmingmeetups.framework.datasource.cache.event.mappers.EventCacheMapper

class EventDaoServiceImpl(
    private val eventDao: EventDao,
    private val eventCacheMapper: EventCacheMapper
) : EventDaoService {
    override suspend fun updateEvent(programmingEvent: ProgrammingEvent) {
        val cachedEvent = eventDao.getEventById(programmingEvent.id!!)
        cachedEvent.programmingEvent = programmingEvent
        eventDao.updateProgrammingEvent(cachedEvent)
    }

    override suspend fun saveProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDao.insertProgrammingEvent(
            eventCacheMapper.mapToEntity(programmingEvent)
        )
    }

    override suspend fun deleteProgrammingEvent(programmingEvent: ProgrammingEvent) {
        eventDao.deleteProgrammingEvent(eventCacheMapper.mapToEntity(programmingEvent))
    }

    override suspend fun getEvents(): List<ProgrammingEvent> {
        return eventCacheMapper.mapFromEntities(eventDao.getProgrammingEvents())
    }

    override suspend fun getUserEvents(userId: String): List<ProgrammingEvent> {
        val events = eventCacheMapper.mapFromEntities(eventDao.getProgrammingEvents())
        return events.filter {
            it.participants!!.firstOrNull { user -> user.id == userId } != null
        }

    }
}