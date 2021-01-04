package com.example.programmingmeetups.business.domain.model

import android.os.Parcelable
import com.example.programmingmeetups.framework.utils.IMAGES_URL
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var description: String,
    var image: String
) : Parcelable {
    fun getName(): String {
        return "$firstName $lastName"
    }

    fun getImageUrl(): String {
        return "$IMAGES_URL$image"
    }

    override fun toString(): String {
        return getName()
    }
}