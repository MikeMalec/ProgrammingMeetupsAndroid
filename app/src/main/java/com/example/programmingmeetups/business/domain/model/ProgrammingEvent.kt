package com.example.programmingmeetups.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgrammingEvent(
    var id: String? = null,
    var tags: MutableList<String>? = mutableListOf(),
    var image: String? = null,
    var icon: String? = null,
    var organizer: User? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var address: String? = null,
    var happensAt: Long? = null,
    var description: String? = null,
    var participants: List<User>? = mutableListOf(),
    var createdAt: String? = null
) : Parcelable