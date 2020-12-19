package com.example.programmingmeetups.framework.presentation.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.utils.PREFERENCES_IMPLEMENTATION
import javax.inject.Named

class UserProfileViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val userProfile = preferencesRepository.getUserInfo().asLiveData()
}