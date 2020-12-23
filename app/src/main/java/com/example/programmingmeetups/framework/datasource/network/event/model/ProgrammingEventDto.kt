package com.example.programmingmeetups.framework.datasource.network.event.model

import android.os.Parcelable
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgrammingEventDto(
    val id: String,
    val tags: MutableList<String>,
    val image: String,
    val icon: String,
    val organizer: User,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val happensAt: Long,
    val description: String,
    val participants: List<User>,
    val rates: List<Int>,
    val createdAt: String
) : Parcelable