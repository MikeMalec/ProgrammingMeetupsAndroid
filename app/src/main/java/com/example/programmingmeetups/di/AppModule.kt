package com.example.programmingmeetups.di

import android.content.Context
import android.location.Geocoder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.network.event.sockets.EventCommentSocketManager
import com.example.programmingmeetups.framework.datasource.network.event.sockets.EventCommentSocketManagerInterface
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactoryImpl
import com.example.programmingmeetups.framework.presentation.map.LocationManager
import com.example.programmingmeetups.framework.presentation.map.LocationManagerInterface
import com.example.programmingmeetups.framework.utils.*
import com.example.programmingmeetups.framework.utils.localization.LocalizationDispatcher
import com.example.programmingmeetups.framework.utils.localization.LocalizationDispatcherInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

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

    @Named(IO_DISPATCHER)
    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return IO
    }

    @Named(LOCALIZATION_DISPATCHER_IMPL)
    @Singleton
    @Provides
    fun provideLocalizationDispatcher(geocoder: Geocoder): LocalizationDispatcherInterface {
        return LocalizationDispatcher(geocoder)
    }

    @Named(COMMENT_SOCKET_MANAGER_IMPL)
    @Provides
    fun provideEventCommentSocketManager(gson: Gson): EventCommentSocketManagerInterface {
        return EventCommentSocketManager(gson)
    }
}