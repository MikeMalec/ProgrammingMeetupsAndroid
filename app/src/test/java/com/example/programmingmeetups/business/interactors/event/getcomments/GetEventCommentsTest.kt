package com.example.programmingmeetups.business.interactors.event.getcomments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.framework.datasource.network.event.model.EventCommentResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import com.example.programmingmeetups.framework.utils.EventCommentFactory
import kotlinx.coroutines.test.runBlockingTest
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class GetEventCommentsTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var getEventComments: GetEventComments
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private val eventCommentFactory = EventCommentFactory()

    @Before
    fun setup() {
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        getEventComments = GetEventComments(fakeEventNetworkDataSource)
    }

    @Test
    fun getEventWithPagination_success_confirmEventsFetched() = runBlockingTest {
        val fetchedComments = mutableListOf<ProgrammingEventCommentDto>()
        val comments = eventCommentFactory.createComments(20)
        fakeEventNetworkDataSource.comments = comments
        getEventComments.getEventComments("token", "id", 1, mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<EventCommentResponse?>> {
                override suspend fun emit(value: Resource<EventCommentResponse?>) {
                    if (value is Resource.Success) {
                        fetchedComments.addAll(value.data!!.comments)
                    }
                }
            })
        getEventComments.getEventComments("token", "id", 2, mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<EventCommentResponse?>> {
                override suspend fun emit(value: Resource<EventCommentResponse?>) {
                    if (value is Resource.Success) {
                        fetchedComments.addAll(value.data!!.comments)
                    }
                }
            })
        assertThat(fetchedComments.size).isEqualTo(20)
    }
}