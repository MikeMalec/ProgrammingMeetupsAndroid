package com.example.programmingmeetups.framework.presentation.events.showevent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.join.JoinEvent
import com.example.programmingmeetups.business.interactors.event.leave.LeaveEvent
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.example.programmingmeetups.utils.EDIT_EVENT
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import com.example.programmingmeetups.utils.JOIN_EVENT
import com.example.programmingmeetups.utils.LEAVE_EVENT
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EventViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var eventViewModel: EventViewModel
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private lateinit var fakeCacheNetworkDataSource: FakeEventCacheDataSource
    private lateinit var joinEvent: JoinEvent
    private lateinit var leaveEvent: LeaveEvent
    private lateinit var fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    private lateinit var user: User

    @Before
    fun setup() {
        fakeCacheNetworkDataSource = FakeEventCacheDataSource()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        joinEvent = JoinEvent(fakeCacheNetworkDataSource, fakeEventNetworkDataSource)
        leaveEvent = LeaveEvent(fakeCacheNetworkDataSource, fakeEventNetworkDataSource)
        fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
        fakePreferencesRepositoryImpl.token = "token"
        user = User("id", "firstName", "lastName", "email", "description", "image")
        fakePreferencesRepositoryImpl.userInfo = user
        eventViewModel = EventViewModel(
            fakePreferencesRepositoryImpl,
            joinEvent,
            leaveEvent,
            mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun joinEvent_success_emitsSuccessWithEvent() = runBlockingTest {
        val event = ProgrammingEvent("id", participants = mutableListOf())
        eventViewModel.setEvent(event)
        eventViewModel.joinEvent()
        eventViewModel.event.getOrAwaitValueTest()
        assertThat(eventViewModel.event.value).isEqualTo(
            ProgrammingEvent("id", participants = mutableListOf(user))
        )
    }

    @Test
    fun joinEvent_fail_emitsError() = runBlockingTest {
        val event = ProgrammingEvent("id", participants = mutableListOf())
        fakeEventNetworkDataSource.throwsException = true
        eventViewModel.setEvent(event)
        eventViewModel.joinEvent()
        eventViewModel.event.getOrAwaitValueTest()
        assertThat(eventViewModel.responseError.value?.peekContent()).isEqualTo(ERROR_UNKNOWN)
    }

    @Test
    fun leaveEvent_success_emitsSuccessWithEvent() = runBlockingTest {
        val event = ProgrammingEvent("id", participants = mutableListOf(user))
        eventViewModel.setEvent(event)
        eventViewModel.leaveEvent()
        eventViewModel.event.getOrAwaitValueTest()
        assertThat(eventViewModel.event.value).isEqualTo(
            ProgrammingEvent("id", participants = mutableListOf())
        )
    }

    @Test
    fun leaveEvent_fail_emitsError() = runBlockingTest {
        val event = ProgrammingEvent("id", participants = mutableListOf())
        fakeEventNetworkDataSource.throwsException = true
        eventViewModel.setEvent(event)
        eventViewModel.leaveEvent()
        eventViewModel.event.getOrAwaitValueTest()
        assertThat(eventViewModel.responseError.value?.peekContent()).isEqualTo(ERROR_UNKNOWN)
    }

    @Test
    fun setEvent_confirmThatUserIsOrganizer() = runBlockingTest {
        val event = ProgrammingEvent("id", participants = mutableListOf(), organizer = user)
        eventViewModel.setEvent(event)
        assertThat(eventViewModel.getUserEventRelation()).isEqualTo(EDIT_EVENT)
    }

    @Test
    fun setEvent_confirmThatUserIsParticipant() = runBlockingTest {
        val event = ProgrammingEvent(
            "id",
            participants = mutableListOf(user),
            organizer = User("2", "firstName", "lastName", "email", "desc", "image")
        )
        eventViewModel.setEvent(event)
        assertThat(eventViewModel.getUserEventRelation()).isEqualTo(LEAVE_EVENT)
    }

    @Test
    fun setEvent_confirmThatUserIsNotParticipant() = runBlockingTest {
        val event = ProgrammingEvent(
            "id",
            participants = mutableListOf(),
            organizer = User("2", "firstName", "lastName", "email", "desc", "image")
        )
        eventViewModel.setEvent(event)
        assertThat(eventViewModel.getUserEventRelation()).isEqualTo(JOIN_EVENT)
    }
}