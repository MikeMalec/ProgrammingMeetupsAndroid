package com.example.programmingmeetups.business.data.network.auth

import com.example.programmingmeetups.framework.datasource.network.auth.AuthService
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {
    override suspend fun register(
        image: MultipartBody.Part,
        firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): AuthResponse {
        return authService.register(image, firstName, lastName, email, password)
    }

    override suspend fun login(
        loginRequest: LoginRequest
    ): AuthResponse {
        return authService.login(loginRequest)
    }

    override suspend fun updateProfile(
        token: String,
        description: RequestBody,
        image: MultipartBody.Part?
    ): AuthResponse {
        return authService.updateProfile(token, description, image)
    }
}