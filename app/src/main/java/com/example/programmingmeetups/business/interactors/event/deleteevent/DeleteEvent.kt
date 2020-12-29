package com.example.programmingmeetups.business.interactors.event.deleteevent

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.di.EventCacheDataSourceImplementation
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteEvent @Inject constructor(
    @EventCacheDataSourceImplementation val eventCacheDataSource: EventCacheDataSource,
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    fun deleteEvent(
        token: String,
        event: ProgrammingEvent,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<GenericResponse?>> = flow {
        val response =
            safeApiCall(dispatcher) { eventNetworkDataSource.deleteEvent(token, event.id!!) }
        if (response is Success) {
            eventCacheDataSource.deleteProgrammingEvent(event)
        }
        emit(response)
    }
}