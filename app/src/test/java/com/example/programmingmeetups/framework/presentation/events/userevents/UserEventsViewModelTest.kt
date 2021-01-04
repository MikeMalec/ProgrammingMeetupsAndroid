package com.example.programmingmeetups.framework.presentation.events.userevents

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.interactors.event.user.GetOwnEvents
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import kotlinx.coroutines.test.runBlockingTest
import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserEventsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    private lateinit var getOwnEvents: GetOwnEvents
    private lateinit var userEventsViewModel: UserEventsViewModel
    private lateinit var preferencesRepositoryImpl: FakePreferencesRepositoryImpl

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        getOwnEvents = GetOwnEvents(fakeEventCacheDataSource)
        preferencesRepositoryImpl = FakePreferencesRepositoryImpl()
    }

    @Test
    fun getUserEvents_success_livedataContainsUserEvents() = runBlockingTest {
        val user = User(
            "id",
            "firstName",
            "lastName",
            "email",
            "description",
            "image"
        )
        fakeEventCacheDataSource.events.add(
            ProgrammingEvent(
                id = "1",
                participants = mutableListOf(
                    user
                )
            )
        )
        preferencesRepositoryImpl.userInfo = user
        userEventsViewModel = UserEventsViewModel(
            preferencesRepositoryImpl,
            getOwnEvents,
            mainCoroutineRule.dispatcher
        )
        val userEvents = userEventsViewModel.userEvents.getOrAwaitValueTest()
        assertThat(userEvents).contains(
            ProgrammingEvent(
                id = "1",
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
    }
}