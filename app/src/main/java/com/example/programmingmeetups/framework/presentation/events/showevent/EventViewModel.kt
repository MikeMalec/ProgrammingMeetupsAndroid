package com.example.programmingmeetups.framework.presentation.events.showevent

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.business.interactors.event.getamountofeventusers.GetAmountOfEventUsers
import com.example.programmingmeetups.business.interactors.event.isParticipant.IsParticipant
import com.example.programmingmeetups.business.interactors.event.join.JoinEvent
import com.example.programmingmeetups.business.interactors.event.leave.LeaveEvent
import com.example.programmingmeetups.framework.datasource.network.event.EventUserPaginator
import com.example.programmingmeetups.framework.datasource.network.event.UserPaginator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Named

class EventViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) val preferencesRepository: PreferencesRepository,
    val isParticipant: IsParticipant,
    val joinEvent: JoinEvent,
    val leaveEvent: LeaveEvent,
    val getAmountOfEventUsers: GetAmountOfEventUsers,
    val eventUserPaginator: EventUserPaginator,
    @Named(IO_DISPATCHER)
    var dispatcher: CoroutineDispatcher
) : ViewModel(), UserPaginator by eventUserPaginator {
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

    private val _eventActionButtonState: MutableLiveData<EventAction> = MutableLiveData()
    val eventActionButtonState: LiveData<EventAction> = _eventActionButtonState

    private val _amountOfParticipants: MutableLiveData<Int> = MutableLiveData(0)
    val amountOfParticipants = _amountOfParticipants

    fun fetchAmountOfParticipants() {
        viewModelScope.launch(dispatcher) {
            val response =
                getAmountOfEventUsers.getAmountOfEventUsers(token!!, event.value!!.id!!, dispatcher)
            if (response is Success) {
                response.data?.also { _amountOfParticipants.postValue(it.amount) }
            }
        }
    }

    fun setUser() {
        viewModelScope.launch {
            preferencesRepository.getUserInfo().collect {
                user = it
            }
        }
    }

    fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect {
                token = it
            }
        }
    }

    fun setEvent(event: ProgrammingEvent) {
        _event.value = event
        if (user!!.id == event.organizer!!.id) {
            _eventActionButtonState.value = EventAction.EditEvent
        }
    }

    fun checkIfUserIsParticipant() {
        if (eventActionButtonState.value == null) {
            viewModelScope.launch(dispatcher) {
                val response = isParticipant.isParticipant(
                    token!!,
                    event.value!!.id!!,
                    dispatcher
                )
                if (response is Success) {
                    val data = response.data
                    data?.also {
                        if (it.isParticipant) {
                            _eventActionButtonState.postValue(EventAction.LeaveEvent)
                        } else {
                            _eventActionButtonState.postValue(EventAction.JoinEvent)
                        }
                    }
                }
            }
        }
    }

    fun joinEvent() {
        _loading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            joinEvent.joinEvent(event.value!!.id!!, "$token", dispatcher).collect {
                dispatchEventResponse(it, true)
            }
        }
    }

    fun leaveEvent() {
        _loading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            leaveEvent.leaveEvent(event.value!!.id!!, "$token", dispatcher).collect {
                dispatchEventResponse(it, false)
            }
        }
    }

    private fun dispatchEventResponse(response: Resource<ProgrammingEvent?>, join: Boolean) {
        when (response) {
            is Success -> {
                when (join) {
                    true -> {
                        var amount = amountOfParticipants.value!!
                        amount++
                        _amountOfParticipants.postValue(amount)
                        _eventActionButtonState.postValue(EventAction.LeaveEvent)
                    }
                    false -> {
                        var amount = amountOfParticipants.value!!
                        amount--
                        _amountOfParticipants.postValue(amount)
                        _eventActionButtonState.postValue(EventAction.JoinEvent)
                    }
                }
                eventUserPaginator.reset()
                _loading.postValue(Event(false))
            }
            is Error -> {
                response.error?.let { _responseError.postValue(Event(it)) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventUserPaginator.stop()
    }
}