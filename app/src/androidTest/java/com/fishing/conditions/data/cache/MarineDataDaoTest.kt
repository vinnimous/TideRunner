package com.fishing.conditions.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fishing.conditions.data.cache.entities.MarineDataEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarineDataDaoTest {

    private lateinit var database: MarineDataDatabase
    private lateinit var dao: MarineDataDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MarineDataDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.marineDataDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveMarineData() = runTest {
        // Given
        val entity = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis()
        )

        // When
        dao.insertMarineData(entity)
        val retrieved = dao.getMarineData(37.7749, -122.4194)

        // Then
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.waterTemperature).isEqualTo(20.0)
        assertThat(retrieved?.waveHeight).isEqualTo(1.0)
    }

    @Test
    fun updateExistingMarineData() = runTest {
        // Given
        val entity1 = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMarineData(entity1)

        // When - Insert new data for same location
        val entity2 = entity1.copy(
            waterTemperature = 22.0,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMarineData(entity2)

        val retrieved = dao.getMarineData(37.7749, -122.4194)

        // Then - Should have updated data
        assertThat(retrieved?.waterTemperature).isEqualTo(22.0)
    }

    @Test
    fun deleteOldCachedData() = runTest {
        // Given
        val oldTimestamp = System.currentTimeMillis() - (48 * 60 * 60 * 1000) // 48 hours ago
        val oldEntity = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = oldTimestamp
        )
        dao.insertMarineData(oldEntity)

        // When
        val cutoffTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000) // 24 hours
        dao.deleteOldData(cutoffTime)

        // Then
        val retrieved = dao.getMarineData(37.7749, -122.4194)
        assertThat(retrieved).isNull()
    }

    @Test
    fun getMarineDataReturnsNullForNonExistentLocation() = runTest {
        // When
        val retrieved = dao.getMarineData(0.0, 0.0)

        // Then
        assertThat(retrieved).isNull()
    }

    @Test
    fun multipleLocationsCanBeStored() = runTest {
        // Given
        val entity1 = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis()
        )
        val entity2 = MarineDataEntity(
            latitude = 40.7128,
            longitude = -74.0060,
            waterTemperature = 15.0,
            waveHeight = 2.0,
            windSpeed = 8.0,
            airTemperature = 12.0,
            timestamp = System.currentTimeMillis()
        )

        // When
        dao.insertMarineData(entity1)
        dao.insertMarineData(entity2)

        // Then
        val retrieved1 = dao.getMarineData(37.7749, -122.4194)
        val retrieved2 = dao.getMarineData(40.7128, -74.0060)

        assertThat(retrieved1?.waterTemperature).isEqualTo(20.0)
        assertThat(retrieved2?.waterTemperature).isEqualTo(15.0)
    }
}
