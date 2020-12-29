package com.example.programmingmeetups.framework.presentation.profile

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.utils.IMAGES_URL
import com.google.android.material.textfield.TextInputEditText

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
        fun setFirstName(editText: TextInputEditText, user: User?) {
            editText.setText(user?.firstName)
        }

        @BindingAdapter("setLastName")
        @JvmStatic
        fun setLastName(editText: TextInputEditText, user: User?) {
            editText.setText(user?.lastName)
        }

        @BindingAdapter("setEmail")
        @JvmStatic
        fun setEmail(editText: TextInputEditText, user: User?) {
            editText.setText(user?.email)
        }

        @BindingAdapter("setDescription")
        @JvmStatic
        fun setDescription(editText: TextInputEditText, user: User?) {
            editText.setText(user?.description)
        }
    }
}