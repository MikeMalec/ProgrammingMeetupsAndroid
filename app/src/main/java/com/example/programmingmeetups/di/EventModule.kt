package com.example.programmingmeetups.di

import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSourceImpl
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.example.programmingmeetups.framework.datasource.network.event.EventApi
import com.example.programmingmeetups.framework.datasource.network.event.EventNetworkService
import com.example.programmingmeetups.framework.datasource.network.event.EventNetworkServiceImpl
import com.example.programmingmeetups.framework.datasource.network.event.mappers.EventNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object EventModule {
    @Singleton
    @Provides
    fun provideEventApi(retrofit: Retrofit.Builder): EventApi {
        return retrofit.build().create(EventApi::class.java)
    }

    @EventNetworkServiceImplementation
    @Singleton
    @Provides
    fun provideEventNetworkService(
        eventApi: EventApi,
        networkMapper: EventNetworkMapper
    ): EventNetworkService {
        return EventNetworkServiceImpl(eventApi, networkMapper)
    }

    @EventNetworkDataSourceImplementation
    @Singleton
    @Provides
    fun provideEventNetworkDataSource(
        @EventNetworkServiceImplementation eventNetworkService: EventNetworkService
    ): EventNetworkDataSource {
        return EventNetworkDataSourceImpl(eventNetworkService)
    }

    @Singleton
    @Provides
    fun provideCreateEvent(
        @EventNetworkDataSourceImplementation eventNetworkDataSource: EventNetworkDataSource,
        @EventCacheDataSourceImplementation eventCacheDataSource: EventCacheDataSource
    ): CreateEvent {
        return CreateEvent(eventNetworkDataSource, eventCacheDataSource)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EventNetworkServiceImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EventNetworkDataSourceImplementation