package com.example.programmingmeetups.di

import android.content.Context
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactoryImpl
import com.example.programmingmeetups.utils.AUTH_FACTORY_IMPL
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import com.example.programmingmeetups.utils.files.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFileManager(@ApplicationContext context: Context): FileManager {
        return FileManager(context)
    }

    @Named(AUTH_FACTORY_IMPL)
    @Singleton
    @Provides
    fun provideAuthViewModelFactory(
        @Named(PREFERENCES_IMPLEMENTATION) preferencesRepository: PreferencesRepository,
        authInteractors: AuthInteractors,
        authValidator: AuthValidator,
        @RequestBodyFactoryImplementation requestBodyFactoryImpl: RequestBodyFactoryInterface
    ): AuthViewModelFactory {
        return AuthViewModelFactoryImpl(
            preferencesRepository,
            authInteractors,
            authValidator,
            requestBodyFactoryImpl,
            Dispatchers.IO
        )
    }
}