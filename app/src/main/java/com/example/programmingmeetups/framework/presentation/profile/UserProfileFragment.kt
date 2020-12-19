package com.example.programmingmeetups.framework.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.UserProfileFragmentBinding

class UserProfileFragment(
    private var userProfileViewModel: UserProfileViewModel? = null
) :
    Fragment() {

    private var binding: UserProfileFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel()
        return setBinding(inflater, container)
    }

    private fun setViewModel() {
        userProfileViewModel = userProfileViewModel ?: ViewModelProvider(requireActivity()).get(
            UserProfileViewModel::class.java
        )
    }

    private fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = UserProfileFragmentBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = this
        binding!!.userProfileViewModel = userProfileViewModel
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}