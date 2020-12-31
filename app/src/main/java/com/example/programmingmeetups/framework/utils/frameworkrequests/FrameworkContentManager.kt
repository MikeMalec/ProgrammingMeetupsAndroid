package com.example.programmingmeetups.framework.utils.frameworkrequests

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import javax.inject.Inject

class FrameworkContentManager @Inject constructor() {
    inner class PickImage : ActivityResultContract<String, Uri?>() {
        override fun createIntent(context: Context, type: String) =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        override fun parseResult(resultCode: Int, result: Intent?): Uri? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return result?.data
        }
    }
}