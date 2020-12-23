package com.example.programmingmeetups.framework.presentation.events.userevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.databinding.UserEventBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class UserEventAdapter @Inject constructor() :
    RecyclerView.Adapter<UserEventAdapter.UserEventViewHolder>() {
    class UserEventViewHolder(private val view: UserEventBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(event: ProgrammingEvent) {
            view.event = event
            view.executePendingBindings()
        }

        companion object {
            fun from(viewGroup: ViewGroup): UserEventViewHolder {
                val layoutInflater = LayoutInflater.from(viewGroup.context)
                val binding = UserEventBinding.inflate(layoutInflater, viewGroup, false)
                return UserEventViewHolder(binding)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ProgrammingEvent>() {
        override fun areItemsTheSame(
            oldItem: ProgrammingEvent,
            newItem: ProgrammingEvent
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProgrammingEvent,
            newItem: ProgrammingEvent
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventViewHolder {

        return UserEventViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        val event = getEvents()[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getEvents() = differ.currentList

    fun submitEvents(events: List<ProgrammingEvent>) {
        differ.submitList(events)
    }
}