package com.example.programmingmeetups.framework.datasource.cache.event.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.programmingmeetups.framework.datasource.cache.event.model.ProgrammingEventCacheEntity

@Database(entities = [ProgrammingEventCacheEntity::class], version = 1)
@TypeConverters(EventConverter::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun getEventDao(): EventDao
}