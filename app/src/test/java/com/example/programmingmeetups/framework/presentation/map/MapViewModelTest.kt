package com.example.programmingmeetups.framework.presentation.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.interactors.event.map.SynchronizeProgrammingEvents
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val fakePreferencesRepository = FakePreferencesRepositoryImpl()
    private var fakeLocationManager = FakeLocationManager()
    private lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private lateinit var mapViewModel: MapViewModel
    private lateinit var synchronizeProgrammingEvents: SynchronizeProgrammingEvents

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        synchronizeProgrammingEvents =
            SynchronizeProgrammingEvents(fakeEventCacheDataSource, fakeEventNetworkDataSource)
    }

    @Test
    fun synchronizeEvents_success_confirmNewEventsEmitted() = runBlocking {
        val cachedEvent = ProgrammingEvent(id = "1")
        val event = ProgrammingEvent(id = "2")
        fakeEventCacheDataSource.events.add(cachedEvent)
        fakeEventNetworkDataSource.events.add(event)
        mapViewModel = MapViewModel(
            fakePreferencesRepository,
            fakeLocationManager,
            synchronizeProgrammingEvents,
            mainCoroutineRule.dispatcher
        )
        mapViewModel.fetchEvents()
        val newEvents = mapViewModel.events.getOrAwaitValueTest()
        assertThat(newEvents).isEqualTo(listOf(event))
    }

    @Test
    fun synchronizeEvents_fails_confirmNoNewEventsEmitted() {
        fakeEventNetworkDataSource.throwsException = true
        val event = ProgrammingEvent(id = "2")
        fakeEventNetworkDataSource.events.add(event)
        mapViewModel = MapViewModel(
            fakePreferencesRepository,
            fakeLocationManager,
            synchronizeProgrammingEvents,
            mainCoroutineRule.dispatcher
        )
        mapViewModel.fetchEvents()
        val events = mapViewModel.events.getOrAwaitValueTest()
        assertThat(events).isEmpty()
    }
}