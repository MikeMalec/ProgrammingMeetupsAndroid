package com.example.programmingmeetups.framework.presentation.events.eventcomments

import android.os.Bundle
import android.view.MotionEvent
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
import com.example.programmingmeetups.framework.utils.extensions.view.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        showShimmer()
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

    private fun showShimmer() {
        binding.shimmer.startShimmer()
    }

    private fun hideShimmer() {
        binding.shimmer.stopShimmer()
        binding.shimmer.gone()
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
        binding.etComment.setOnTouchListener { view, motionEvent ->
            val right = 2
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (motionEvent.rawX >= (binding.etComment.right - binding.etComment.compoundDrawables[right].bounds.width())) {
                    viewModel.sendComment(event.id!!, binding.etComment.text.toString())
                    binding.etComment.setText("")
                    true
                }
            }
            false
        }
    }

    private fun observeComments() {
        lifecycleScope.launchWhenStarted {
            delay(300)
            viewModel.comments.observe(viewLifecycleOwner, Observer {
                hideShimmer()
                eventCommentAdapter.submitComments(it)
            })
        }
    }
}