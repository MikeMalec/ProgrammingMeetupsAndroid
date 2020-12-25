package com.example.programmingmeetups.business.interactors.event.join

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.di.EventCacheDataSourceImplementation
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JoinEvent @Inject constructor(
    @EventCacheDataSourceImplementation val eventCacheDataSource: EventCacheDataSource,
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    fun joinEvent(
        eventId: String,
        token: String,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<ProgrammingEvent?>> = flow {
        val response = safeApiCall(dispatcher) { eventNetworkDataSource.joinEvent(eventId, token) }
        if (response is Success) {
            response.data?.let {
                eventCacheDataSource.updateEvent(it)
            }
        }
        emit(response)
    }
}