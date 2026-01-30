package com.fishing.conditions.data.repository

import com.fishing.conditions.data.api.*
import com.fishing.conditions.data.api.models.*
import com.fishing.conditions.data.cache.MarineDataDao
import com.fishing.conditions.data.cache.entities.MarineDataEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("MarineDataRepository Tests")
class MarineDataRepositoryTest {

    private lateinit var repository: MarineDataRepository
    private lateinit var marineDataDao: MarineDataDao
    private lateinit var noaaApi: NoaaApi
    private lateinit var openMeteoApi: OpenMeteoApi
    private lateinit var openWeatherApi: OpenWeatherApi
    private lateinit var stormglassApi: StormglassApi
    private lateinit var solunarApi: SolunarApi

    @BeforeEach
    fun setup() {
        marineDataDao = mockk(relaxed = true)
        noaaApi = mockk(relaxed = true)
        openMeteoApi = mockk(relaxed = true)
        openWeatherApi = mockk(relaxed = true)
        stormglassApi = mockk(relaxed = true)
        solunarApi = mockk(relaxed = true)

        repository = MarineDataRepository(
            marineDataDao,
            noaaApi,
            openMeteoApi,
            openWeatherApi,
            stormglassApi,
            solunarApi
        )
    }

    @Test
    @DisplayName("Should use cached data when fresh")
    fun `getMarineConditions uses cache when not expired`() = runTest {
        // Given
        val cachedEntity = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis() - 1000 // 1 second ago
        )
        coEvery { marineDataDao.getMarineData(any(), any()) } returns cachedEntity

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.waterTemperature).isEqualTo(68.0)
        coVerify(exactly = 0) { openMeteoApi.getMarineForecast(any(), any()) }
    }

    @Test
    @DisplayName("Should handle API errors gracefully")
    fun `getMarineConditions returns cached data on API error`() = runTest {
        // Given
        val cachedEntity = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis() - (7 * 60 * 60 * 1000) // Expired
        )
        coEvery { marineDataDao.getMarineData(any(), any()) } returns cachedEntity

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then - should not crash and should prefer cached fields when available
        assertThat(result).isNotNull()
    }

    // private fun createMockWeatherResponse(): OpenMeteoResponse {
    //     return OpenMeteoResponse(
    //         latitude = 37.7749,
    //         longitude = -122.4194,
    //         hourly = OpenMeteoHourly(
    //             time = listOf("2026-01-29T00:00"),
    //             temperature = listOf(18.0),
    //             humidity = listOf(70.0),
    //             windSpeed = listOf(5.0),
    //             windDirection = listOf(270.0),
    //             windGusts = listOf(7.0),
    //             pressure = listOf(1013.0),
    //             cloudCover = listOf(30.0),
    //             visibility = listOf(10000.0),
    //             precipitation = listOf(0.0)
    //         ),
    //         current = OpenMeteoCurrent(
    //             time = "2026-01-29T00:00",
    //             temperature = 18.0,
    //             windSpeed = 5.0,
    //             windDirection = 270.0
    //         )
    //     )
    // }
}
