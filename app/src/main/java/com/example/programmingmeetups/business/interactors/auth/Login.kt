package com.example.programmingmeetups.business.interactors.auth

import com.example.programmingmeetups.business.data.network.auth.AuthRepository
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Login(val authRepository: AuthRepository) {
    fun login(
        loginRequest: LoginRequest,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<AuthResponse?>> = flow {
        val response = safeApiCall(dispatcher) { authRepository.login(loginRequest) }
        emit(response)
    }
}