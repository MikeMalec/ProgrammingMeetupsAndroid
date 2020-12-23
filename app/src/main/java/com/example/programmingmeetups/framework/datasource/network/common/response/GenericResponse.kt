package com.example.programmingmeetups.framework.datasource.network.common.response

data class GenericResponse(
    val success: Boolean? = null,
    val error: String? = null,
    val message: String? = null
)