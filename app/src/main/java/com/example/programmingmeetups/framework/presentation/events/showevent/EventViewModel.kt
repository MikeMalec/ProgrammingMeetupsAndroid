package com.example.programmingmeetups.framework.presentation.events.showevent

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.join.JoinEvent
import com.example.programmingmeetups.business.interactors.event.leave.LeaveEvent
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Named

class EventViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) val preferencesRepository: PreferencesRepository,
    val joinEvent: JoinEvent,
    val leaveEvent: LeaveEvent,
    @Named(IO_DISPATCHER) var dispatcher: CoroutineDispatcher
) : ViewModel() {
    var user: User? = null
    var token: String? = null

    init {
        setUser()
        setToken()
    }

    private val _responseError: MutableLiveData<Event<String>> = MutableLiveData()
    val responseError = _responseError

    private val _event: MutableLiveData<ProgrammingEvent> = MutableLiveData()
    val event: LiveData<ProgrammingEvent> = _event

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: LiveData<Event<Boolean>> = _loading

    fun getUserEventRelation(): String {
        val event = event.value
        val participants = event?.participants
        if (user != null && event != null) {
            if (event.organizer!!.id == user!!.id) {
                return EDIT_EVENT
            } else {
                val existsInParticipants = participants!!.firstOrNull { it.id == user!!.id }
                if (existsInParticipants != null) {
                    return LEAVE_EVENT
                } else {
                    return JOIN_EVENT
                }
            }
        }
        return JOIN_EVENT
    }

    fun setUser() {
        viewModelScope.launch(dispatcher) {
            preferencesRepository.getUserInfo().collect {
                user = it
            }
        }
    }

    fun setToken() {
        viewModelScope.launch(dispatcher) {
            preferencesRepository.getToken().collect {
                token = it
            }
        }
    }

    fun setEvent(event: ProgrammingEvent) {
        _event.value = event
    }

    fun joinEvent() {
        _loading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            joinEvent.joinEvent(event.value!!.id!!, "$token", dispatcher).collect {
                dispatchEventResponse(it)
            }
        }
    }

    fun leaveEvent() {
        _loading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            leaveEvent.leaveEvent(event.value!!.id!!, "$token", dispatcher).collect {
                dispatchEventResponse(it)
            }
        }
    }

    private fun dispatchEventResponse(response: Resource<ProgrammingEvent?>) {
        when (response) {
            is Resource.Success -> _event.postValue(response.data)
            is Resource.Error -> {
                response.error?.let { _responseError.postValue(Event(it)) }
            }
        }
    }
}