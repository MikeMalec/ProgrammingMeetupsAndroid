package com.example.programmingmeetups.business.data.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface EventNetworkDataSource {
    suspend fun createEvent(
        token: String,
        image: MultipartBody.Part,
        icon: MultipartBody.Part,
        latitude: RequestBody,
        longitude: RequestBody,
        address: RequestBody,
        happensAt: RequestBody,
        tags: RequestBody,
        description: RequestBody
    ): ProgrammingEvent

    suspend fun fetchEvents(token: String): List<ProgrammingEvent>
}