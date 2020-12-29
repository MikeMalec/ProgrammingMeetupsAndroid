package com.example.programmingmeetups.framework.datasource.network.auth.data

import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part image: MultipartBody.Part,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @Multipart
    @PUT("auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): AuthResponse
}