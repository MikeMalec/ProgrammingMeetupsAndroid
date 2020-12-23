package com.example.programmingmeetups.business.interactors.event.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CreateEventTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    lateinit var createEvent: CreateEvent

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        createEvent = CreateEvent(fakeEventNetworkDataSource, fakeEventCacheDataSource)
    }

    @Test
    fun createEvent_success_cacheUpdated() = runBlockingTest {
        createEvent.createEvent(
            "token123",
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            RequestBody.create(MediaType.parse("text/plain"), "latitude"),
            RequestBody.create(MediaType.parse("text/plain"), "longitude"),
            RequestBody.create(MediaType.parse("text/plain"), "address"),
            RequestBody.create(MediaType.parse("text/plain"), "happensAt"),
            RequestBody.create(MediaType.parse("text/plain"), "tags"),
            RequestBody.create(MediaType.parse("text/plain"), "description"),
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {
                assertThat(value).isEqualTo(
                    Resource.Success(
                        ProgrammingEvent(
                            id = "token123",
                            image = "image",
                            icon = "icon",
                            latitude = 1.1,
                            longitude = 1.1,
                            address = "test",
                            happensAt = 1,
                            tags = mutableListOf("test1", "test2"),
                            description = "test"
                        )
                    )
                )
            }
        })
        assertThat(fakeEventCacheDataSource.events[0]).isEqualTo(
            ProgrammingEvent(
                id = "token123",
                image = "image",
                icon = "icon",
                latitude = 1.1,
                longitude = 1.1,
                address = "test",
                happensAt = 1,
                tags = mutableListOf("test1", "test2"),
                description = "test"
            )
        )
    }

    @Test
    fun createEvent_fail_confirmCacheUnchanged() = runBlockingTest {
        fakeEventNetworkDataSource.createEventThrowsException = true
        createEvent.createEvent(
            "token123",
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            RequestBody.create(MediaType.parse("text/plain"), "latitude"),
            RequestBody.create(MediaType.parse("text/plain"), "longitude"),
            RequestBody.create(MediaType.parse("text/plain"), "address"),
            RequestBody.create(MediaType.parse("text/plain"), "happensAt"),
            RequestBody.create(MediaType.parse("text/plain"), "tags"),
            RequestBody.create(MediaType.parse("text/plain"), "description"),
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<ProgrammingEvent?>> {
            override suspend fun emit(value: Resource<ProgrammingEvent?>) {
                assertThat(value).isEqualTo(Resource.Error(ERROR_UNKNOWN))
            }
        })
        assertThat(fakeEventCacheDataSource.events).isEmpty()
    }
}