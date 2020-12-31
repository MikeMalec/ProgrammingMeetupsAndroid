package com.example.programmingmeetups.framework.presentation.auth

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.auth.FakeAuthRepository
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource.Error
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.business.interactors.auth.Login
import com.example.programmingmeetups.business.interactors.auth.Register
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.FakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidator
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import com.example.programmingmeetups.framework.utils.ERROR_UNKNOWN
import com.example.programmingmeetups.framework.utils.FILL_IN_ALL_FIELDS
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import com.google.common.truth.Truth.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.File

class AuthViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var authViewModel: AuthViewModel
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var authValidator: AuthValidator
    private lateinit var authInteractors: AuthInteractors
    private lateinit var login: Login
    private lateinit var register: Register
    private lateinit var fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    private lateinit var fakeRequestBodyFactoryImpl: FakeRequestBodyFactoryImpl

    @Before
    fun setup() {
        fakeAuthRepository = FakeAuthRepository()
        authValidator = AuthValidator()
        login = Login(fakeAuthRepository)
        register = Register(fakeAuthRepository)
        authInteractors = AuthInteractors(register, login)
        fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
        fakeRequestBodyFactoryImpl = FakeRequestBodyFactoryImpl()
        fakeRequestBodyFactoryImpl.file = mock(File::class.java)
        authViewModel =
            AuthViewModel(
                fakePreferencesRepositoryImpl,
                authInteractors,
                authValidator,
                fakeRequestBodyFactoryImpl,
                mainCoroutineRule.dispatcher
            )
    }

    @Test
    fun successfully_validates_registration() = runBlockingTest {
        val uri = mock(Uri::class.java)
        authViewModel.attemptRegister(
            uri,
            RegisterRequest(
                "firstName",
                "lastName",
                "email",
                "password"
            )
        )

        val registerRequestResponse = authViewModel.registerRequestResponse.getOrAwaitValueTest()
        assertThat(registerRequestResponse.getContent()).isEqualTo(
            Success(
                AuthResponse(
                    token = "token",
                    user = User(
                        firstName = "firstName",
                        lastName = "lastName",
                        email = "email",
                        image = "image",
                        description = "",
                        id = "id"
                    )
                )
            )
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo("token")
    }

    @Test
    fun register_fails_validation() {
        val uri = mock(Uri::class.java)
        authViewModel.attemptRegister(
            uri,
            RegisterRequest(
                "",
                "lastName",
                "email",
                "password"
            )
        )
        val registerValidationMessage =
            authViewModel.registerValidationMessage.getOrAwaitValueTest()
        assertThat(registerValidationMessage.getContent()).isEqualTo(
            FILL_IN_ALL_FIELDS
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo(null)
    }

    @Test
    fun register_throws_exception_shows_error_unknown() = runBlockingTest {
        fakeAuthRepository.throwsException = true
        val uri = mock(Uri::class.java)
        authViewModel.attemptRegister(
            uri,
            RegisterRequest(
                "firstName",
                "lastName",
                "email",
                "password"
            )
        )
        val registerRequestResponse = authViewModel.registerRequestResponse.getOrAwaitValueTest()
        assertThat(registerRequestResponse.getContent()).isEqualTo(
            Error(ERROR_UNKNOWN)
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo(null)
    }

    @Test
    fun successfully_validates_login() {
        authViewModel.attemptLogin(LoginRequest("email", "password"))
        val loginRequestResponse = authViewModel.loginRequestResponse.getOrAwaitValueTest()
        assertThat(loginRequestResponse.getContent()).isEqualTo(
            Success(
                AuthResponse(
                    token = "token",
                    user = User(
                        firstName = "firstName",
                        lastName = "lastName",
                        email = "email",
                        image = "image",
                        description = "",
                        id = "id"
                    )
                )
            )
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo("token")
    }

    @Test
    fun login_validation_fails() {
        authViewModel.attemptLogin(LoginRequest("", "password"))
        val loginValidationMessage = authViewModel.loginValidationMessage.getOrAwaitValueTest()
        assertThat(loginValidationMessage.getContent()).isEqualTo(
            FILL_IN_ALL_FIELDS
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo(null)
    }

    @Test
    fun login_throws_exceptions_shows_error_unkown() {
        fakeAuthRepository.throwLoginError = true
        authViewModel.attemptLogin(LoginRequest("email", "password"))
        val loginRequestResponse = authViewModel.loginRequestResponse.getOrAwaitValueTest()
        assertThat(loginRequestResponse.getContent()).isEqualTo(
            Error(ERROR_UNKNOWN)
        )
        assertThat(authViewModel.token.getOrAwaitValueTest()).isEqualTo(null)
    }
}