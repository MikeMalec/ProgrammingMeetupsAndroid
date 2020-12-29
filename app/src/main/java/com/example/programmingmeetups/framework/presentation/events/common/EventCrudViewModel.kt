package com.example.programmingmeetups.framework.presentation.events.common

import android.net.Uri
import androidx.lifecycle.ViewModel

abstract class EventCrudViewModel : ViewModel() {
    open fun setImage(uri: Uri) {}
    open fun setIcon(uri: Uri) {}
    open fun setDate(date: Long) {}
    open fun addTag(tag: String) {}
    open fun removeTag(tag: String) {}
    open fun setDescription(description: String) {}

}