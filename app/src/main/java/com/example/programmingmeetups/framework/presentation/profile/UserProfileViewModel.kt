package com.example.programmingmeetups.framework.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.business.interactors.auth.UpdateProfile
import com.example.programmingmeetups.di.RequestBodyFactoryImplementation
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.utils.IO_DISPATCHER
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import com.example.programmingmeetups.utils.REQUEST_BODY_FACTORY_IMPL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Named

class UserProfileViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository,
    @RequestBodyFactoryImplementation private val requestBodyFactory: RequestBodyFactoryInterface,
    private val updateProfile: UpdateProfile,
    @Named(IO_DISPATCHER) private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private lateinit var token: String

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect { it?.also { token = it } }
        }
    }

    val userProfile = preferencesRepository.getUserInfo().asLiveData()
    var imageUri: Uri? = null

    private val _updateResponse: MutableLiveData<Event<Resource<AuthResponse?>>> = MutableLiveData()
    val updateResponse: LiveData<Event<Resource<AuthResponse?>>> = _updateResponse

    fun updateProfile(description: String) {
        val dsc = requestBodyFactory.createTextRequestBody(description)
        var image: MultipartBody.Part? = null
        if (imageUri != null) {
            image = requestBodyFactory.createImageRequestBody(imageUri!!)
        }
        viewModelScope.launch(dispatcher) {
            updateProfile.updateProfile(token, dsc, image, dispatcher).collect { response ->
                if (response is Success) {
                    response.data?.user?.also { preferencesRepository.saveUserInfo(it) }
                }
                _updateResponse.postValue(Event(response))
            }
        }
    }
}