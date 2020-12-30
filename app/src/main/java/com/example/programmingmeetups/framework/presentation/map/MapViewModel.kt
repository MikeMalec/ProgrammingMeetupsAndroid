package com.example.programmingmeetups.framework.presentation.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.interactors.event.map.SynchronizeProgrammingEvents
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.utils.IO_DISPATCHER
import com.example.programmingmeetups.utils.LOCATION_MANAGER_IMPL
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Named

class MapViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository,
    @Named(LOCATION_MANAGER_IMPL) private val locationManager: LocationManagerInterface,
    private val synchronizeProgrammingEvents: SynchronizeProgrammingEvents,
    @Named(IO_DISPATCHER) private val dispatcher: CoroutineDispatcher
) :
    ViewModel() {

    var cameraPosition: CameraPosition? = null

    val position = locationManager.position

    fun requestPosition() = locationManager.getLocation()

    private lateinit var token: String

    private val _events: MutableLiveData<List<ProgrammingEvent>> = MutableLiveData()

    val events: LiveData<List<ProgrammingEvent>> = _events

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect { token = it!! }
        }
    }

    fun fetchEvents() {
        viewModelScope.launch(dispatcher) {
            synchronizeProgrammingEvents.synchronizeEvents(token, dispatcher)
                .collect { _events.postValue(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationManager.stop()
    }

    fun stop() {
        locationManager.stop()
    }
}