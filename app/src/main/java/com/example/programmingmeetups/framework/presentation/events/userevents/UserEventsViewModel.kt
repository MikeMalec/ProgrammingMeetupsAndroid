package com.example.programmingmeetups.framework.presentation.events.userevents

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.interactors.event.user.GetUserEvents
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Named

class UserEventsViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) val preferencesRepository: PreferencesRepository,
    val getUserEvents: GetUserEvents,
    @Named(IO_DISPATCHER) private var dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _userEvents: MutableLiveData<List<ProgrammingEvent>> = MutableLiveData()
    val userEvents: LiveData<List<ProgrammingEvent>> = _userEvents

    init {
        setEvents()
    }

    fun setEvents() {
        viewModelScope.launch(dispatcher) {
            preferencesRepository.getUserInfo().map { user ->
                user?.id
            }.collect { userId ->
                userId?.also { _userEvents.postValue(getUserEvents.getUserEvents(userId)) }
            }
        }
    }
}