package com.example.programmingmeetups.framework.presentation.profile

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
import com.example.programmingmeetups.utils.IMAGES_URL

class UserProfileBindingAdapter {
    companion object {

        @BindingAdapter("setImage")
        @JvmStatic
        fun setPhoto(imageView: ImageView, user: User?) {
            if (user != null) {
                Glide.with(imageView).load("${IMAGES_URL}${user.image}").into(imageView)
            }
        }

        @BindingAdapter("setFirstName")
        @JvmStatic
        fun setFirstName(textView: TextView, user: User?) {
            textView.text = user?.firstName
        }

        @BindingAdapter("setLastName")
        @JvmStatic
        fun setLastName(textView: TextView, user: User?) {
            textView.text = user?.lastName
        }

        @BindingAdapter("setEmail")
        @JvmStatic
        fun setEmail(textView: TextView, user: User?) {
            textView.text = user?.email
        }

        @BindingAdapter("setDescription")
        @JvmStatic
        fun setDescription(textView: TextView, user: User?) {
            textView.text = user?.description
        }
    }
}