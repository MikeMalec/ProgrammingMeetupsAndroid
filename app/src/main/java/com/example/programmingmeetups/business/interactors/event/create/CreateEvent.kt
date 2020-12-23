package com.example.programmingmeetups.business.interactors.event.create

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateEvent(
    private val eventNetworkDataSource: EventNetworkDataSource,
    private val eventCacheDataSource: EventCacheDataSource
) {
    fun createEvent(
        token: String,
        image: MultipartBody.Part,
        icon: MultipartBody.Part,
        latitude: RequestBody,
        longitude: RequestBody,
        address: RequestBody,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<ProgrammingEvent?>> = flow {
        val response = safeApiCall(dispatcher) {
            eventNetworkDataSource.createEvent(
                token,
                image,
                icon,
                latitude,
                longitude,
                address,
                happensAt,
                tags,
                description
            )
        }
        if (response is Resource.Success) {
            response.data?.also { eventCacheDataSource.saveProgrammingEvent(it) }
        }
        emit(response)
    }
}