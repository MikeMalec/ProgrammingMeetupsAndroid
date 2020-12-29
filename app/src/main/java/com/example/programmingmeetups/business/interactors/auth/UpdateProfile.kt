package com.example.programmingmeetups.business.interactors.auth

import com.example.programmingmeetups.business.data.network.auth.AuthRepository
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.di.AuthRepositoryImplementation
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateProfile
@Inject constructor(
    @AuthRepositoryImplementation val authRepository: AuthRepository
) {
    fun updateProfile(
        token: String,
        description: RequestBody,
        image: MultipartBody.Part?,
        dispatcher: CoroutineDispatcher
    ): Flow<Resource<AuthResponse?>> = flow {
        emit(Loading())
        val response =
            safeApiCall(dispatcher) { authRepository.updateProfile(token, description, image) }
        emit(response)
    }
}