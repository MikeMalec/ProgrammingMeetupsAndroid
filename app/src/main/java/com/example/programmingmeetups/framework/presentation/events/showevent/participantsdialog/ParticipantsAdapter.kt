package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.ParticipantLayoutBinding

class ParticipantsAdapter() :
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

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(users: List<User>) {
        differ.submitList(users)
        notifyDataSetChanged()
    }

    fun getUsers() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        getUsers()[position].also { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return getUsers().size
    }
}