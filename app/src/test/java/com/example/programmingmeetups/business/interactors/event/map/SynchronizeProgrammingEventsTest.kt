package com.example.programmingmeetups.business.interactors.event.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SynchronizeProgrammingEventsTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    lateinit var synchronizeProgrammingEvent: SynchronizeProgrammingEvents

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        synchronizeProgrammingEvent =
            SynchronizeProgrammingEvents(fakeEventCacheDataSource, fakeEventNetworkDataSource)
    }

    @Test
    fun synchronizeEvents_success_confirmCacheUpdated() = runBlockingTest {
        val cachedEvent = ProgrammingEvent("cached",address = "cached")
        fakeEventCacheDataSource.events.add(cachedEvent)
        val event = ProgrammingEvent(
            id = "id",
            address = "address"
        )
        fakeEventNetworkDataSource.events.add(event)
        var counter = 0
        synchronizeProgrammingEvent.synchronizeEvents("token", mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<List<ProgrammingEvent>?> {
                override suspend fun emit(value: List<ProgrammingEvent>?) {
                    if (counter == 0) {
                        assertThat(value).contains(cachedEvent)
                    } else {
                        assertThat(value).isEqualTo(listOf(event))
                    }
                    counter++
                }
            })
    }

    @Test
    fun synchronizeEvents_fail_confirmCacheUnchanged() = runBlockingTest {
        fakeEventNetworkDataSource.fetchEventsThrowsException = true
        synchronizeProgrammingEvent.synchronizeEvents("token", mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<List<ProgrammingEvent>?> {
                override suspend fun emit(value: List<ProgrammingEvent>?) {
                    assertThat(value).isEmpty()
                }
            })
    }
}