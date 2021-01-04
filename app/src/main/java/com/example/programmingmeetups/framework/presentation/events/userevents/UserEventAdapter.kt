package com.example.programmingmeetups.framework.presentation.events.userevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.DateManager
import com.example.programmingmeetups.databinding.UserEventBinding
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import dagger.hilt.android.scopes.FragmentScoped
import java.util.zip.Inflater
import javax.inject.Inject

@FragmentScoped
class UserEventAdapter @Inject constructor() :
    RecyclerView.Adapter<UserEventAdapter.UserEventViewHolder>() {

    lateinit var clickCallback: (event: ProgrammingEvent) -> Unit

    class UserEventViewHolder(val view: UserEventBinding) : RecyclerView.ViewHolder(view.root)

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
        val binding = UserEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        val event = getEvents()[position]
        holder.view.apply {
            Glide.with(ivEventImage).load("$IMAGES_URL${event.image}").into(ivEventImage)
            tvEventAddress.text = event.address
            tvEventDate.text = DateManager.getDateWithDayNameAndHours(event.happensAt!!)
            userEventCard.setOnClickListener { clickCallback(event) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getEvents() = differ.currentList

    fun submitEvents(events: List<ProgrammingEvent>) {
        differ.submitList(events)
        notifyDataSetChanged()
    }
}