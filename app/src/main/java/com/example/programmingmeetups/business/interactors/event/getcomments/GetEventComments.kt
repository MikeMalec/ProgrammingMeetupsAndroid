package com.example.programmingmeetups.business.interactors.event.getcomments

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.example.programmingmeetups.framework.datasource.network.event.model.EventCommentResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEventComments @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    fun getEventComments(
        token: String,
        eventId: String,
        page: Int,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<EventCommentResponse?>> = flow {
        val comments = safeApiCall(dispatcher) {
            eventNetworkDataSource.getEventComments(token, eventId, page)
        }
        emit(comments)
    }
}