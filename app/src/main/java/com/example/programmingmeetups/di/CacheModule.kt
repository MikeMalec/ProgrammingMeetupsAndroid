package com.example.programmingmeetups.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSource
import com.example.programmingmeetups.business.data.cache.event.EventCacheDataSourceImpl
import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.interactors.event.map.SynchronizeProgrammingEvents
import com.example.programmingmeetups.business.interactors.event.user.GetUserEvents
import com.example.programmingmeetups.framework.datasource.cache.event.EventDaoService
import com.example.programmingmeetups.framework.datasource.cache.event.EventDaoServiceImpl
import com.example.programmingmeetups.framework.datasource.cache.event.database.EventDao
import com.example.programmingmeetups.framework.datasource.cache.event.database.EventDatabase
import com.example.programmingmeetups.framework.datasource.cache.event.mappers.EventCacheMapper
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepositoryImpl
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import com.example.programmingmeetups.utils.PREFERENCES_NAME
import com.example.programmingmeetups.utils.PROGRAMMING_MEETUPS_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.createDataStore(name = PREFERENCES_NAME)
    }

    @Singleton
    @Provides
    @Named(PREFERENCES_IMPLEMENTATION)
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideEventsDatabase(@ApplicationContext context: Context): EventDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EventDatabase::class.java,
            PROGRAMMING_MEETUPS_DB
        ).build()
    }

    @Singleton
    @Provides
    fun provideEventDao(eventDatabase: EventDatabase): EventDao {
        return eventDatabase.getEventDao()
    }

    @EventCacheDaoServiceImplementation
    @Singleton
    @Provides
    fun provideEventDaoService(
        eventDao: EventDao,
        eventCacheMapper: EventCacheMapper
    ): EventDaoService {
        return EventDaoServiceImpl(eventDao, eventCacheMapper)
    }

    @EventCacheDataSourceImplementation
    @Singleton
    @Provides
    fun provideEventCacheDataSource(
        @EventCacheDaoServiceImplementation eventDaoService: EventDaoService
    ): EventCacheDataSource {
        return EventCacheDataSourceImpl(eventDaoService)
    }

    @Singleton
    @Provides
    fun provideFetchProgrammingEvents(
        @EventCacheDataSourceImplementation eventCacheDataSource: EventCacheDataSource,
        @EventNetworkDataSourceImplementation eventNetworkDataSource: EventNetworkDataSource
    ): SynchronizeProgrammingEvents {
        return SynchronizeProgrammingEvents(eventCacheDataSource, eventNetworkDataSource)
    }

    @Singleton
    @Provides
    fun provideGetUserEvents(@EventCacheDataSourceImplementation eventCacheDataSource: EventCacheDataSource): GetUserEvents {
        return GetUserEvents(eventCacheDataSource)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EventCacheDataSourceImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EventCacheDaoServiceImplementation