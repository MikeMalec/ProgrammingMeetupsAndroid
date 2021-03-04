package com.example.programmingmeetups.business.interactors.event.synchronizeuserevents

import android.util.Log
import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.data.util.safeCacheCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventCacheDataSourceImplementation
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SynchronizeUserEvents @Inject constructor(
    @EventCacheDataSourceImplementation val eventCacheDataSource: EventCacheDataSource,
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    suspend fun synchronizeUserEvents(token: String, dispatcher: CoroutineDispatcher) {
        val response = safeApiCall(dispatcher) { eventNetworkDataSource.getOwnEvents(token) }
        if (response is Resource.Success) {
            response.data?.also {
                val apiEvents = it
                val cachedEvents = eventCacheDataSource.getAllEvents()
                cachedEvents.forEach { cachedEvent ->
                    val existsInApiResponse =
                        apiEvents.firstOrNull { apiEvent -> apiEvent.id == cachedEvent.id }
                    existsInApiResponse ?: eventCacheDataSource.deleteProgrammingEvent(cachedEvent)
                }
                apiEvents.forEach {
                    safeCacheCall(dispatcher) {
                        eventCacheDataSource.saveProgrammingEvent(it)
                    }
                }
            }
        }
    }
}