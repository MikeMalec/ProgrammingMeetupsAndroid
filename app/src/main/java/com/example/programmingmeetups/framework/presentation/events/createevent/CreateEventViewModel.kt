package com.example.programmingmeetups.framework.presentation.events.createevent

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Loading
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.example.programmingmeetups.di.RequestBodyFactoryImplementation
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventDto
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudViewModel
import com.example.programmingmeetups.utils.FILL_IN_ALL_FIELDS
import com.example.programmingmeetups.utils.IO_DISPATCHER
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Named

class CreateEventViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository,
    @RequestBodyFactoryImplementation private val requestBodyFactory: RequestBodyFactoryInterface,
    private val createEvent: CreateEvent,
    private val eventValidator: EventValidator,
    @Named(IO_DISPATCHER) private val dispatcher: CoroutineDispatcher
) : EventCrudViewModel() {

    private lateinit var token: String

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect { token = it!! }
        }
    }

    private val programmingEvent = ProgrammingEvent()

    private val _event: MutableLiveData<ProgrammingEvent> = MutableLiveData(programmingEvent)

    val event: LiveData<ProgrammingEvent> = _event

    private val _validationMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val validationMessage: LiveData<Event<String>> = _validationMessage

    private val _createEventRequestResponse: MutableLiveData<Event<Resource<ProgrammingEvent?>>> =
        MutableLiveData()
    val createEventRequestResponse: LiveData<Event<Resource<ProgrammingEvent?>>> =
        _createEventRequestResponse

    private fun setEvent() {
        _event.postValue(programmingEvent)
    }

    fun setCoordinates(
        latitude: Double,
        longitude: Double
    ) {
        programmingEvent.latitude = latitude
        programmingEvent.longitude = longitude
    }

    fun setAddress(address: String) {
        programmingEvent.address = address
        setEvent()
    }

    override fun setDate(date: Long) {
        programmingEvent.happensAt = date
        setEvent()
    }

    override fun addTag(tag: String) {
        programmingEvent.tags?.add(tag)
        setEvent()
    }

    override fun removeTag(tag: String) {
        programmingEvent.tags?.remove(tag)
        setEvent()
    }

    private var imageUri: Uri? = null

    override fun setImage(image: Uri) {
        imageUri = image
        programmingEvent.image = image.toString()
        setEvent()
    }

    private var iconUri: Uri? = null

    override fun setIcon(icon: Uri) {
        iconUri = icon
        programmingEvent.icon = icon.toString()
        setEvent()
    }

    override fun setDescription(description: String) {
        programmingEvent.description = description
    }

    fun attemptToCreateEvent() {
        eventValidator.validateEvent(programmingEvent, ::createEvent) {
            _validationMessage.postValue(
                Event(FILL_IN_ALL_FIELDS)
            )
        }
    }

    fun createEvent() {
        _createEventRequestResponse.value = Event(Loading(null))
        viewModelScope.launch(dispatcher) {
            val latitude =
                requestBodyFactory.createTextRequestBody(programmingEvent.latitude.toString())
            val longitude =
                requestBodyFactory.createTextRequestBody(programmingEvent.longitude.toString())
            val address = requestBodyFactory.createTextRequestBody(programmingEvent.address!!)
            val date =
                requestBodyFactory.createTextRequestBody(programmingEvent.happensAt!!.toString())
            val tags =
                requestBodyFactory.createTextRequestBody(
                    (programmingEvent.tags ?: listOf<String>()).joinToString(",")
                )
            val image = requestBodyFactory.createImageRequestBody(imageUri!!, true)
            val icon = requestBodyFactory.createImageRequestBody(iconUri!!, false)
            val description =
                requestBodyFactory.createTextRequestBody(programmingEvent.description!!)
            createEvent.createEvent(
                token,
                image,
                icon,
                latitude,
                longitude,
                address,
                date,
                tags,
                description,
                dispatcher
            ).collect {
                _createEventRequestResponse.postValue(Event(it))
            }
        }
    }
}