package com.example.programmingmeetups.framework.presentation.events.updateevent

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.business.data.cache.event.AndroidFakeEventCacheDataSourceImpl
import com.example.programmingmeetups.business.data.network.event.AndroidFakeEventNetworkDataSourceImpl
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class UpdateEventFragmentTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var androidCustomFragmentFactory: AndroidCustomFragmentFactory

    private val event = ProgrammingEvent(
        "id",
        mutableListOf(),
        "image",
        "icon",
        null,
        1.1,
        1.1,
        "address",
        1L,
        "desc"
    )

    private lateinit var bundle: Bundle

    @Before
    fun setup() {
        hiltRule.inject()
        bundle = Bundle()
        bundle.putParcelable("event", event)
    }

    @Test
    fun updateEvent_success_navigatesBack() {
        val navController = mock(NavController::class.java)
        var cacheDataSource: AndroidFakeEventCacheDataSourceImpl? = null
        var networkDataSource: AndroidFakeEventNetworkDataSourceImpl? = null
        var viewModel: UpdateEventViewModel? = null
        launchFragmentInHiltContainer<UpdateEventFragment>(
            fragmentFactory = androidCustomFragmentFactory,
            fragmentArgs = bundle
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = updateEventViewModel
            cacheDataSource =
                updateEventViewModel!!.updateEvent.eventCacheDataSource as AndroidFakeEventCacheDataSourceImpl
            networkDataSource =
                updateEventViewModel!!.updateEvent.eventNetworkDataSource as AndroidFakeEventNetworkDataSourceImpl
        }
        cacheDataSource!!.events.add(event)
        networkDataSource!!.events.add(event)
        //for unknown reason it wont click on btn
//        onView(withId(R.id.btnUpdateEvent)).perform(click())
        viewModel!!.attemptToUpdateEvent()
        viewModel!!.updateRequestResponse.getOrAwaitValue().peekContent()
        verify(navController).popBackStack()
    }
}
