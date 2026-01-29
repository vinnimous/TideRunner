package com.fishing.conditions.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fishing.conditions.data.cache.entities.MarineDataEntity

@Database(
    entities = [MarineDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MarineDataDatabase : RoomDatabase() {
    abstract fun marineDataDao(): MarineDataDao
}
