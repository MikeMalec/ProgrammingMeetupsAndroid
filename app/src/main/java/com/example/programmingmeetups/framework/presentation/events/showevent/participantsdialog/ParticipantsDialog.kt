package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.ParticipantsDialogBinding

class ParticipantsDialog(private val participants: List<User>) : AppCompatDialogFragment() {

    private lateinit var binding: ParticipantsDialogBinding

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
    }

    private fun setRv() {
        binding.rvParticipants.apply {
            adapter = ParticipantsAdapter(participants)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}