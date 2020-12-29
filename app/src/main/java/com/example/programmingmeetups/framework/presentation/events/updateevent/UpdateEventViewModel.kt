package com.example.programmingmeetups.framework.presentation.events.updateevent

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.interactors.event.deleteevent.DeleteEvent
import com.example.programmingmeetups.business.interactors.event.update.UpdateEvent
import com.example.programmingmeetups.di.RequestBodyFactoryImplementation
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.common.response.GenericResponse
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.events.common.EventCrudViewModel
import com.example.programmingmeetups.utils.FILL_IN_ALL_FIELDS
import com.example.programmingmeetups.utils.IO_DISPATCHER
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import com.example.programmingmeetups.utils.REQUEST_BODY_FACTORY_IMPL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Named

class UpdateEventViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) val preferencesRepository: PreferencesRepository,
    @RequestBodyFactoryImplementation val requestBodyFactory: RequestBodyFactoryInterface,
    val eventValidator: EventValidator,
    val updateEvent: UpdateEvent,
    val deleteEvent: DeleteEvent,
    @Named(IO_DISPATCHER)
    val dispatcher: CoroutineDispatcher
) : EventCrudViewModel() {

    private lateinit var token: String

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect {
                it?.also { token = it }
            }
        }
    }

    lateinit var programmingEvent: ProgrammingEvent

    private val _event: MutableLiveData<ProgrammingEvent> = MutableLiveData()

    val event: LiveData<ProgrammingEvent> = _event


    private val _validationMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val validationMessage: LiveData<Event<String>> = _validationMessage

    private val _updateRequestResponse: MutableLiveData<Event<Resource<ProgrammingEvent?>>> =
        MutableLiveData()
    val updateRequestResponse: LiveData<Event<Resource<ProgrammingEvent?>>> = _updateRequestResponse

    private val _deleteRequestResponse: MutableLiveData<Event<Resource<GenericResponse?>>> =
        MutableLiveData()
    val deleteRequestResponse: LiveData<Event<Resource<GenericResponse?>>> = _deleteRequestResponse

    fun setEvent() {
        _event.postValue(programmingEvent)
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
    }

    override fun setDescription(description: String) {
        programmingEvent.description = description
    }

    var imageUri: Uri? = null
    override fun setImage(uri: Uri) {
        imageUri = uri
    }

    var iconUri: Uri? = null
    override fun setIcon(uri: Uri) {
        iconUri = uri
    }

    fun attemptToUpdateEvent() {
        eventValidator.validateEvent(programmingEvent, ::updateEvent) {
            _validationMessage.postValue(
                Event(FILL_IN_ALL_FIELDS)
            )
        }
    }

    private fun updateEvent() {
        viewModelScope.launch(dispatcher) {
            val happensAt =
                requestBodyFactory.createTextRequestBody(programmingEvent.happensAt.toString())

            val tags =
                requestBodyFactory.createTextRequestBody(
                    (programmingEvent.tags ?: listOf<String>()).joinToString(",")
                )

            val description =
                requestBodyFactory.createTextRequestBody(programmingEvent.description!!)
            var image: MultipartBody.Part? = null
            if (imageUri != null) {
                image = requestBodyFactory.createImageRequestBody(imageUri!!, true)
            }
            var icon: MultipartBody.Part? = null
            if (iconUri != null) {
                icon = requestBodyFactory.createImageRequestBody(iconUri!!, false)
            }
            updateEvent.updateEvent(
                token,
                programmingEvent.id!!,
                happensAt,
                tags,
                description,
                image,
                icon,
                dispatcher
            ).collect {
                _updateRequestResponse.postValue(Event(it))
            }
        }
    }

    fun deleteEvent() {
        viewModelScope.launch(dispatcher) {
            deleteEvent.deleteEvent(token, programmingEvent, dispatcher).collect {
                _deleteRequestResponse.postValue(Event(it))
            }
        }
    }
}