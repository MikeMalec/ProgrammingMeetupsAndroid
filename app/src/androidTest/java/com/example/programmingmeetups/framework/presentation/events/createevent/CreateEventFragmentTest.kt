package com.example.programmingmeetups.framework.presentation.events.createevent

import android.net.Uri
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@ExperimentalCoroutinesApi
@UninstallModules(AppModule::class)
@HiltAndroidTest
@MediumTest
class CreateEventFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var androidCustomFragmentFactory: AndroidCustomFragmentFactory

    lateinit var args: Bundle

    @Before
    fun setup() {
        hiltRule.inject()
        args = Bundle()
        args.putParcelable("coordinates", LatLng(1.1, 1.1))
    }

    @Test
    fun click_setDate_showsDateDialog() {
        launchFragmentInHiltContainer<CreateEventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) { }
        onView(withId(R.id.clAddDate)).perform(click())
        onView(withId(R.id.btnSetDate)).check(matches(isDisplayed()))
    }

    @Test
    fun click_addTag_showsTagDialog() {
        launchFragmentInHiltContainer<CreateEventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) { }
        onView(withId(R.id.ivAddTag)).perform(click())
        onView(withId(R.id.createTagBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun openTagDialog_addTag_displayTag() {
        var viewModel: CreateEventViewModel? = null
        launchFragmentInHiltContainer<CreateEventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            viewModel = createEventViewModel
        }
        onView(withId(R.id.ivAddTag)).perform(click())
        onView(withId(R.id.etTag)).perform(replaceText("TAG"))
        onView(withId(R.id.createTagBtn)).perform(click())
        viewModel!!.event.getOrAwaitValue()
        onView(withText("TAG")).check(matches(isDisplayed()))
    }

    @Test
    fun createNewEvent_success_popBackstack() {
        val navController = mock(NavController::class.java)
        var viewModel: CreateEventViewModel? = null
        launchFragmentInHiltContainer<CreateEventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = args
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = this.createEventViewModel
        }

        viewModel!!.setImage(Uri.parse("test"))
        viewModel!!.setIcon(Uri.parse("test"))
        viewModel!!.setAddress("address")
        viewModel!!.setCoordinates(1.1, 1.1)
        viewModel!!.setDate(1L)

        onView(withId(R.id.etEventDescription)).perform(replaceText("event description"))

        onView(withId(R.id.btnCreateEvent)).perform(click())
        val createEventResponse =
            viewModel!!.createEventRequestResponse.getOrAwaitValue().peekContent()
        assertThat(createEventResponse).isEqualTo(
            Success(
                ProgrammingEvent(
                    id = "token",
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
        verify(navController).popBackStack()
    }
}