package com.example.programmingmeetups.framework.presentation.events.userevents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.databinding.UserEventsFragmentBinding
import com.example.programmingmeetups.framework.utils.extensions.view.gone
import com.example.programmingmeetups.framework.utils.extensions.view.hide
import com.example.programmingmeetups.framework.utils.extensions.view.show
import com.example.programmingmeetups.framework.utils.recyclerview.RecyclerViewPaginationScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        canShowNoEvents = true
        lifecycleScope.launchWhenStarted {
            delay(500)
            observeEvents()
        }
    }

    override fun onStart() {
        super.onStart()
        userEventsViewModel?.fetchEvents()
    }

    private fun setViewModel() {
        userEventsViewModel = userEventsViewModel ?: ViewModelProvider(this).get(
            UserEventsViewModel::class.java
        )
    }

    private fun setBinding(view: View) {
        binding = UserEventsFragmentBinding.bind(view)
        if (userEventsViewModel!!.didShimmer) hideShimmer()
        showShimmer()
    }

    private fun showShimmer() {
        if (!userEventsViewModel!!.didShimmer) {
            binding.shimmer.startShimmer()
        }
        userEventsViewModel!!.didShimmer = true
    }

    private fun hideShimmer() {
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
    }

    private var canShowNoEvents = true
    private fun showNoEvents() {
        if (canShowNoEvents) {
            binding.tvNoEvents.show()
        }
    }

    private fun hideNoEvents() {
        binding.tvNoEvents.hide()
    }

    private fun setRecyclerView() {
        userEventAdapter.clickCallback = ::navigateToEvent
        binding.rvUserEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userEventAdapter
            addOnScrollListener(RecyclerViewPaginationScrollListener(userEventAdapter) { userEventsViewModel?.fetchEvents() })
        }
    }

    private fun navigateToEvent(event: ProgrammingEvent) {
        binding.apply {
            val action =
                UserEventsFragmentDirections.actionUserEventsFragmentToEventFragment(
                    event
                )
            findNavController().navigate(action)
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            userEventsViewModel!!.userEvents.observe(viewLifecycleOwner, Observer {
                dispatchAmountOfEvents(it.size)
                hideShimmer()
                userEventAdapter.submitEvents(it)
            })
        }
    }

    private fun dispatchAmountOfEvents(amount: Int) {
        when (amount) {
            0 -> showNoEvents()
            else -> hideNoEvents()
        }
    }

    override fun onPause() {
        super.onPause()
        canShowNoEvents = false
        userEventsViewModel?.reset()
    }
}