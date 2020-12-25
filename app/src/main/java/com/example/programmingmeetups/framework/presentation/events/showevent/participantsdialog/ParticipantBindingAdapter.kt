package com.example.programmingmeetups.framework.presentation.events.showevent.participantsdialog

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.utils.IMAGES_URL

class ParticipantBindingAdapter {
    companion object {
        @BindingAdapter("setParticipantFirstName")
        @JvmStatic
        fun setParticipantFirstName(textView: TextView, user: User) {
            textView.text = user.firstName
        }

        @BindingAdapter("setParticipantLastName")
        @JvmStatic
        fun setParticipantLastName(textView: TextView, user: User) {
            Log.d("XXX","setParticipantLastName $user")
            textView.text = user.lastName
        }

        @BindingAdapter("setParticipantPhoto")
        @JvmStatic
        fun setParticipantPhoto(imageView: ImageView, user: User) {
            Glide.with(imageView).load("$IMAGES_URL${user.image}").into(imageView)
        }
    }
}