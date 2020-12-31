package com.example.programmingmeetups.framework.presentation.events.showevent

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import com.example.programmingmeetups.framework.utils.EDIT_EVENT
import com.example.programmingmeetups.framework.utils.JOIN_EVENT
import com.example.programmingmeetups.framework.utils.LEAVE_EVENT
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@UninstallModules(AppModule::class)
@HiltAndroidTest
@MediumTest
class EventFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var androidCustomFragmentFactory: AndroidCustomFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun display_event_asOrganizer() = runBlockingTest {
        androidCustomFragmentFactory.androidFakePreferencesRepository.userInfo =
            User("id", "firstName", "lastName", "email", "desc", "img")
        androidCustomFragmentFactory.androidFakePreferencesRepository.token = "token"
        val args = Bundle()
        args.putParcelable(
            "event", ProgrammingEvent(
                id = "id",
                address = "address",
                tags = mutableListOf("kotlin", "android", "node"),
                image = "image",
                icon = "icon",
                organizer = User("id", "firstName", "lastName", "email", "desc", "img"),
                latitude = 1.1,
                longitude = 1.1,
                happensAt = 1608836649930L,
                description = "description"
            )
        )
        var viewModel: EventViewModel? = null
        launchFragmentInHiltContainer<EventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = eventViewModel
        }
        // have to call it like this otherwise data binding not called
        val event = viewModel?.event?.getOrAwaitValue()
        viewModel!!.setEvent(event!!)

        onView(withId(R.id.tvEventAddress)).check(matches(withText("address")))
        onView(withId(R.id.tvEventDescription)).check(matches(withText("description")))
        onView(withId(R.id.tvEventDate)).check(matches(withText("Thu 24 December 8:04 PM")))
        onView(withId(R.id.tvOrganizerName)).check(matches(withText("firstName lastName")))
        onView(withText("kotlin")).check(matches(isDisplayed()))
        onView(withText("android")).check(matches(isDisplayed()))
        onView(withText("node")).check(matches(isDisplayed()))
        onView(withId(R.id.btnEventAction)).check(matches(withText(EDIT_EVENT)))
    }

    @Test
    fun display_event_asParticipant() = runBlockingTest {
        val user = User("id", "firstName", "lastName", "email", "desc", "img")
        androidCustomFragmentFactory.androidFakePreferencesRepository.userInfo = user
        androidCustomFragmentFactory.androidFakePreferencesRepository.token = "token"
        val args = Bundle()
        args.putParcelable(
            "event", ProgrammingEvent(
                id = "id",
                address = "address",
                tags = mutableListOf("kotlin", "android", "node"),
                image = "image",
                icon = "icon",
                organizer = User("id2", "firstName", "lastName", "email", "desc", "img"),
                latitude = 1.1,
                longitude = 1.1,
                participants = mutableListOf(user),
                happensAt = 1608836649930L,
                description = "description"
            )
        )
        var viewModel: EventViewModel? = null
        launchFragmentInHiltContainer<EventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = eventViewModel
        }
        val event = viewModel?.event?.getOrAwaitValue()
        viewModel!!.setEvent(event!!)
        onView(withId(R.id.btnEventAction)).check(matches(withText(LEAVE_EVENT)))
    }

    @Test
    fun display_event_asNotParticipant() = runBlockingTest {
        val user = User("id", "firstName", "lastName", "email", "desc", "img")
        androidCustomFragmentFactory.androidFakePreferencesRepository.userInfo = user
        androidCustomFragmentFactory.androidFakePreferencesRepository.token = "token"
        val args = Bundle()
        args.putParcelable(
            "event", ProgrammingEvent(
                id = "id",
                address = "address",
                tags = mutableListOf("kotlin", "android", "node"),
                image = "image",
                icon = "icon",
                organizer = User("id2", "firstName", "lastName", "email", "desc", "img"),
                latitude = 1.1,
                longitude = 1.1,
                participants = mutableListOf(),
                happensAt = 1608836649930L,
                description = "description"
            )
        )
        var viewModel: EventViewModel? = null
        launchFragmentInHiltContainer<EventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = eventViewModel
        }
        val event = viewModel?.event?.getOrAwaitValue()
        viewModel!!.setEvent(event!!)
        onView(withId(R.id.btnEventAction)).check(matches(withText(JOIN_EVENT)))
    }

    @Test
    fun joinEvent_success_displayAsParticipant() {
        val user = User("id", "firstName", "lastName", "email", "description", "image")
        androidCustomFragmentFactory.androidFakePreferencesRepository.userInfo = user
        androidCustomFragmentFactory.androidFakePreferencesRepository.token = "token"
        val args = Bundle()
        args.putParcelable(
            "event", ProgrammingEvent(
                id = "id",
                address = "address",
                tags = mutableListOf("kotlin", "android", "node"),
                image = "image",
                icon = "icon",
                organizer = User("id2", "firstName", "lastName", "email", "desc", "img"),
                latitude = 1.1,
                longitude = 1.1,
                participants = mutableListOf(),
                happensAt = 1608836649930L,
                description = "description"
            )
        )
        var viewModel: EventViewModel? = null
        launchFragmentInHiltContainer<EventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = eventViewModel
        }
        val event = viewModel?.event?.getOrAwaitValue()
        viewModel!!.setEvent(event!!)
        onView(withId(R.id.btnEventAction)).check(matches(withText(JOIN_EVENT)))
        viewModel!!.joinEvent()
        onView(withId(R.id.btnEventAction)).check(matches(withText(LEAVE_EVENT)))
    }

    @Test
    fun leaveEvent_success_displayAsNotParticipant() = runBlockingTest {
        val user = User("id", "firstName", "lastName", "email", "desc", "img")
        androidCustomFragmentFactory.androidFakePreferencesRepository.userInfo = user
        androidCustomFragmentFactory.androidFakePreferencesRepository.token = "token"
        val args = Bundle()
        args.putParcelable(
            "event", ProgrammingEvent(
                id = "id",
                address = "address",
                tags = mutableListOf("kotlin", "android", "node"),
                image = "image",
                icon = "icon",
                organizer = User("id2", "firstName", "lastName", "email", "desc", "img"),
                latitude = 1.1,
                longitude = 1.1,
                participants = mutableListOf(user),
                happensAt = 1608836649930L,
                description = "description"
            )
        )
        var viewModel: EventViewModel? = null
        launchFragmentInHiltContainer<EventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = eventViewModel
        }
        val event = viewModel?.event?.getOrAwaitValue()
        viewModel!!.setEvent(event!!)
        onView(withId(R.id.btnEventAction)).check(matches(withText(LEAVE_EVENT)))
        viewModel!!.leaveEvent()
        onView(withId(R.id.btnEventAction)).check(matches(withText(JOIN_EVENT)))
    }
}