package com.example.programmingmeetups.framework.datasource.network.auth.data.response

import com.example.programmingmeetups.business.domain.model.User

data class AuthResponse(
    val token: String? = null,
    val error: String? = null,
    val user: User? = null
)