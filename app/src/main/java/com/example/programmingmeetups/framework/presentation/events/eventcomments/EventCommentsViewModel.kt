package com.example.programmingmeetups.framework.presentation.events.eventcomments

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.business.interactors.event.getcomments.GetEventComments
import com.example.programmingmeetups.framework.datasource.network.event.sockets.EventCommentSocketManagerInterface
import com.example.programmingmeetups.framework.datasource.network.event.model.EventCommentResponse
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.COMMENT_SOCKET_MANAGER_IMPL
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Named

class EventCommentsViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) val preferencesRepository: PreferencesRepository,
    @Named(COMMENT_SOCKET_MANAGER_IMPL) private val eventCommentSocketManager: EventCommentSocketManagerInterface,
    val getEventComments: GetEventComments,
    @Named(IO_DISPATCHER) val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private lateinit var token: String

    private val _comments: MutableLiveData<List<ProgrammingEventCommentDto>> = MutableLiveData()
    val comments: LiveData<List<ProgrammingEventCommentDto>> = _comments
    var fetchedComments = mutableListOf<ProgrammingEventCommentDto>()
    var pages = 1
    var page = 1

    init {
        setToken()
        collectSocketComments()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect {
                it?.also { token = it }
            }
        }
    }

    private fun collectSocketComments() {
        viewModelScope.launch(dispatcher) {
            eventCommentSocketManager.comments.consumeAsFlow().collect {
                fetchedComments.add(0, it)
                _comments.postValue(fetchedComments)
            }
        }
    }

    fun connectSocket(eventId: String) {
        eventCommentSocketManager.connect(eventId)
    }

    fun sendComment(eventId: String, comment: String) {
        eventCommentSocketManager.sendComment(token, eventId, comment)
    }

    var fetching = false
    fun fetchComments(eventId: String) {
        var cPage = page
        if (cPage <= pages && !fetching) {
            fetching = true
            page++
            viewModelScope.launch(dispatcher) {
                getEventComments.getEventComments(token, eventId, cPage, dispatcher)
                    .collect {
                        dispatchCommentResponse(it)
                    }
            }
        }
    }

    private fun dispatchCommentResponse(response: Resource<EventCommentResponse?>) {
        fetching = false
        if (response is Success) {
            response.data?.also {
                pages = it.pages
                val fetched = fetchedComments
                fetchedComments = mutableListOf(
                    *fetched.toTypedArray(), *it.comments.toTypedArray()
                )
                _comments.postValue(fetchedComments)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventCommentSocketManager.disconnect()
    }
}