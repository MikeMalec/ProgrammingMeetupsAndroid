package com.example.programmingmeetups.framework.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

interface AuthViewModelFactory : ViewModelProvider.Factory

class AuthViewModelFactoryImpl(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository,
    private val authInteractors: AuthInteractors,
    private val authValidator: AuthValidator,
    private val requestBodyFactoryImpl: RequestBodyFactoryInterface,
    private val dispatcher: CoroutineDispatcher
) : AuthViewModelFactory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(
            preferencesRepository,
            authInteractors,
            authValidator,
            requestBodyFactoryImpl,
            dispatcher
        ) as T
    }
}