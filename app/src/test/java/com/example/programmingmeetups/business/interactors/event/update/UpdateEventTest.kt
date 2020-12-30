package com.example.programmingmeetups.business.interactors.event.update

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.FakeRequestBodyFactoryImpl
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class UpdateEventTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var updateEvent: UpdateEvent
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    private val fakeRequestBodyFactoryImpl = FakeRequestBodyFactoryImpl()

    @Before
    fun setup() {
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        updateEvent = UpdateEvent(fakeEventNetworkDataSource, fakeEventCacheDataSource)
    }

    @Test
    fun updateEvent_success_confirmCacheUpdated() = runBlockingTest {
        fakeRequestBodyFactoryImpl.file = mock(File::class.java)
        val uri = mock(Uri::class.java)
        val event = ProgrammingEvent("id")
        fakeEventCacheDataSource.events.add(event)
        fakeEventNetworkDataSource.events.add(event)
        val happensAt = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val tags = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val description = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val image = fakeRequestBodyFactoryImpl.createImageRequestBody(uri)
        val icon = fakeRequestBodyFactoryImpl.createImageRequestBody(uri)
        updateEvent.updateEvent(
            "token",
            "id",
            happensAt,
            tags,
            description,
            image,
            icon,
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {}
        })

        assertThat(fakeEventCacheDataSource.events[0]).isEqualTo(
            ProgrammingEvent(
                "id",
                happensAt = 1L,
                tags = mutableListOf("updated"),
                description = "updated",
                image = "new image",
                icon = "new icon"
            )
        )
    }

    @Test
    fun updateEvent_fail_confirmCacheNotUpdated() = runBlockingTest {
        fakeRequestBodyFactoryImpl.file = mock(File::class.java)
        val uri = mock(Uri::class.java)
        val event = ProgrammingEvent("id")
        fakeEventCacheDataSource.events.add(event)
        fakeEventNetworkDataSource.events.add(event)
        fakeEventNetworkDataSource.throwsException = true
        val happensAt = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val tags = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val description = fakeRequestBodyFactoryImpl.createTextRequestBody("")
        val image = fakeRequestBodyFactoryImpl.createImageRequestBody(uri)
        val icon = fakeRequestBodyFactoryImpl.createImageRequestBody(uri)
        updateEvent.updateEvent(
            "token",
            "id",
            happensAt,
            tags,
            description,
            image,
            icon,
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {}
        })

        assertThat(fakeEventCacheDataSource.events[0]).isEqualTo(
            ProgrammingEvent("id")
        )
    }
}