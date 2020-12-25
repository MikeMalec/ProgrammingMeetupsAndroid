package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.ParticipantLayoutBinding

class ParticipantsAdapter(private val participants: List<User>) :
    RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {
    class ParticipantViewHolder(val binding: ParticipantLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: User) {
            binding.participant = participant
            binding.executePendingBindings()
        }

        companion object {
            fun from(viewGroup: ViewGroup): ParticipantViewHolder {
                val inflater = LayoutInflater.from(viewGroup.context)
                val view = ParticipantLayoutBinding.inflate(inflater, viewGroup, false)
                return ParticipantViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        participants[position].also { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return participants.size
    }
}