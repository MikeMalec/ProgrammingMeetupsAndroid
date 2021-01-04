package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.ParticipantsDialogBinding
import com.example.programmingmeetups.framework.presentation.events.showevent.EventViewModel

class ParticipantsDialog(
    private val eventViewModel: EventViewModel,
    showUserCallback: (user: User) -> Unit
) : AppCompatDialogFragment() {

    private lateinit var binding: ParticipantsDialogBinding
    private val participantsAdapter = ParticipantsAdapter { user ->
        showUserCallback(user)
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ParticipantsDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
        checkUsers()
        observeUsers()
    }

    private fun checkUsers() {
        if (eventViewModel.eventUsers.value == null || (eventViewModel.eventUsers.value != null && eventViewModel.eventUsers.value!!.isEmpty())) {
            eventViewModel.paginate(
                eventViewModel.token!!,
                eventViewModel.event.value!!.id!!
            )
        }
    }

    private fun observeUsers() {
        lifecycleScope.launchWhenStarted {
            eventViewModel.eventUsers.observe(viewLifecycleOwner, Observer {
                participantsAdapter.submitList(it)
            })
        }
    }

    private fun setRv() {
        binding.rvParticipants.apply {
            adapter = participantsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val positionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (positionOfLastVisibleItem == participantsAdapter.itemCount ||
                        positionOfLastVisibleItem == participantsAdapter.itemCount - 1
                    ) {
                        eventViewModel.paginate(
                            eventViewModel.token!!,
                            eventViewModel.event.value!!.id!!
                        )
                    }
                }
            })
        }
    }
}