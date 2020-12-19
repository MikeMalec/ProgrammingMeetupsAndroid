package com.example.programmingmeetups.framework.presentation.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.programmingmeetups.business.domain.util.Event
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel
constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authInteractors: AuthInteractors,
    private val authValidator: AuthValidator,
    private val requestBodyFactory: RequestBodyFactoryInterface,
    private val dispatcher: CoroutineDispatcher
) :
    ViewModel() {
    val token get() = preferencesRepository.getToken().asLiveData()

    private val _registerLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val registerLoading: LiveData<Event<Boolean>> = _registerLoading

    private val _registerRequestResponse: MutableLiveData<Event<Resource<AuthResponse?>>> =
        MutableLiveData()
    val registerRequestResponse: LiveData<Event<Resource<AuthResponse?>>>
        get() = _registerRequestResponse

    private val _loginLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loginLoading: LiveData<Event<Boolean>> = _loginLoading

    private val _loginRequestResponse: MutableLiveData<Event<Resource<AuthResponse?>>> =
        MutableLiveData()
    val loginRequestResponse: LiveData<Event<Resource<AuthResponse?>>>
        get() = _loginRequestResponse

    private val _registerValidationMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val registerValidationMessage: LiveData<Event<String>>
        get() = _registerValidationMessage

    private val _loginValidationMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val loginValidationMessage: LiveData<Event<String>>
        get() = _loginValidationMessage

    fun attemptRegister(image: Uri?, registerRequest: RegisterRequest) {
        authValidator.validateRegisterRequest(image, registerRequest, { req ->
            register(image!!, req)
        }) { errMsg -> _registerValidationMessage.value = Event(errMsg) }
    }

    fun register(uri: Uri, registerRequest: RegisterRequest) {
        _registerLoading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            val firstName = requestBodyFactory.createTextRequestBody(registerRequest.firstName)
            val lastName = requestBodyFactory.createTextRequestBody(registerRequest.lastName)
            val email = requestBodyFactory.createTextRequestBody(registerRequest.email)
            val password = requestBodyFactory.createTextRequestBody(registerRequest.password)
            val image = requestBodyFactory.createImageRequestBody(uri)
            authInteractors.register.register(
                image,
                firstName,
                lastName,
                email,
                password,
                dispatcher
            )
                .collect {
                    dispatchAuthResponse(it)
                    _registerRequestResponse.postValue(Event(it))
                }
        }
    }

    fun attemptLogin(loginRequest: LoginRequest) {
        authValidator.validateLoginRequest(loginRequest, { req ->
            login(req)
        }) { errMsg -> _loginValidationMessage.value = Event(errMsg) }
    }

    fun login(loginRequest: LoginRequest) {
        _loginLoading.value = Event(true)
        viewModelScope.launch(dispatcher) {
            authInteractors.login.login(loginRequest, dispatcher).collect {
                dispatchAuthResponse(it)
                _loginRequestResponse.postValue(Event(it))
            }
        }
    }

    private suspend fun dispatchAuthResponse(authResponse: Resource<AuthResponse?>) {
        if (authResponse is Success) {
            authResponse.data?.token?.also { token ->
                preferencesRepository.saveToken(token)
            }
            authResponse.data?.user?.also { userInfo ->
                preferencesRepository.saveUserInfo(userInfo)
            }
        }
    }
}