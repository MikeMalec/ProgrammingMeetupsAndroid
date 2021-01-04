package com.example.programmingmeetups.business.interactors.event.getamountofeventusers

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.UsersAmountResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAmountOfEventUsers @Inject constructor(@EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource) {
    suspend fun getAmountOfEventUsers(
        token: String,
        eventId: String,
        dispatcher: CoroutineDispatcher
    ): Resource<UsersAmountResponse?> {
        return safeApiCall(dispatcher) {
            eventNetworkDataSource.getAmountOfEventUsers(
                token,
                eventId
            )
        }
    }
}