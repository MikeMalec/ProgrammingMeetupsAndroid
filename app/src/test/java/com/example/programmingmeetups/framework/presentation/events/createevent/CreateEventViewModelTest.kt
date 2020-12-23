package com.example.programmingmeetups.framework.presentation.events.createevent

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.FakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.example.programmingmeetups.utils.FILL_IN_ALL_FIELDS
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File

@ExperimentalCoroutinesApi
class CreateEventViewModelTest {
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val eventValidator = EventValidator()
    private val fakeRequestBodyFactoryImpl = FakeRequestBodyFactoryImpl()
    private val fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
    lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    lateinit var createEventViewModel: CreateEventViewModel
    lateinit var createEvent: CreateEvent

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        fakePreferencesRepositoryImpl.token = "token"
        createEvent = CreateEvent(fakeEventNetworkDataSource, fakeEventCacheDataSource)
    }

    @Test
    fun createEvent_fails_confirmValidationMessageEmitted() = runBlockingTest {
        createEventViewModel =
            CreateEventViewModel(
                fakePreferencesRepositoryImpl,
                fakeRequestBodyFactoryImpl,
                createEvent,
                eventValidator,
                mainCoroutineRule.dispatcher
            )
        createEventViewModel.attemptToCreateEvent()
        val validationMessage = createEventViewModel.validationMessage.getOrAwaitValueTest()
        assertThat(validationMessage.peekContent()).isEqualTo(FILL_IN_ALL_FIELDS)
    }

    @Test
    fun createEvent_success_confirmSuccessResponseEmitted() = runBlockingTest {
        val uri = mock(Uri::class.java)
        val file = mock(File::class.java)
        fakeRequestBodyFactoryImpl.file = file
        createEventViewModel =
            CreateEventViewModel(
                fakePreferencesRepositoryImpl,
                fakeRequestBodyFactoryImpl,
                createEvent,
                eventValidator,
                mainCoroutineRule.dispatcher
            )
        createEventViewModel.setCoordinates(1.1, 1.1)
        createEventViewModel.setAddress("address")
        createEventViewModel.setDate(1L)
        createEventViewModel.setImage(uri)
        createEventViewModel.setIcon(uri)
        createEventViewModel.setDescription("description")
        createEventViewModel.attemptToCreateEvent()
        val createEventResponse = createEventViewModel.createEventRequestResponse.getOrAwaitValueTest().peekContent()
        assertThat(createEventResponse).isEqualTo(Resource.Success(
            ProgrammingEvent(
            id = "Bearer token",
            image = "image",
            icon = "icon",
            latitude = 1.1,
            longitude = 1.1,
            address = "test",
            happensAt = 1,
            tags = mutableListOf("test1", "test2"),
            description = "test"
        )
        ))
    }
}