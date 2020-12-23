package com.example.programmingmeetups.framework.datasource.network.auth.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AuthResponse(
    val token: String? = null,
    val error: String? = null,
    val user: User? = null
)

@Parcelize
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val description: String,
    val image: String
) : Parcelable
