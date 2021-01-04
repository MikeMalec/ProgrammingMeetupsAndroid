package com.example.programmingmeetups.framework.datasource.cache.event

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

    override suspend fun getEvents(happensAt: Long): List<ProgrammingEvent> {
        return eventCacheMapper.mapFromEntities(eventDao.getEvents(happensAt))
    }

    override suspend fun getAllEvents(): List<ProgrammingEvent> {
        return eventCacheMapper.mapFromEntities(eventDao.getAllEvents())
    }
}