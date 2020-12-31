package com.example.programmingmeetups.framework.presentation.events.eventcomments

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.data.network.event.AndroidFakeEventNetworkDataSourceImpl
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import com.example.programmingmeetups.framework.utils.AndroidEventCommentFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class EventCommentsFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var androidCustomFragmentFactory: AndroidCustomFragmentFactory

    @Inject
    lateinit var androidEventCommentFactory: AndroidEventCommentFactory

    private lateinit var bundle: Bundle

    @Before
    fun setup() {
        hiltRule.inject()
        bundle = Bundle()
        bundle.putParcelable("event", ProgrammingEvent("id"))
    }

    @Test
    fun displayComments() {
        var fakeEventNetworkDataSourceImpl: AndroidFakeEventNetworkDataSourceImpl? = null
        var viewModel: EventCommentsViewModel? = null
        launchFragmentInHiltContainer<EventCommentsFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = bundle
        ) {
            viewModel = eventCommentsViewModel
        }
        fakeEventNetworkDataSourceImpl =
            viewModel!!.getEventComments.eventNetworkDataSource as AndroidFakeEventNetworkDataSourceImpl
        fakeEventNetworkDataSourceImpl.comments = androidEventCommentFactory.createComments(19)
        viewModel!!.fetchComments("id")
        viewModel!!.comments.getOrAwaitValue()
        onView(withText("comment #0")).check(matches(isDisplayed()))
    }

    @Test
    fun createComment_success_confirmDisplayed() {
        var viewModel: EventCommentsViewModel? = null
        launchFragmentInHiltContainer<EventCommentsFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = bundle
        ) {
            viewModel = eventCommentsViewModel
        }
        onView(withId(R.id.etComment)).perform(typeText("Comment"), closeSoftKeyboard())
        onView(withId(R.id.btnSendComment)).perform(click())
        onView(withId(R.id.etComment)).perform(typeText(""), closeSoftKeyboard())
        viewModel!!.comments.getOrAwaitValue()
        onView(withText("Comment")).check(matches(isDisplayed()))
    }

    @Test
    fun paginate_success_confirmPaginatedDataDisplayed() {
        var fakeEventNetworkDataSourceImpl: AndroidFakeEventNetworkDataSourceImpl? = null
        var viewModel: EventCommentsViewModel? = null
        var fragment: EventCommentsFragment? = null
        launchFragmentInHiltContainer<EventCommentsFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = bundle
        ) {
            viewModel = eventCommentsViewModel
            fragment = this
        }
        fakeEventNetworkDataSourceImpl =
            viewModel!!.getEventComments.eventNetworkDataSource as AndroidFakeEventNetworkDataSourceImpl
        fakeEventNetworkDataSourceImpl.comments = androidEventCommentFactory.createComments(29)
        viewModel!!.fetchComments("id")
        viewModel!!.comments.getOrAwaitValue()
        viewModel!!.fetchComments("id")
        viewModel!!.comments.getOrAwaitValue()
        onView(withId(R.id.rvComments)).perform(
            RecyclerViewActions.scrollToPosition<EventCommentAdapter.EventCommentViewHolder>(
                20
            )
        )
        viewModel!!.comments.getOrAwaitValue()
        assertThat(fragment!!.eventCommentAdapter.itemCount).isEqualTo(29)
    }
}