package com.example.programmingmeetups.framework.utils.extensions.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.MapView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

fun ImageView.clearBackground() {
    background = null
}

fun ImageView.clearImageTintList() {
    imageTintList = null
}

fun BottomNavigationView.hide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}

fun BottomNavigationView.show() {
    if (visibility == View.INVISIBLE || visibility == View.GONE) {
        visibility = View.VISIBLE
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun MapView.hide() {
    visibility = View.INVISIBLE
}

fun MapView.show() {
    visibility = View.VISIBLE
}

fun TextView.show() {
    visibility = View.VISIBLE
}

fun TextView.hide() {
    visibility = View.INVISIBLE
}

fun AppCompatActivity.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun BottomSheetDialogFragment.shortToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.shortSnackbar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun AppCompatActivity.longSnackbar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}