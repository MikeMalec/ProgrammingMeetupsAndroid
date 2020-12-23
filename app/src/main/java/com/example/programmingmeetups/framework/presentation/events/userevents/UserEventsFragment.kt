package com.example.programmingmeetups.framework.presentation.events.userevents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.UserEventsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserEventsFragment(var userEventsViewModel: UserEventsViewModel? = null) :
    Fragment(R.layout.user_events_fragment) {

    @Inject
    lateinit var userEventAdapter: UserEventAdapter

    private lateinit var binding: UserEventsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setBinding(view)
        setRecyclerView()
        observeEvents()
    }

    private fun setViewModel() {
        userEventsViewModel = userEventsViewModel ?: ViewModelProvider(requireActivity()).get(
            UserEventsViewModel::class.java
        )
    }

    private fun setBinding(view: View) {
        binding = UserEventsFragmentBinding.bind(view)
    }

    private fun setRecyclerView() {
        binding.rvUserEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userEventAdapter
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            userEventsViewModel!!.userEvents.observe(viewLifecycleOwner, Observer {
                userEventAdapter.submitEvents(it)
            })
        }
    }
}