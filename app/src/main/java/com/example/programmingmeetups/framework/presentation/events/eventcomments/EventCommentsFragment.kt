package com.example.programmingmeetups.framework.presentation.events.eventcomments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.databinding.EventCommentsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventCommentsFragment(var eventCommentsViewModel: EventCommentsViewModel? = null) :
    Fragment(R.layout.event_comments) {

    val viewModel: EventCommentsViewModel
        get() = eventCommentsViewModel!!

    private lateinit var binding: EventCommentsBinding

    private val args: EventCommentsFragmentArgs by navArgs()

    private val event: ProgrammingEvent
        get() = args.event

    @Inject
    lateinit var eventCommentAdapter: EventCommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        binding = EventCommentsBinding.bind(view)
        setRv()
        setSendClick()
        observeComments()
    }

    private fun setViewModel() {
        eventCommentsViewModel = eventCommentsViewModel ?: ViewModelProvider(this).get(
            EventCommentsViewModel::class.java
        )
        viewModel.connectSocket(event.id!!)
        viewModel.fetchComments(event.id!!)
    }

    private fun setRv() {
        binding.rvComments.apply {
            val lm = LinearLayoutManager(requireContext())
            lm.reverseLayout = true
            layoutManager = lm
            adapter = eventCommentAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val positionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (positionOfLastVisibleItem == eventCommentAdapter.itemCount || positionOfLastVisibleItem == eventCommentAdapter.itemCount - 1) {
                        viewModel.fetchComments(event.id!!)
                    }
                }
            })
        }
    }

    private fun setSendClick() {
        binding.btnSendComment.setOnClickListener {
            viewModel.sendComment(event.id!!, binding.etComment.text.toString())
        }
    }

    private fun observeComments() {
        lifecycleScope.launchWhenStarted {
            viewModel.comments.observe(viewLifecycleOwner, Observer {
                eventCommentAdapter.submitComments(it)
            })
        }
    }
}