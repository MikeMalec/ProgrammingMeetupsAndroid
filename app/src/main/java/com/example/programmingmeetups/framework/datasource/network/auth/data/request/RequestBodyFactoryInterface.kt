package com.example.programmingmeetups.framework.datasource.network.auth.data.request

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RequestBodyFactoryInterface {
    fun createTextRequestBody(text: String): RequestBody
    fun createImageRequestBody(uri: Uri): MultipartBody.Part
}