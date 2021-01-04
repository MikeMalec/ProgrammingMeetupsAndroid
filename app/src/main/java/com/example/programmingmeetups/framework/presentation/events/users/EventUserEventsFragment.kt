package com.example.programmingmeetups.framework.presentation.events.users

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.EventUserEventsFragmentBinding
import com.example.programmingmeetups.framework.presentation.events.common.BaseFragment
import com.example.programmingmeetups.framework.presentation.events.userevents.UserEventAdapter
import com.example.programmingmeetups.framework.utils.recyclerview.RecyclerViewPaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventUserEventsFragment(var eventUserViewModel: EventUserViewModel? = null) :
    BaseFragment(R.layout.event_user_events_fragment) {

    @Inject
    lateinit var userEventAdapter: UserEventAdapter

    private lateinit var binding: EventUserEventsFragmentBinding
    private val args: EventUserProfileFragmentArgs by navArgs()
    private val user: User
        get() = args.user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding(view)
        setViewModel()
        setRv()
        observeEvents()
        fetchEvents()
    }

    private fun setBinding(view: View) {
        binding = EventUserEventsFragmentBinding.bind(view)
    }

    private fun setViewModel() {
        eventUserViewModel =
            eventUserViewModel ?: ViewModelProvider(this).get(EventUserViewModel::class.java)
    }

    private fun setRv() {
        userEventAdapter.clickCallback = ::navigateToEvent
        binding.rvUserEvents.apply {
            adapter = userEventAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(RecyclerViewPaginationScrollListener(userEventAdapter) {
                eventUserViewModel!!.paginate(
                    eventUserViewModel!!.token,
                    user.id
                )
            })
        }
    }

    private fun navigateToEvent(programmingEvent: ProgrammingEvent) {
        EventUserEventsFragmentDirections.actionEventUserEventsFragmentToEventFragment(
            programmingEvent
        ).run {
            findNavController().navigate(this)
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            eventUserViewModel!!.events.observe(viewLifecycleOwner, Observer {
                userEventAdapter.submitEvents(it)
            })
        }
    }

    private fun fetchEvents() {
        if (eventUserViewModel!!.events.value == null) {
            eventUserViewModel!!.paginate(eventUserViewModel!!.token, user.id)
        }
    }
}