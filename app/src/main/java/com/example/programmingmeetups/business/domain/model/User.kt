package com.example.programmingmeetups.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var description: String,
    var image: String
) : Parcelable