package com.example.programmingmeetups.framework.presentation.events.createevent

import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.DateManager
import com.example.programmingmeetups.utils.extensions.view.show
import com.google.android.material.chip.Chip

class CreateEventBindingAdapter {
    companion object {
        @BindingAdapter("setEventAddress")
        @JvmStatic
        fun setEventAddress(textView: TextView, programmingEvent: ProgrammingEvent) {
            textView.text = programmingEvent.address
        }

        @BindingAdapter("setEventDate")
        @JvmStatic
        fun setEventDate(textView: TextView, programmingEvent: ProgrammingEvent) {
            programmingEvent.happensAt?.run {
                textView.text = DateManager.getDateWithDayNameAndHours(this)
            }
        }

        @BindingAdapter("addTag", "passViewModel")
        @JvmStatic
        fun addTag(
            linearLayout: LinearLayout,
            programmingEvent: ProgrammingEvent,
            createEventViewModel: CreateEventViewModel
        ) {
            val layoutInflater = LayoutInflater.from(linearLayout.context)
            linearLayout.removeAllViews()
            programmingEvent.tags?.forEach { tag ->
                val tagLayout =
                    layoutInflater.inflate(R.layout.event_tag, null, false)
                val chip = tagLayout.findViewById<Chip>(R.id.eventTagChip)
                chip.text = tag
                chip.setOnClickListener {
                    linearLayout.removeView(tagLayout)
                    createEventViewModel.removeTag(tag)
                }
                linearLayout.addView(tagLayout)
            }
        }

        @BindingAdapter("setEventImage")
        @JvmStatic
        fun setEventImage(textView: TextView, programmingEvent: ProgrammingEvent) {
            programmingEvent.image?.run {
                textView.show()
            }
        }

        @BindingAdapter("setEventIcon")
        @JvmStatic
        fun setEventIcon(textView: TextView, programmingEvent: ProgrammingEvent) {
            programmingEvent.icon?.run {
                textView.show()
            }
        }

        @BindingAdapter("setEventDescription")
        @JvmStatic
        fun setEventDescription(editText: EditText, programmingEvent: ProgrammingEvent) {
            val description = programmingEvent.description
            if (description != null && description.isNotEmpty() && editText.text.toString()
                    .isEmpty()
            ) {
                editText.setText(description)
            }
        }
    }
}