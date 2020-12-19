package com.example.programmingmeetups.framework.datasource.network.auth.data.request

import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AndroidFakeRequestBodyFactoryImpl : RequestBodyFactoryInterface {
    var file: File = File("test")

    override fun createTextRequestBody(text: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), text)

    }

    override fun createImageRequestBody(uri: Uri): MultipartBody.Part {
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("image", "test", requestBody)
    }
}