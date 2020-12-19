package com.example.programmingmeetups.utils.files

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class FileManager(val context: Context) {

    fun getPath(uri: Uri): String? {
        val cursor = context.contentResolver?.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val path = cursor.getString(index)
            cursor.close()
            return path
        }
        return null
    }
}