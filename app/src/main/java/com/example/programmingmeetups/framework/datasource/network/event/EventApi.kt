package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EventApi {
    @Multipart
    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part icon: MultipartBody.Part,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("address") address: RequestBody,
        @Part("happensAt") happensAt: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part("description") description: RequestBody
    ): ProgrammingEventDto

    @GET("events")
    suspend fun fetchEvents(
        @Header("Authorization") token: String
    ): List<ProgrammingEventDto>
}