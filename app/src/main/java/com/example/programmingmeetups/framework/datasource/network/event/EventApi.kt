package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.*
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

    @Multipart
    @POST("events/{id}")
    suspend fun updateEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: String,
        @Part("happensAt") happensAt: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part icon: MultipartBody.Part? = null
    ): ProgrammingEventDto

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: String
    ): GenericResponse

    @GET("events")
    suspend fun fetchEvents(
        @Header("Authorization") token: String
    ): List<ProgrammingEventDto>

    @GET("events")
    suspend fun fetchEvents(
        @Header("Authorization") token: String,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("radius") radius: Double
    ): List<ProgrammingEventDto>

    @GET("events/{id}/isParticipant")
    suspend fun isParticipant(
        @Header("Authorization") token: String,
        @Path("id") eventId: String
    ): IsParticipantResponse

    @POST("events/{id}/join")
    suspend fun joinEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: String
    ): ProgrammingEventDto

    @DELETE("events/{id}/leave")
    suspend fun leaveEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: String
    ): ProgrammingEventDto

    @GET("events/{id}/comments")
    suspend fun getEventComments(
        @Header("Authorization") token: String,
        @Path("id") eventId: String,
        @Query("page") page: Int
    ): EventCommentResponse

    @GET("events/{id}/users")
    suspend fun getEventUsers(
        @Header("Authorization") token: String,
        @Path("id") eventId: String,
        @Query("page") page: Int
    ): UsersResponse

    @GET("events/{id}/users/amount")
    suspend fun getAmountOfEventUsers(
        @Header("Authorization") token: String,
        @Path("id") eventId: String
    ): UsersAmountResponse

    @GET("users/events")
    suspend fun getUserEvents(
        @Header("Authorization") token: String
    ): List<ProgrammingEventDto>
}