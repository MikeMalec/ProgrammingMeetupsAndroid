package com.example.programmingmeetups.business.data.util

import android.util.Log
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.utils.ERROR_TIMEOUT
import com.example.programmingmeetups.utils.CACHE_TIMEOUT
import com.example.programmingmeetups.utils.ERROR_UNKNOWN
import com.example.programmingmeetups.utils.NETWORK_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): Resource<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                Resource.Success(apiCall())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    Resource.Error(ERROR_TIMEOUT)
                }
                is IOException -> {
                    Resource.Error()
                }
                is HttpException -> {
                    val errorResponse = convertErrorBody(throwable)
                    Resource.Error(
                        errorResponse
                    )
                }
                else -> {
                    Resource.Error(
                        ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T
): Resource<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(CACHE_TIMEOUT) {
                Resource.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    Resource.Error(ERROR_TIMEOUT)
                }
                else -> {
                    Resource.Error(ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}

private fun convertErrorBody(error: String?): String? {
    return try {
        val json = JSONObject(error)
        return json.getString("error")
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}