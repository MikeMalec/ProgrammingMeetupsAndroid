package com.example.programmingmeetups.framework.datasource.network.auth.utils

import android.net.Uri
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AuthValidatorTest {
    private var authValidator: AuthValidator? = null

    @Before
    fun setup() {
        authValidator = AuthValidator()
    }

    @Test
    fun valid_register_input_runs_success_callback() {
        val uri = mock(Uri::class.java)
        var success: Boolean? = null
        val successCallback: (registerRequest: RegisterRequest) -> Unit = { registerRequest ->
            assertThat(registerRequest).isEqualTo(
                RegisterRequest(
                    "firstName",
                    "lastName",
                    "email",
                    "password"
                )
            )
            success = true
        }
        val errorCallback: (message: String) -> Unit = {
            success = false
        }
        authValidator!!.validateRegisterRequest(
            uri,
            RegisterRequest("firstName", "lastName", "email", "password"),
            successCallback = successCallback,
            errorCallback = errorCallback
        )
        assertThat(success).isEqualTo(true)
    }

    @Test
    fun invalid_register_input_runs_error_callback() {
        val uri = mock(Uri::class.java)
        var success: Boolean? = null
        val successCallback: (registerRequest: RegisterRequest) -> Unit = { registerRequest ->
            success = true
        }
        val errorCallback: (message: String) -> Unit = {
            success = false
        }
        authValidator!!.validateRegisterRequest(
            uri,
            RegisterRequest("", "lastName", "email", "password"),
            successCallback = successCallback,
            errorCallback = errorCallback
        )
        assertThat(success).isEqualTo(false)
    }

    @Test
    fun valid_login_input_runs_success_callback() {
        var success: Boolean? = null
        val successCallback: (loginRequest: LoginRequest) -> Unit = { loginRequest ->
            assertThat(loginRequest).isEqualTo(
                LoginRequest(
                    "email",
                    "password"
                )
            )
            success = true
        }
        val errorCallback: (message: String) -> Unit = {
            success = false
        }
        authValidator!!.validateLoginRequest(
            LoginRequest( "email", "password"),
            successCallback = successCallback,
            errorCallback = errorCallback
        )
        assertThat(success).isEqualTo(true)
    }

    @Test
    fun invalid_login_input_runs_success_callback() {
        var success: Boolean? = null
        val successCallback: (loginRequest: LoginRequest) -> Unit = { loginRequest ->
            success = true
        }
        val errorCallback: (message: String) -> Unit = {
            success = false
        }
        authValidator!!.validateLoginRequest(
            LoginRequest( "email", ""),
            successCallback = successCallback,
            errorCallback = errorCallback
        )
        assertThat(success).isEqualTo(false)
    }
}