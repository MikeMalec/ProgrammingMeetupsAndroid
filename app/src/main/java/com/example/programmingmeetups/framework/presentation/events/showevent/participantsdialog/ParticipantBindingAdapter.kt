package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.utils.IMAGES_URL

class ParticipantBindingAdapter {
    companion object {
        @BindingAdapter("setParticipantName")
        @JvmStatic
        fun setParticipantName(textView: TextView, user: User) {
            textView.text = "${user.firstName} ${user.lastName}"
        }

        @BindingAdapter("setParticipantPhoto")
        @JvmStatic
        fun setParticipantPhoto(imageView: ImageView, user: User) {
            Glide.with(imageView).load("$IMAGES_URL${user.image}").into(imageView)
        }
    }
}