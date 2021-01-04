package com.example.programmingmeetups.business.interactors.event.user

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.UserEventsPaginationResponse
import com.example.programmingmeetups.framework.utils.pagination.PaginationAction
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetUserEvents @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) : PaginationAction() {
    override suspend fun action(
        token: String,
        id: String,
        currentPage: Int,
        dispatcher: CoroutineDispatcher
    ): Resource<UserEventsPaginationResponse?> {
        return safeApiCall(dispatcher) {
            eventNetworkDataSource.getUserEvents(token, id, currentPage)
        }
    }
}