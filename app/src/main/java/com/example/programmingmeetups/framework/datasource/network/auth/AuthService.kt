package com.example.programmingmeetups.framework.datasource.network.auth

import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface AuthService {
    suspend fun register(
        image: MultipartBody.Part, firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): AuthResponse

    suspend fun login(loginRequest: LoginRequest): AuthResponse
}