package com.example.programmingmeetups.framework.presentation.events.userevents

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.DateManager
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import com.google.android.material.card.MaterialCardView

class UserEventBindAdapter {
    companion object {
        @BindingAdapter("setEventItemImage")
        @JvmStatic
        fun setEventItemImage(imageView: ImageView, event: ProgrammingEvent) {
            Glide.with(imageView).load("$IMAGES_URL${event.image}").into(imageView)
        }

        @BindingAdapter("setEventItemAddress")
        @JvmStatic
        fun setEventItemAddress(textView: TextView, event: ProgrammingEvent) {
            textView.text = event.address
        }

        @BindingAdapter("setEventItemDate")
        @JvmStatic
        fun setEventItemDate(textView: TextView, event: ProgrammingEvent) {
            textView.text = DateManager.getDateWithDayNameAndHours(event.happensAt!!)
        }

        @BindingAdapter("setOnClickListener", "event")
        @JvmStatic
        fun setOnClickListener(
            cardView: MaterialCardView,
            imageView: ImageView,
            event: ProgrammingEvent
        ) {
            cardView.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    imageView to "eventImage"
                )
                val action =
                    UserEventsFragmentDirections.actionUserEventsFragmentToEventFragment(
                        event
                    )
                cardView.findNavController().navigate(action, extras)
            }
        }
    }
}