package com.example.programmingmeetups.business.interactors.event.geteventusers

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.UsersResponse
import com.example.programmingmeetups.framework.utils.pagination.PaginationAction
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetEventUsers @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) : PaginationAction() {
    override suspend fun action(
        token: String,
        id: String,
        currentPage: Int,
        dispatcher: CoroutineDispatcher
    ): Resource<UsersResponse?> {
        return safeApiCall(dispatcher) {
            eventNetworkDataSource.getEventUsers(
                token,
                id,
                currentPage
            )
        }
    }
}