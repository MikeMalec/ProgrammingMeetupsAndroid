package com.example.programmingmeetups.framework.datasource.preferences

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {

    override fun getToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesRepository.token]
            }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesRepository.token] = token
        }
    }

    override suspend fun saveUserInfo(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesRepository.firstName] = user.firstName
            preferences[PreferencesRepository.lastName] = user.lastName
            preferences[PreferencesRepository.email] = user.email
            preferences[PreferencesRepository.password] = user.password
            preferences[PreferencesRepository.description] = user.description
            preferences[PreferencesRepository.image] = user.image
        }
    }

    override fun getUserInfo(): Flow<User?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                User(
                    preferences[PreferencesRepository.firstName]!!,
                    preferences[PreferencesRepository.lastName]!!,
                    preferences[PreferencesRepository.email]!!,
                    preferences[PreferencesRepository.password]!!,
                    preferences[PreferencesRepository.description]!!,
                    preferences[PreferencesRepository.image]!!
                )
            }
    }
}