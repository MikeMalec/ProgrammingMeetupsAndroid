package com.example.programmingmeetups.framework.datasource.network.auth.utils

import android.net.Uri
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.LoginRequest
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RegisterRequest
import com.example.programmingmeetups.framework.utils.FILL_IN_ALL_FIELDS
import javax.inject.Inject

class AuthValidator @Inject constructor() {
    fun validateRegisterRequest(
        image: Uri?,
        registerRequest: RegisterRequest,
        successCallback: (registerRequest: RegisterRequest) -> Unit,
        errorCallback: (message: String) -> Unit
    ) {
        registerRequest.run {
            if (image != null && firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                successCallback(registerRequest)
            } else {
                errorCallback(FILL_IN_ALL_FIELDS)
            }
        }
    }

    fun validateLoginRequest(
        loginRequest: LoginRequest,
        successCallback: (loginRequest: LoginRequest) -> Unit,
        errorCallback: (message: String) -> Unit
    ) {
        loginRequest.run {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                successCallback(loginRequest)
            } else {
                errorCallback(FILL_IN_ALL_FIELDS)
            }
        }
    }
}