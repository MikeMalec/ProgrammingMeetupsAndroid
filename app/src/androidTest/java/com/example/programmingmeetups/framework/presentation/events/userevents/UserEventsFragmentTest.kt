package com.example.programmingmeetups.framework.presentation.events.userevents

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.business.data.cache.event.AndroidFakeEventCacheDataSourceImpl
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
@MediumTest
class UserEventsFragmentTest {
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
    fun showsUserEvents() {
        val user = User("id", "firstName", "lastName", "email", "description", "image")
        var viewModel: UserEventsViewModel? = null
        var fakeCacheDataSource: AndroidFakeEventCacheDataSourceImpl? = null
        var fakePreferencesRepository: AndroidFakePreferencesRepositoryImpl? = null
        launchFragmentInHiltContainer<UserEventsFragment>(
            fragmentFactory =
            androidCustomFragmentFactory
        ) {
            viewModel = userEventsViewModel
        }
        fakeCacheDataSource =
            viewModel!!.getUserEvents.eventCacheDataSource as AndroidFakeEventCacheDataSourceImpl
        fakePreferencesRepository =
            viewModel!!.preferencesRepository as AndroidFakePreferencesRepositoryImpl
        fakePreferencesRepository.userInfo = user
        fakeCacheDataSource.events.add(
            ProgrammingEvent(
                "id",
                address = "address",
                happensAt = 1000L,
                participants = mutableListOf(user)
            )
        )
        viewModel!!.setEvents()
        val events = viewModel!!.userEvents.getOrAwaitValue()
        onView(withText("address")).check(matches(isDisplayed()))
    }

    @Test
    fun clickEvent_navigatesToEventFragment() {
        fun showsUserEvents() {
            val navigationController = mock(NavController::class.java)
            val user = User("id", "firstName", "lastName", "email", "description", "image")
            var viewModel: UserEventsViewModel? = null
            var fakeCacheDataSource: AndroidFakeEventCacheDataSourceImpl? = null
            var fakePreferencesRepository: AndroidFakePreferencesRepositoryImpl? = null
            launchFragmentInHiltContainer<UserEventsFragment>(
                fragmentFactory =
                androidCustomFragmentFactory
            ) {
                viewModel = userEventsViewModel
                Navigation.setViewNavController(requireView(), navigationController)
            }
            fakeCacheDataSource =
                viewModel!!.getUserEvents.eventCacheDataSource as AndroidFakeEventCacheDataSourceImpl
            fakePreferencesRepository =
                viewModel!!.preferencesRepository as AndroidFakePreferencesRepositoryImpl
            fakePreferencesRepository.userInfo = user
            val event = ProgrammingEvent(
                "id",
                address = "address",
                happensAt = 1000L,
                participants = mutableListOf(user)
            )
            fakeCacheDataSource.events.add(event)
            viewModel!!.setEvents()
            val events = viewModel!!.userEvents.getOrAwaitValue()
            onView(withText("address")).perform(click())
            verify(navigationController).navigate(UserEventsFragmentDirections.actionUserEventsFragmentToEventFragment(event))
        }
    }
}