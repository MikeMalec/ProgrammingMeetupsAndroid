package com.example.programmingmeetups.business.interactors.event.geteventusers

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.UsersResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetEventUsers @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    suspend fun getEventUsers(
        token: String,
        eventId: String,
        page: Int,
        dispatcher: CoroutineDispatcher
    ): Resource<UsersResponse?> {
        return safeApiCall(dispatcher) {
            eventNetworkDataSource.getEventUsers(
                token,
                eventId,
                page
            )
        }
    }

}