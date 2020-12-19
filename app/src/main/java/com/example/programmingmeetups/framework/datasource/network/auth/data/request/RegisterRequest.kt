package com.example.programmingmeetups.framework.datasource.network.auth.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)