package com.example.programmingmeetups.business.domain.util

sealed class Resource<out T> {
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: String? = null) : Resource<Nothing>()
}