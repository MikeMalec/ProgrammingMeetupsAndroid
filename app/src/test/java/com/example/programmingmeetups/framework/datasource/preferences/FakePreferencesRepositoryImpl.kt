package com.example.programmingmeetups.framework.datasource.preferences

import com.example.programmingmeetups.business.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferencesRepositoryImpl : PreferencesRepository {

    var token: String? = "token"

    var userInfo: User? = null

    override fun getToken(): Flow<String?> = flow {
        emit(token)
    }

    override suspend fun saveToken(token: String) {
        this.token = token
    }

    override suspend fun saveUserInfo(user: User) {
        this.userInfo = user
    }

    override fun getUserInfo(): Flow<User?> = flow {
        emit(userInfo)
    }

}