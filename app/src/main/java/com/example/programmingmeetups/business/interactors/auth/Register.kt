package com.example.programmingmeetups.business.interactors.auth

import com.example.programmingmeetups.business.data.network.auth.AuthRepository
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Register(val authRepository: AuthRepository) {
    fun register(
        image: MultipartBody.Part,
        firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        password: RequestBody,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<AuthResponse?>> = flow {
        val response =
            safeApiCall(dispatcher) {
                authRepository.register(
                    image,
                    firstName,
                    lastName,
                    email,
                    password
                )
            }
        emit(response)
    }
}