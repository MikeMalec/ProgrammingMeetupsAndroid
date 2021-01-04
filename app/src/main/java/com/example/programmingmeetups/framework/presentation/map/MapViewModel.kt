package com.example.programmingmeetups.framework.presentation.map

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.map.FetchEvents
import com.example.programmingmeetups.business.interactors.event.map.SynchronizeProgrammingEvents
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import com.example.programmingmeetups.framework.utils.LOCATION_MANAGER_IMPL
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Named

class MapViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository,
    @Named(LOCATION_MANAGER_IMPL) private val locationManager: LocationManagerInterface,
    private val fetchEvents: FetchEvents,
    @Named(IO_DISPATCHER) private val dispatcher: CoroutineDispatcher
) :
    ViewModel() {

    var cameraPosition: CameraPosition? = null

    val position = locationManager.position

    fun requestPosition() = locationManager.getLocation()

    private lateinit var token: String

    private val _events: MutableLiveData<List<ProgrammingEvent>> = MutableLiveData()

    val events: LiveData<List<ProgrammingEvent>> = _events

    private val uniqueEvents = mutableSetOf<ProgrammingEvent>()

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect { token = it!! }
        }
    }

    fun fetchEvents(position: LatLng, radius: Double) {
        viewModelScope.launch(dispatcher) {
            val response = fetchEvents.fetchEvents(token, position, radius, dispatcher)
            if (response is Resource.Success) {
                response.data?.also {
                    dispatchEvents(it)
                }
            }
        }
    }

    private fun dispatchEvents(events: List<ProgrammingEvent>) {
        val newEvents = events.filter { new ->
            uniqueEvents.firstOrNull { old -> old.id == new.id } == null
        }
        uniqueEvents.addAll(newEvents)
        _events.postValue(newEvents)
    }

    override fun onCleared() {
        super.onCleared()
        locationManager.stop()
    }

    fun stop() {
        locationManager.stop()
        uniqueEvents.clear()
    }
}