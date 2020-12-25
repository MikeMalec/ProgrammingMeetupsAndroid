package com.example.programmingmeetups.framework.datasource.preferences

import androidx.datastore.preferences.core.preferencesKey
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.utils.*
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun saveUserInfo(user: User)

    fun getUserInfo(): Flow<User?>

    companion object {
        val token = preferencesKey<String>(PREFERENCES_TOKEN)
        val firstName = preferencesKey<String>(PREFERENCES_FIRST_NAME)
        val userId = preferencesKey<String>(USER_ID)
        val lastName = preferencesKey<String>(PREFERENCES_LAST_NAME)
        val email = preferencesKey<String>(PREFERENCES_EMAIL)
        val description = preferencesKey<String>(PREFERENCES_DESCRIPTION)
        val image = preferencesKey<String>(PREFERENCES_IMAGE)
    }
}