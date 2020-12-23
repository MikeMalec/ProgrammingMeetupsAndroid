package com.example.programmingmeetups.business.interactors.event.map

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SynchronizeProgrammingEvents(
    private val eventCacheDataSource: EventCacheDataSource,
    private val eventNetworkDataSource: EventNetworkDataSource
) {
    fun synchronizeEvents(
        token: String,
        dispatcher: CoroutineDispatcher
    ): Flow<List<ProgrammingEvent>> = flow {
        val cachedEvents = eventCacheDataSource.getEvents()
        emit(cachedEvents)
        val eventsFromApi = safeApiCall(dispatcher) { eventNetworkDataSource.fetchEvents(token) }
        if (eventsFromApi is Success) {
            eventsFromApi.data?.forEach {
                eventCacheDataSource.saveProgrammingEvent(it)
            }
            cachedEvents.forEach { cachedEvent ->
                val exists = eventsFromApi.data?.firstOrNull { apiEvent ->
                    apiEvent.id == cachedEvent.id
                }
                if (exists == null) eventCacheDataSource.deleteProgrammingEvent(cachedEvent)
            }
            emit(eventCacheDataSource.getEvents())
        }
    }
}