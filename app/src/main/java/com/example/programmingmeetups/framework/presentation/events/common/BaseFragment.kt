package com.example.programmingmeetups.framework.presentation.events.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.programmingmeetups.framework.presentation.UIController
import java.lang.Exception

abstract class BaseFragment(layout: Int) : Fragment(layout) {
    protected lateinit var uiController: UIController

    protected fun runUiControllerAction(action: () -> Unit) {
        if (::uiController.isInitialized) action()
    }

    protected fun setUIController() {
        try {
            uiController = activity as UIController
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUIController()
    }
}