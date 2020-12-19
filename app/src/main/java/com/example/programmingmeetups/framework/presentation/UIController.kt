package com.example.programmingmeetups.framework.presentation

interface UIController {
    fun showShortToast(message: String)
    fun showLongToast(message: String)
    fun showShortSnackbar(message: String)
    fun showLongSnackbar(message: String)
}