package com.example.programmingmeetups.framework.presentation.events.eventcomments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.event.FakeEventNetworkDataSource
import com.example.programmingmeetups.business.interactors.event.getcomments.GetEventComments
import com.example.programmingmeetups.framework.datasource.network.event.sockets.FakeCommentSocketManager
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.example.programmingmeetups.framework.utils.EventCommentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EventCommentsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var eventCommentsViewModel: EventCommentsViewModel
    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var fakeCommentSocketManager: FakeCommentSocketManager
    private lateinit var fakeEventNetworkDataSource: FakeEventNetworkDataSource
    private lateinit var getEventComments: GetEventComments
    private val eventCommentFactory = EventCommentFactory()

    @Before
    fun setup() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        fakePreferencesRepository.token = "token"
        fakeCommentSocketManager = FakeCommentSocketManager()
        fakeEventNetworkDataSource = FakeEventNetworkDataSource()
        getEventComments = GetEventComments(fakeEventNetworkDataSource)
        eventCommentsViewModel = EventCommentsViewModel(
            fakePreferencesRepository,
            fakeCommentSocketManager,
            getEventComments,
            mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun getTokenFromSockets_success_confirmListContainsComment() {
        eventCommentsViewModel.sendComment("id", "comment")
        val comments = eventCommentsViewModel.comments.getOrAwaitValueTest()
        assertThat(comments[0].comment).isEqualTo("comment")
    }

    @Test
    fun paginate_success_confirmThatListContainsCommentsAndPaginatedDataAddedInRightOrder() {
        fakeEventNetworkDataSource.comments = eventCommentFactory.createComments(20)
        eventCommentsViewModel.sendComment("id", "comment")
        eventCommentsViewModel.fetchComments("id")
        eventCommentsViewModel.fetchComments("id")
        val comments = eventCommentsViewModel.comments.getOrAwaitValueTest()
        assertThat(comments.size).isEqualTo(21)
        assertThat(comments[0].comment).isEqualTo("comment")
    }
}