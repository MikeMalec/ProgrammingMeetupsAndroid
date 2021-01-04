package com.example.programmingmeetups.business.interactors.event.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.cache.event.FakeEventCacheDataSource
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.*

class SynchronizeUserEventsTest {
    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var fakeEventCacheDataSource: FakeEventCacheDataSource
    lateinit var getOwnEvents: GetOwnEvents

    @Before
    fun setup() {
        fakeEventCacheDataSource = FakeEventCacheDataSource()
        getOwnEvents =
            GetOwnEvents(fakeEventCacheDataSource)
    }

    @Test
    fun getUserEvents_success_confirmGotResults() = runBlockingTest {
        val programmingEvent = ProgrammingEvent(
            participants = listOf(
                User(
                    id = "1",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                ),
                User(
                    id = "2",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                ),
                User(
                    id = "3",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                )
            )
        )
        fakeEventCacheDataSource.events.add(programmingEvent)
        val userEvents = getOwnEvents.getOwnEvents("1")
        assertThat(userEvents[0]).isEqualTo(programmingEvent)
    }

    @Test
    fun getUserEvents_fail_confirmNoResults() = runBlockingTest {
        val programmingEvent = ProgrammingEvent(
            participants = listOf(
                User(
                    id = "1",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                ),
                User(
                    id = "2",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                ),
                User(
                    id = "3",
                    firstName = "firstName",
                    lastName = "lastName",
                    email = "email",
                    description = "description",
                    image = "image"
                )
            )
        )
        fakeEventCacheDataSource.events.add(programmingEvent)
        val userEvents = getOwnEvents.getOwnEvents("4")
        assertThat(userEvents).isEmpty()
    }
}