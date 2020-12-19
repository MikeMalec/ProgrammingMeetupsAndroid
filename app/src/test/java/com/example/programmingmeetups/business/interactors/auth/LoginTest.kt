package com.example.programmingmeetups.business.interactors.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.auth.FakeAuthRepository
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.business.domain.util.Resource.*
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.*

@InternalCoroutinesApi
class LoginTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    var fakeAuthRepository: FakeAuthRepository? = null

    var login: Login? = null

    @Before
    fun setup() {
        fakeAuthRepository = FakeAuthRepository()
        login = Login(fakeAuthRepository!!)
    }

    @Test
    fun login_success_returns_token_and_user_info() = runBlockingTest {
        login!!.login(LoginRequest("email", "password"), mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<AuthResponse?>> {
                override suspend fun emit(value: Resource<AuthResponse?>) {
                    assertThat(value).isEqualTo(
                        Success<AuthResponse?>(
                            AuthResponse(
                                token = "token", user =
                                User(
                                    firstName = "firstName",
                                    lastName = "lastName",
                                    email = "email",
                                    password = "password",
                                    image = "image",
                                    description = ""
                                )
                            )
                        )
                    )
                }
            })
    }

    @Test
    fun login_throws_error_unknown() = runBlockingTest {
        fakeAuthRepository!!.throwLoginError = true
        login!!.login(LoginRequest("email", "password"), mainCoroutineRule.dispatcher)
            .collect(object : FlowCollector<Resource<AuthResponse?>> {
                override suspend fun emit(value: Resource<AuthResponse?>) {
                    assertThat(value).isEqualTo(Error(ERROR_UNKNOWN))
                }
            })
    }
}