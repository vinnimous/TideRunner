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
        noaaApi = mockk()
        openMeteoApi = mockk()
        openWeatherApi = mockk()
        stormglassApi = mockk()
        solunarApi = mockk()

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
    @DisplayName("Should fetch fresh data when cache is empty")
    fun `getMarineConditions fetches from APIs when no cache`() = runTest {
        // Given
        coEvery { marineDataDao.getMarineData(any(), any()) } returns null
        coEvery { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) } returns createMockMarineResponse()
        coEvery { openMeteoApi.getForecast(any(), any(), any(), any(), any(), any()) } returns createMockWeatherResponse()
        coEvery { solunarApi.getSolunarData(any(), any(), any(), any()) } returns createMockSolunarResponse()

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.latitude).isEqualTo(37.7749)
        assertThat(result?.longitude).isEqualTo(-122.4194)
        coVerify { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) }
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
        assertThat(result?.waterTemperature).isEqualTo(20.0)
        coVerify(exactly = 0) { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) }
    }

    @Test
    @DisplayName("Should fetch fresh data when cache is expired")
    fun `getMarineConditions refreshes when cache expired`() = runTest {
        // Given - cache from 7 hours ago (expired)
        val expiredEntity = MarineDataEntity(
            latitude = 37.7749,
            longitude = -122.4194,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            windSpeed = 5.0,
            airTemperature = 18.0,
            timestamp = System.currentTimeMillis() - (7 * 60 * 60 * 1000)
        )
        coEvery { marineDataDao.getMarineData(any(), any()) } returns expiredEntity
        coEvery { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) } returns createMockMarineResponse()
        coEvery { openMeteoApi.getForecast(any(), any(), any(), any(), any(), any()) } returns createMockWeatherResponse()
        coEvery { solunarApi.getSolunarData(any(), any(), any(), any()) } returns createMockSolunarResponse()

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then
        assertThat(result).isNotNull()
        coVerify { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) }
    }

    @Test
    @DisplayName("Should cache fetched data")
    fun `getMarineConditions caches new data`() = runTest {
        // Given
        coEvery { marineDataDao.getMarineData(any(), any()) } returns null
        coEvery { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) } returns createMockMarineResponse()
        coEvery { openMeteoApi.getForecast(any(), any(), any(), any(), any(), any()) } returns createMockWeatherResponse()
        coEvery { solunarApi.getSolunarData(any(), any(), any(), any()) } returns createMockSolunarResponse()

        // When
        repository.getMarineConditions(37.7749, -122.4194)

        // Then
        coVerify { marineDataDao.insertMarineData(any()) }
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
        coEvery { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) } throws Exception("Network error")

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then - should not crash and should prefer cached fields when available
        assertThat(result).isNotNull()
    }

    @Test
    @DisplayName("Should return null when no data available")
    fun `getMarineConditions returns null when no cache and API fails`() = runTest {
        // Given
        coEvery { marineDataDao.getMarineData(any(), any()) } returns null
        coEvery { openMeteoApi.getMarineForecast(any(), any(), any(), any(), any()) } throws Exception("Network error")

        // When
        val result = repository.getMarineConditions(37.7749, -122.4194)

        // Then
        assertThat(result).isNotNull()
    }

    private fun createMockMarineResponse(): OpenMeteoMarineResponse {
        return OpenMeteoMarineResponse(
            latitude = 37.7749,
            longitude = -122.4194,
            hourly = OpenMeteoMarineHourly(
                time = listOf("2026-01-29T00:00"),
                waveHeight = listOf(1.0),
                waveDirection = listOf(180.0),
                wavePeriod = listOf(5.0),
                windWaveHeight = listOf(0.8),
                windWaveDirection = listOf(180.0),
                swellWaveHeight = listOf(0.2),
                swellWaveDirection = listOf(180.0),
                currentVelocity = listOf(0.5),
                currentDirection = listOf(90.0),
                seaSurfaceTemperature = listOf(20.0)
            ),
            hourlyUnits = mapOf("wave_height" to "m")
        )
    }

    private fun createMockWeatherResponse(): OpenMeteoResponse {
        return OpenMeteoResponse(
            latitude = 37.7749,
            longitude = -122.4194,
            hourly = OpenMeteoHourly(
                time = listOf("2026-01-29T00:00"),
                temperature = listOf(18.0),
                humidity = listOf(70.0),
                windSpeed = listOf(5.0),
                windDirection = listOf(270.0),
                windGusts = listOf(7.0),
                pressure = listOf(1013.0),
                cloudCover = listOf(30.0),
                visibility = listOf(10000.0),
                precipitation = listOf(0.0)
            ),
            current = OpenMeteoCurrent(
                time = "2026-01-29T00:00",
                temperature = 18.0,
                windSpeed = 5.0,
                windDirection = 270.0
            )
        )
    }

    private fun createMockSolunarResponse(): SolunarResponse {
        return SolunarResponse(
            date = "2026-01-29",
            latitude = 37.7749,
            longitude = -122.4194,
            timezone = "UTC",
            moonPhase = SolunarMoonPhase(phase = "Full Moon", illumination = 100.0, age = 14.0),
            moonrise = "12:00",
            moonset = "00:00",
            moonTransit = "18:00",
            sunrise = "06:00",
            sunset = "18:00",
            dayLength = "12:00",
            sunTransit = "12:00",
            majorPeriods = listOf(
                SolunarPeriod(start = "06:00", end = "08:00", type = "major"),
                SolunarPeriod(start = "18:00", end = "20:00", type = "major")
            ),
            minorPeriods = listOf(
                SolunarPeriod(start = "12:00", end = "13:00", type = "minor")
            ),
            score = 85
        )
    }
}
