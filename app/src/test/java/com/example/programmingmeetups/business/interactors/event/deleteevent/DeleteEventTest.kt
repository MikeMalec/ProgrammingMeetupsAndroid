package com.example.programmingmeetups.business.interactors.event.deleteevent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DeleteEventTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var deleteEvent: DeleteEvent
    private lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        deleteEvent = DeleteEvent(fakeEventCacheDataSource, fakeEventNetworkDataSource)
    }

    @Test
    fun deleteEvent_success_confirmCacheUpdated() = runBlocking {
        val event = ProgrammingEvent("id")
        fakeEventCacheDataSource.events.add(event)
        deleteEvent.deleteEvent("token", event, mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<GenericResponse?>> {
                override suspend fun emit(value: Resource<GenericResponse?>) {
                    assertThat(value).isEqualTo(Resource.Success(GenericResponse()))
                }
            })
        assertThat(fakeEventCacheDataSource.events.size).isEqualTo(0)
    }

    @Test
    fun deleteEvent_fail_confirmCacheNotUpdated() = runBlockingTest {
        val event = ProgrammingEvent("id")
        fakeEventCacheDataSource.events.add(event)
        fakeEventNetworkDataSource.throwsException = true
        deleteEvent.deleteEvent("token", event, mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<GenericResponse?>> {
                override suspend fun emit(value: Resource<GenericResponse?>) {
                    assertThat(value).isEqualTo(Resource.Error(ERROR_UNKNOWN))
                }
            })
        assertThat(fakeEventCacheDataSource.events.size).isEqualTo(1)
    }
}