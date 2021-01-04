package com.example.programmingmeetups.framework.presentation.events.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class ViewModelWithToken(val preferencesRepository: PreferencesRepository) : ViewModel() {
    lateinit var token: String

    init {
        setToken()
    }

    private fun setToken() {
        viewModelScope.launch {
            preferencesRepository.getToken().collect {
                it?.also {
                    token = it
                }
            }
        }
    }
}