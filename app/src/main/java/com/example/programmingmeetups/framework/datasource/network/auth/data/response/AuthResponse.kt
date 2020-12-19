package com.example.programmingmeetups.framework.datasource.network.auth.data.response

data class AuthResponse(
    val token: String? = null,
    val error: String? = null,
    val user: User? = null
)

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val description: String,
    val image: String
)
