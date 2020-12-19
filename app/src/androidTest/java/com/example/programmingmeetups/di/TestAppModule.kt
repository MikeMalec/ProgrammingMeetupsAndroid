package com.example.programmingmeetups.di

import com.example.programmingmeetups.business.data.network.auth.AndroidFakeAuthRepository
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.business.interactors.auth.Login
import com.example.programmingmeetups.business.interactors.auth.Register
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.AndroidFakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactoryImpl
import com.example.programmingmeetups.utils.AUTH_FACTORY_IMPL
import com.example.programmingmeetups.utils.FAKE_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Named(FAKE_PREFERENCES)
    @Singleton
    @Provides
    fun provideFakePreferences(): AndroidFakePreferencesRepositoryImpl {
        return AndroidFakePreferencesRepositoryImpl()
    }

    @Named(AUTH_FACTORY_IMPL)
    @Singleton
    @Provides
    fun provideAuthViewModelFactory(
        @Named(FAKE_PREFERENCES) preferencesRepository: AndroidFakePreferencesRepositoryImpl
    ): AuthViewModelFactory {
        return AuthViewModelFactoryImpl(
            preferencesRepository,
            AuthInteractors(
                Register(AndroidFakeAuthRepository()),
                Login(AndroidFakeAuthRepository())
            ),
            AuthValidator(),
            AndroidFakeRequestBodyFactoryImpl(),
            Dispatchers.Main
        )
    }

    @Singleton
    @Provides
    fun provideAuthViewModel(
        @Named(FAKE_PREFERENCES) preferencesRepository: AndroidFakePreferencesRepositoryImpl
    ): AuthViewModel {
        return AuthViewModel(
            preferencesRepository,
            AuthInteractors(
                Register(AndroidFakeAuthRepository()),
                Login(AndroidFakeAuthRepository())
            ),
            AuthValidator(),
            AndroidFakeRequestBodyFactoryImpl(),
            Dispatchers.Main
        )
    }
}