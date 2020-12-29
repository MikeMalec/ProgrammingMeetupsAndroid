package com.example.programmingmeetups.business.interactors.event.update

import android.util.Log
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateEvent @Inject constructor(
    @EventNetworkDataSourceImplementation private val eventNetworkDataSource: EventNetworkDataSource,
    @EventCacheDataSourceImplementation private val eventCacheDataSource: EventCacheDataSource
) {
    fun updateEvent(
        token: String,
        eventId: String,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody,
        image: MultipartBody.Part?,
        icon: MultipartBody.Part?,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<ProgrammingEvent?>> = flow {
        val response = safeApiCall(dispatcher) {
            eventNetworkDataSource.updateEvent(
                token,
                eventId,
                happensAt,
                tags,
                description,
                image,
                icon
            )
        }
        if (response is Success) {
            response.data?.also {
                eventCacheDataSource.updateEvent(it)
            }
        }
        emit(response)
    }
}