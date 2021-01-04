package com.example.programmingmeetups.framework.presentation.events.users

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.EventUserProfileFragmentBinding
import com.example.programmingmeetups.framework.presentation.events.common.BaseFragment
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventUserProfileFragment :
    BaseFragment(R.layout.event_user_profile_fragment) {

    private lateinit var binding: EventUserProfileFragmentBinding

    private val args: EventUserProfileFragmentArgs by navArgs()

    private val user: User
        get() = args.user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding(view)
        setViews()
    }

    private fun setBinding(view: View) {
        binding = EventUserProfileFragmentBinding.bind(view)
    }

    private fun setViews() {
        binding.apply {
            Glide.with(requireView()).load("$IMAGES_URL${user.image}").into(ivUserImage)
            tvUserName.text = user.getName()
            tvFewWords.text = "Few words about ${user.firstName}"
            tvEventsInvolved.text = "Find out what events ${user.firstName} is involved in"
            tvUserDescription.text = user.description
            clUserInvolvedIn.setOnClickListener {
                navigateToUserEvents()
            }
        }
    }

    private fun navigateToUserEvents() {
        EventUserProfileFragmentDirections.actionEventUserProfileFragmentToEventUserEventsFragment(
            user
        ).run {
            findNavController().navigate(this)
        }
    }
}