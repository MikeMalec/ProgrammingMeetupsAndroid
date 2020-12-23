package com.example.programmingmeetups.business.domain.model

import android.os.Parcelable
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
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
    var rates: List<Int>? = mutableListOf(),
    var createdAt: String? = null
) : Parcelable