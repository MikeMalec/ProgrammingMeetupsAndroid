package com.example.programmingmeetups.framework.presentation.events.userevents

import android.util.Log
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
    val getUserEvents: GetUserEvents,
    @Named(IO_DISPATCHER) private var dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _userEvents: MutableLiveData<List<ProgrammingEvent>> = MutableLiveData()
    val userEvents: LiveData<List<ProgrammingEvent>> = _userEvents

    private val fetchedEvents = mutableListOf<ProgrammingEvent>()
    private var lastHappensAt = 0L

    fun fetchEvents() {
        viewModelScope.launch(dispatcher) {
            val events = getUserEvents.getUserEvents(lastHappensAt)
            if (events.isNotEmpty()) {
                lastHappensAt = events[events.size - 1].happensAt!!
            }
            fetchedEvents.addAll(events)
            _userEvents.postValue(fetchedEvents)
        }
    }

    fun reset() {
        lastHappensAt = 0
        fetchedEvents.clear()
        _userEvents.value = fetchedEvents
    }
}