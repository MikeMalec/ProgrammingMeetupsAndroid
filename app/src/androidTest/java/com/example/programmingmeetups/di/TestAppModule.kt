package com.example.programmingmeetups.di

import android.content.Context
import android.location.Geocoder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.programmingmeetups.business.data.network.auth.AndroidFakeAuthRepository
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.business.interactors.auth.Login
import com.example.programmingmeetups.business.interactors.auth.Register
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.AndroidFakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.network.event.sockets.AndroidFakeCommentSocketManager
import com.example.programmingmeetups.framework.datasource.network.event.sockets.EventCommentSocketManagerInterface
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactoryImpl
import com.example.programmingmeetups.framework.presentation.map.LocationManager
import com.example.programmingmeetups.framework.presentation.map.LocationManagerInterface
import com.example.programmingmeetups.framework.utils.*
import com.example.programmingmeetups.framework.utils.localization.FakeLocalizationDispatcherImpl
import com.example.programmingmeetups.framework.utils.localization.LocalizationDispatcherInterface
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Named(IO_DISPATCHER)
    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

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

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return FusedLocationProviderClient(context)
    }

    @Named(LOCATION_MANAGER_IMPL)
    @Singleton
    @Provides
    fun provideLocationManager(fusedLocationProviderClient: FusedLocationProviderClient): LocationManagerInterface {
        return LocationManager(fusedLocationProviderClient)
    }

    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }

    @Singleton
    @Provides
    fun provideMarkerIconRequestOptions(): RequestOptions {
        val requestOptions = RequestOptions()
        requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        return requestOptions
    }

    @Named(MARKER_GLIDE)
    @Singleton
    @Provides
    fun provideMarkerIconGlideInstance(
        @ApplicationContext context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context).setDefaultRequestOptions(requestOptions)
    }

    @Named(LOCALIZATION_DISPATCHER_IMPL)
    @Singleton
    @Provides
    fun provideFakeLocalizationDispatcher(): LocalizationDispatcherInterface {
        return FakeLocalizationDispatcherImpl()
    }

    @Named(COMMENT_SOCKET_MANAGER_IMPL)
    @Provides
    fun provideEventCommentSocketManager(): EventCommentSocketManagerInterface {
        return AndroidFakeCommentSocketManager()
    }
}