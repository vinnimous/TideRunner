package com.fishing.conditions.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fishing.conditions.data.cache.entities.MarineDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarineDataDao {
    @Query("SELECT * FROM marine_data WHERE latitude = :lat AND longitude = :lon ORDER BY timestamp DESC LIMIT 1")
    suspend fun getMarineData(lat: Double, lon: Double): MarineDataEntity?

    @Query("SELECT * FROM marine_data ORDER BY timestamp DESC")
    fun getAllMarineData(): Flow<List<MarineDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarineData(data: MarineDataEntity)

    @Query("DELETE FROM marine_data WHERE timestamp < :timestamp")
    suspend fun deleteOldData(timestamp: Long)
}
