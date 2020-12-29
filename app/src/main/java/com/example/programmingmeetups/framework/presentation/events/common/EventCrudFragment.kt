package com.example.programmingmeetups.framework.presentation.events.common

import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.programmingmeetups.framework.presentation.events.createevent.DateBottomDialog
import com.example.programmingmeetups.framework.presentation.events.createevent.TagBottomDialog
import com.example.programmingmeetups.utils.DATE_BOTTOM_DIALOG
import com.example.programmingmeetups.utils.TAG_BOTTOM_DIALOG
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager

abstract class EventCrudFragment(
    open val contentManager: FrameworkContentManager,
    open val layout: Int
) : BaseFragment(layout) {
    private val getEventImage =
        registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
            result?.run {
                getViewModel().setImage(result)
            }
        }

    fun requestEventImage() = getEventImage.launch("")

    val getEventIcon =
        registerForActivityResult(contentManager.PickImage()) { result: Uri? ->
            result?.run {
                getViewModel().setIcon(result)
            }
        }

    fun requestEventIcon() = getEventIcon.launch("")

    abstract fun getViewModel(): EventCrudViewModel

    protected fun showDateDialog() {
        DateBottomDialog(getViewModel()).show(requireFragmentManager(), DATE_BOTTOM_DIALOG)
    }

    fun showAddTagDialog() {
        TagBottomDialog(getViewModel()).show(requireFragmentManager(), TAG_BOTTOM_DIALOG)
    }

}