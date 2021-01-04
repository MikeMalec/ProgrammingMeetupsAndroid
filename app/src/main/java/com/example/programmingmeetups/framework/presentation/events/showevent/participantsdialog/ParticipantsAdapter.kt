package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.databinding.ParticipantLayoutBinding

class ParticipantsAdapter(val callback: (user: User) -> Unit) :
    RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {
    class ParticipantViewHolder(val participantLayoutBinding: ParticipantLayoutBinding) :
        RecyclerView.ViewHolder(participantLayoutBinding.root)

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
        val binding = ParticipantLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val user = getUsers()[position]
        holder.participantLayoutBinding.apply {
            clParticipantLayout.setOnClickListener { callback(user) }
            tvUserName.text = user.getName()
            Glide.with(ivUserImage).load(user.getImageUrl()).into(ivUserImage)
        }
    }

    override fun getItemCount(): Int {
        return getUsers().size
    }
}