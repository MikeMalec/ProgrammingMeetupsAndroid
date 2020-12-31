package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.DateManager
import com.example.programmingmeetups.framework.presentation.events.showevent.EventViewModel
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class EventBindingAdapter {
    companion object {
        @BindingAdapter("setEventMainImage")
        @JvmStatic
        fun setEventMainImage(imageView: ImageView, event: ProgrammingEvent?) {
            event?.also {
                Glide.with(imageView).load("$IMAGES_URL${event.image}").into(imageView)
            }
        }

        @BindingAdapter("setShowEventAddress")
        @JvmStatic
        fun setShowEventAddress(textView: TextView, event: ProgrammingEvent?) {
            event?.also {
                textView.text = event.address
            }
        }

        @BindingAdapter("setShowEventDate")
        @JvmStatic
        fun setShowEventDate(textView: TextView, event: ProgrammingEvent?) {
            event?.also {
                event.happensAt?.also {
                    textView.text = DateManager.getDateWithDayNameAndHours(it)
                }
            }
        }

        @BindingAdapter("setShowEventDescription")
        @JvmStatic
        fun setShowEventDescription(textView: TextView, event: ProgrammingEvent?) {
            event?.also {
                textView.text = event.description
            }
        }

        @BindingAdapter("setOrganizerName")
        @JvmStatic
        fun setOrganizerName(textView: TextView, event: ProgrammingEvent?) {
            event?.also {
                textView.text =
                    "Organized by ${event.organizer?.firstName} ${event.organizer?.lastName}"
            }
        }

        @BindingAdapter("setOrganizerImage")
        @JvmStatic
        fun setOrganizerImage(imageView: ImageView, event: ProgrammingEvent?) {
            event?.also {
                Glide.with(imageView.context).load("$IMAGES_URL${event.organizer!!.image}")
                    .into(imageView)
            }
        }

        @BindingAdapter("setEventTags")
        @JvmStatic
        fun setEventTags(linearLayout: LinearLayout, event: ProgrammingEvent?) {
            event?.also {
                val layoutInflater = LayoutInflater.from(linearLayout.context)
                linearLayout.removeAllViews()
                event.tags?.forEach { tag ->
                    val tagLayout =
                        layoutInflater.inflate(R.layout.event_tag_without_close, null, false)
                    val chip = tagLayout.findViewById<Chip>(R.id.eventTagChip)
                    chip.text = tag
                    linearLayout.addView(tagLayout)
                }
            }
        }

        @BindingAdapter("setMainBtn", "setEventViewModel")
        @JvmStatic
        fun setMainBtn(
            button: MaterialButton,
            event: ProgrammingEvent?,
            eventViewModel: EventViewModel
        ) {
            event.also {
                event?.organizer
                button.text = eventViewModel.getUserEventRelation()
            }
        }
    }
}