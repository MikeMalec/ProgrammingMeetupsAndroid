package com.example.programmingmeetups.framework.presentation.events.showevent

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.DateManager
import com.example.programmingmeetups.databinding.EventFragmentBinding
import com.example.programmingmeetups.utils.IMAGES_URL
import com.google.android.material.chip.Chip

class EventFragment : Fragment(R.layout.event_fragment) {
    private lateinit var binding: EventFragmentBinding

    private val args: EventFragmentArgs by navArgs()

    private val programmingEvent: ProgrammingEvent
        get() = args.event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedViewsAnimation()
    }

    private fun setSharedViewsAnimation() {
        val moveAnimation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        moveAnimation.duration = 300
        sharedElementEnterTransition = moveAnimation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EventFragmentBinding.bind(view)
        setViews()
    }

    private fun setViews() {
        binding.apply {
            setMainImage(ivEventImage)
            tvEventAddress.text = programmingEvent.address
            tvEventDate.text = DateManager.getDateWithDayNameAndHours(programmingEvent.happensAt!!)
            tvEventDescription.text = programmingEvent.description
            tvOrganizerName.text =
                "${programmingEvent.organizer?.firstName} ${programmingEvent.organizer?.lastName}"
            Glide.with(requireContext()).load("$IMAGES_URL${programmingEvent.organizer!!.image}")
                .into(binding.ivOrganizer)
            addTags(llEventTags)
        }
    }

    private fun setMainImage(imageView: ImageView) {
        Glide.with(imageView).load("$IMAGES_URL${programmingEvent.image}").into(imageView)
    }

    private fun addTags(linearLayout: LinearLayout) {
        val layoutInflater = LayoutInflater.from(requireContext())
        programmingEvent.tags?.forEach { tag ->
            val tagLayout =
                layoutInflater.inflate(R.layout.event_tag_without_close, null, false)
            val chip = tagLayout.findViewById<Chip>(R.id.eventTagChip)
            chip.text = tag
            linearLayout.addView(tagLayout)
        }
    }
}