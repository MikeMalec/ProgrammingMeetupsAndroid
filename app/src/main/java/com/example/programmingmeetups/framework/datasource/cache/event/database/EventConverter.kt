package com.example.programmingmeetups.framework.datasource.cache.event.database

import androidx.room.TypeConverter
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToProgrammingEvent(programmingEvent: String): ProgrammingEvent {
        val listType = object : TypeToken<ProgrammingEvent>() {}.type
        return gson.fromJson(programmingEvent, listType)
    }

    @TypeConverter
    fun programmingEventToString(programmingEvent: ProgrammingEvent): String {
        return gson.toJson(programmingEvent)
    }
}