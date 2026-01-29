package com.fishing.conditions.data.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marine_data")
data class MarineDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val waterTemperature: Double?,
    val waveHeight: Double?,
    val windSpeed: Double?,
    val airTemperature: Double?,
    val timestamp: Long
)
