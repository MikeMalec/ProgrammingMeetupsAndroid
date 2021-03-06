package com.example.programmingmeetups.framework.datasource.cache.event.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.utils.PROGRAMMING_EVENTS_TABLE
import kotlinx.android.parcel.Parcelize

@Entity(tableName = PROGRAMMING_EVENTS_TABLE)
@Parcelize
data class ProgrammingEventCacheEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var organizerId: String,
    var programmingEventId: String,
    var programmingEvent: ProgrammingEvent
) : Parcelable