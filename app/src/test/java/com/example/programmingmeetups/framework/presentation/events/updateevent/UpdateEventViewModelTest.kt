package com.example.programmingmeetups.framework.presentation.events.updateevent

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.deleteevent.DeleteEvent
import com.example.programmingmeetups.business.interactors.event.update.UpdateEvent
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.FakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import com.example.programmingmeetups.utils.FILL_IN_ALL_FIELDS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File

@ExperimentalCoroutinesApi
class UpdateEventViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var updateEventViewModel: UpdateEventViewModel
    private lateinit var preferencesRepository: FakePreferencesRepositoryImpl
    private val eventValidator = EventValidator()
    private val requestBodyFactory = FakeRequestBodyFactoryImpl()
    private lateinit var updateEvent: UpdateEvent
    private lateinit var deleteEvent: DeleteEvent
    private lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        updateEvent = UpdateEvent(fakeEventNetworkDataSource, fakeEventCacheDataSource)
        deleteEvent = DeleteEvent(fakeEventCacheDataSource, fakeEventNetworkDataSource)
        preferencesRepository = FakePreferencesRepositoryImpl()
        preferencesRepository.token = "token"
        updateEventViewModel = UpdateEventViewModel(
            preferencesRepository,
            requestBodyFactory,
            eventValidator,
            updateEvent,
            deleteEvent,
            mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun update_success_confirmSuccessResponseEmitted() = runBlocking {
        val event = ProgrammingEvent(
            id = "id",
            image = "image",
            icon = "icon",
            description = "desc",
            latitude = 1.1,
            longitude = 1.1,
            address = "address",
            happensAt = 1L
        )
        fakeEventNetworkDataSource.events.add(event)
        requestBodyFactory.file = mock(File::class.java)
        updateEventViewModel.programmingEvent = event
        updateEventViewModel.imageUri = mock(Uri::class.java)
        updateEventViewModel.iconUri = mock(Uri::class.java)
        updateEventViewModel.attemptToUpdateEvent()
        val response =
            updateEventViewModel.updateRequestResponse.getOrAwaitValueTest().peekContent()
        assertThat(response).isEqualTo(
            Resource.Success(
                ProgrammingEvent(
                    id = "id",
                    tags = mutableListOf("updated"),
                    image = "new image",
                    icon = "new icon",
                    description = "updated",
                    latitude = 1.1,
                    longitude = 1.1,
                    address = "address",
                    happensAt = 1L
                )
            )
        )
    }

    @Test
    fun update_fail_confirmValidationErrorEmitted() = runBlockingTest {
        val event = ProgrammingEvent(
            id = "id",
            image = "image",
            icon = "icon",
            description = "desc",
            latitude = 1.1,
            longitude = 1.1,
            address = "address"
        )
        fakeEventNetworkDataSource.events.add(event)
        requestBodyFactory.file = mock(File::class.java)
        updateEventViewModel.programmingEvent = event
        updateEventViewModel.imageUri = mock(Uri::class.java)
        updateEventViewModel.iconUri = mock(Uri::class.java)
        updateEventViewModel.attemptToUpdateEvent()
        val response =
            updateEventViewModel.validationMessage.getOrAwaitValueTest().peekContent()
        assertThat(response).isEqualTo(
            FILL_IN_ALL_FIELDS
        )
    }

    @Test
    fun delete_success_confirmSuccessResponseEmitted() = runBlockingTest {
        val event = ProgrammingEvent(
            id = "id",
            image = "image",
            icon = "icon",
            description = "desc",
            latitude = 1.1,
            longitude = 1.1,
            address = "address"
        )
        fakeEventNetworkDataSource.events.add(event)
        fakeEventCacheDataSource.events.add(event)
        updateEventViewModel.programmingEvent = event
        updateEventViewModel.deleteEvent()
        val response =
            updateEventViewModel.deleteRequestResponse.getOrAwaitValueTest().peekContent()
        assertThat(response).isEqualTo(
            Resource.Success(GenericResponse())
        )
    }

    @Test
    fun delete_error_confirmErrorResponseEmitted() = runBlockingTest {
        val event = ProgrammingEvent(
            id = "id",
            image = "image",
            icon = "icon",
            description = "desc",
            latitude = 1.1,
            longitude = 1.1,
            address = "address"
        )
        fakeEventNetworkDataSource.events.add(event)
        fakeEventCacheDataSource.events.add(event)
        fakeEventNetworkDataSource.throwsException = true
        updateEventViewModel.programmingEvent = event
        updateEventViewModel.deleteEvent()
        val response =
            updateEventViewModel.deleteRequestResponse.getOrAwaitValueTest().peekContent()
        assertThat(response).isEqualTo(
            Resource.Error(ERROR_UNKNOWN)
        )
    }
}