package com.example.programmingmeetups.framework.datasource.network.auth.data.request

import android.net.Uri
import com.example.programmingmeetups.framework.utils.files.FileManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RequestBodyFactoryImpl(private val fileManager: FileManager) :
    RequestBodyFactoryInterface {
    override fun createTextRequestBody(text: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), text)
    }

    override fun createImageRequestBody(uri: Uri, main: Boolean): MultipartBody.Part {
        val file = File(fileManager.getPath(uri)!!)
        var fileName: String? = null
        if (main) {
            fileName = "Main${file.name}"
        } else {
            fileName = "Icon${file.name}"
        }
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("image", fileName, requestBody)
    }
}