package com.example.programmingmeetups.business.data.network.auth

import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Response

class AndroidFakeAuthRepository : AuthRepository {

    var throwRegisterError = false
    var registerSuccessfully = true

    override suspend fun register(
        image: MultipartBody.Part,
        firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): AuthResponse {
        if (throwRegisterError) throw Exception()
        if (registerSuccessfully == true) {
            val firstNameValue = Buffer()
            firstName.writeTo(firstNameValue)
            val lastNameValue = Buffer()
            lastName.writeTo(lastNameValue)
            val emailValue = Buffer()
            email.writeTo(emailValue)
            val passwordValue = Buffer()
            password.writeTo(passwordValue)
            return AuthResponse(
                token = "token",
                user = User(
                    firstName = firstNameValue.readUtf8(),
                    lastName = lastNameValue.readUtf8(),
                    email = emailValue.readUtf8(),
                    image = "image",
                    description = "",
                    id = "id"
                )
            )
        } else {
            throw Exception()
        }
    }

        var throwLoginError = false
        var loginSuccessfully = true

        override suspend fun login(loginRequest: LoginRequest): AuthResponse {
            if (throwLoginError) throw Exception()
            if (loginSuccessfully) {
                return AuthResponse(
                    token = "token", user =
                    User(
                        firstName = "firstName",
                        lastName = "lastName",
                        email = loginRequest.email,
                        image = "image",
                        description = "",
                        id = "id"
                    )
                )
            } else {
                throw Exception()
            }
        }
    }