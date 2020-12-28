package com.example.programmingmeetups.framework.presentation.events.eventcomments

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import com.example.programmingmeetups.utils.IMAGES_URL

class EventCommentBinderAdapter {
    companion object {
        @BindingAdapter("setCommentUser")
        @JvmStatic
        fun setCommentUser(
            textView: TextView,
            programmingEventCommentDto: ProgrammingEventCommentDto
        ) {
            textView.text =
                "${programmingEventCommentDto.user.firstName} ${programmingEventCommentDto.user.lastName}"
        }

        @BindingAdapter("setCommentUserImage")
        @JvmStatic
        fun setCommentUserImage(
            imageView: ImageView,
            programmingEventCommentDto: ProgrammingEventCommentDto
        ) {
            Glide.with(imageView).load("${IMAGES_URL}${programmingEventCommentDto.user.image}")
                .into(imageView)
        }

        @BindingAdapter("setCommentMessage")
        @JvmStatic
        fun setCommentMessage(
            textView: TextView,
            programmingEventCommentDto: ProgrammingEventCommentDto
        ) {
            textView.text = programmingEventCommentDto.comment
        }
    }
}