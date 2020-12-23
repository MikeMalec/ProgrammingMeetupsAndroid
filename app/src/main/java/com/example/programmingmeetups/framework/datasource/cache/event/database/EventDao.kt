package com.example.programmingmeetups.framework.datasource.cache.event.database

import androidx.room.*
import com.example.programmingmeetups.framework.datasource.cache.event.model.ProgrammingEventCacheEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgrammingEvent(programmingEventCacheEntity: ProgrammingEventCacheEntity)

    @Delete
    suspend fun deleteProgrammingEvent(programmingEventCacheEntity: ProgrammingEventCacheEntity)

    @Query("SELECT * FROM programming_events_table")
    fun getProgrammingEvents(): List<ProgrammingEventCacheEntity>
}