package com.example.programmingmeetups.business.interactors.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.auth.FakeAuthRepository
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RegisterTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    var register: Register? = null

    var fakeAuthRepository: FakeAuthRepository? = null

    @Before
    fun setup() {
        fakeAuthRepository = FakeAuthRepository()
        register = Register(fakeAuthRepository!!)
    }

    @Test
    fun register_success_returns_token_and_user_info() = runBlockingTest {
        register!!.register(
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            RequestBody.create(MediaType.parse("text/plain"), "firstName"),
            RequestBody.create(MediaType.parse("text/plain"), "lastName"),
            RequestBody.create(MediaType.parse("text/plain"), "email"),
            RequestBody.create(MediaType.parse("text/plain"), "password"),
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<AuthResponse?>> {
            override suspend fun emit(value: Resource<AuthResponse?>) {
                assertThat(value).isEqualTo(
                    Resource.Success<AuthResponse?>(
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
            }
        })
    }

    @Test
    fun register_throws_error_unknown() = runBlockingTest {
        fakeAuthRepository!!.throwsException = true
        register!!.register(
            MultipartBody.Part.createFormData(
                "image", "test", RequestBody.create(
                    MediaType.parse("image/*"), File("")
                )
            ),
            RequestBody.create(MediaType.parse("text/plain"), "firstName"),
            RequestBody.create(MediaType.parse("text/plain"), "lastName"),
            RequestBody.create(MediaType.parse("text/plain"), "email"),
            RequestBody.create(MediaType.parse("text/plain"), "password"),
            mainCoroutineRule.dispatcher
        ).collect(object : FlowCollector<Resource<AuthResponse?>> {
            override suspend fun emit(value: Resource<AuthResponse?>) {
                assertThat(value).isEqualTo(
                    Resource.Error(ERROR_UNKNOWN)
                )
            }
        })
    }
}