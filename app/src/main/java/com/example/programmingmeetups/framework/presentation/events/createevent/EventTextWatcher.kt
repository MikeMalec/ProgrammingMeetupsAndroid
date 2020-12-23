package com.example.programmingmeetups.framework.presentation.events.createevent

import android.text.Editable
import android.text.TextWatcher
import javax.inject.Inject

class EventTextWatcher @Inject constructor() : TextWatcher {

    lateinit var createEventViewModel: CreateEventViewModel

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        createEventViewModel.setDescription(p0.toString())
    }

    override fun afterTextChanged(p0: Editable?) {}
}