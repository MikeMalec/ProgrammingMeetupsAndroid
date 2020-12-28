package com.example.programmingmeetups.framework.presentation.events.eventcomments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.databinding.EventCommentItemBinding
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import javax.inject.Inject

class EventCommentAdapter @Inject constructor() :
    RecyclerView.Adapter<EventCommentAdapter.EventCommentViewHolder>() {

    class EventCommentViewHolder(val binding: EventCommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(programmingEventCommentDto: ProgrammingEventCommentDto) {
            binding.comment = programmingEventCommentDto
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EventCommentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val layout = EventCommentItemBinding.inflate(layoutInflater, parent, false)
                return EventCommentViewHolder(layout)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ProgrammingEventCommentDto>() {
        override fun areItemsTheSame(
            oldItem: ProgrammingEventCommentDto,
            newItem: ProgrammingEventCommentDto
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ProgrammingEventCommentDto,
            newItem: ProgrammingEventCommentDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    private fun getComments() = differ.currentList

    fun submitComments(comments: List<ProgrammingEventCommentDto>) {
        differ.submitList(comments)
        // for unknown reason diff util doesn't show comments from sockets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCommentViewHolder {
        return EventCommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EventCommentViewHolder, position: Int) {
        getComments()[position].also { holder.bind(it) }
    }

    override fun getItemCount(): Int = getComments().size
}