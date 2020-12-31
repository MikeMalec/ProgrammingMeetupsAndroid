package com.example.programmingmeetups.business.interactors.event.join

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.framework.utils.ERROR_UNKNOWN
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import com.google.common.truth.Truth.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class JoinEventTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeEventCacheDataSourceImpl: FakeEventCacheDataSource
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private lateinit var joinEvent: JoinEvent

    @Before
    fun setup() {
        fakeEventCacheDataSourceImpl = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()

        joinEvent = JoinEvent(fakeEventCacheDataSourceImpl, fakeEventNetworkDataSource)
    }

    @Test
    fun joinEvent_success_confirmCachedEventUpdated() = runBlockingTest {
        val event = ProgrammingEvent("eventId", participants = mutableListOf())
        var updatedEvent: ProgrammingEvent? = null
        fakeEventCacheDataSourceImpl.events.add(event)
        joinEvent.joinEvent(event.id!!, "token", mainCoroutineRule.dispatcher).collect(object :
            FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {
                assertThat(value).isEqualTo(
                    Resource.Success(
                        ProgrammingEvent(
                            id = event.id,
                            participants = mutableListOf(
                                User(
                                    "id",
                                    "firstName",
                                    "lastName",
                                    "email",
                                    "description",
                                    "image"
                                )
                            )
                        )
                    )
                )
                updatedEvent = (value as Resource.Success).data
            }
        })
        assertThat(fakeEventCacheDataSourceImpl.events).contains(updatedEvent)
        assertThat(fakeEventCacheDataSourceImpl.events.size).isEqualTo(1)
    }

    @Test
    fun joinEvent_fails_confirmCachedEventNotChanged() = runBlockingTest {
        val event = ProgrammingEvent("eventId", participants = mutableListOf())
        fakeEventCacheDataSourceImpl.events.add(event)
        fakeEventNetworkDataSource.throwsException = true
        joinEvent.joinEvent(event.id!!, "token", mainCoroutineRule.dispatcher).collect(object :
            FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {
                assertThat(value).isEqualTo(
                    Resource.Error(ERROR_UNKNOWN)
                )
            }
        })
        assertThat(fakeEventCacheDataSourceImpl.events[0]).isEqualTo(event)
    }
}