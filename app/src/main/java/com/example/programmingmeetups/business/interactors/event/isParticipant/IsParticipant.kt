package com.example.programmingmeetups.business.interactors.event.isParticipant

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.IsParticipantResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class IsParticipant @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    suspend fun isParticipant(
        token: String,
        eventId: String,
        dispatcher: CoroutineDispatcher
    ): Resource<IsParticipantResponse?> {
        return safeApiCall(dispatcher) { eventNetworkDataSource.isParticipant(token, eventId) }
    }
}