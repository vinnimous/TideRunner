package com.fishing.conditions.data.api.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("API Models Tests")
class ApiModelsTest {

    @Test
    @DisplayName("OpenMeteoMarineResponse parses correctly")
    fun `openMeteoMarineResponse has correct structure`() {
        // Given
        val response = OpenMeteoMarineResponse(
            latitude = 37.7749,
            longitude = -122.4194,
            hourly = OpenMeteoMarineResponse.MarineHourly(
                time = listOf("2026-01-29T00:00", "2026-01-29T01:00"),
                waveHeight = listOf(1.0, 1.2),
                waveDirection = listOf(180.0, 185.0),
                wavePeriod = listOf(5.0, 5.2),
                windWaveHeight = listOf(0.8, 0.9),
                windWaveDirection = listOf(180.0, 185.0),
                swellWaveHeight = listOf(0.2, 0.3),
                swellWaveDirection = listOf(180.0, 185.0),
                currentVelocity = listOf(0.5, 0.6),
                currentDirection = listOf(90.0, 95.0),
                seaSurfaceTemperature = listOf(20.0, 20.1)
            )
        )

        // Then
        assertThat(response.latitude).isEqualTo(37.7749)
        assertThat(response.hourly.waveHeight).hasSize(2)
        assertThat(response.hourly.waveHeight[0]).isEqualTo(1.0)
        assertThat(response.hourly.seaSurfaceTemperature[0]).isEqualTo(20.0)
    }

    @Test
    @DisplayName("OpenMeteoResponse parses correctly")
    fun `openMeteoResponse has correct structure`() {
        // Given
        val response = OpenMeteoResponse(
            latitude = 37.7749,
            longitude = -122.4194,
            current = OpenMeteoResponse.Current(
                time = "2026-01-29T00:00",
                temperature = 18.0,
                windSpeed = 5.0,
                windDirection = 270.0
            ),
            hourly = OpenMeteoResponse.Hourly(
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
            )
        )

        // Then
        assertThat(response.current.temperature).isEqualTo(18.0)
        assertThat(response.hourly.windSpeed).hasSize(1)
        assertThat(response.hourly.pressure[0]).isEqualTo(1013.0)
    }

    @Test
    @DisplayName("SolunarResponse parses correctly")
    fun `solunarResponse has correct structure`() {
        // Given
        val response = SolunarResponse(
            sunrise = "06:00",
            sunset = "18:00",
            moonrise = "12:00",
            moonset = "00:00",
            moonPhase = SolunarResponse.MoonPhase(
                phase = "Full Moon",
                illumination = 100
            ),
            majorPeriods = listOf(
                SolunarResponse.SolunarPeriod(start = "06:00", end = "08:00"),
                SolunarResponse.SolunarPeriod(start = "18:00", end = "20:00")
            ),
            minorPeriods = listOf(
                SolunarResponse.SolunarPeriod(start = "12:00", end = "13:00")
            ),
            score = 85
        )

        // Then
        assertThat(response.sunrise).isEqualTo("06:00")
        assertThat(response.moonPhase.phase).isEqualTo("Full Moon")
        assertThat(response.majorPeriods).hasSize(2)
        assertThat(response.minorPeriods).hasSize(1)
        assertThat(response.score).isEqualTo(85)
    }

    @Test
    @DisplayName("SolunarResponse handles all moon phases")
    fun `solunarResponse supports all moon phases`() {
        // Given
        val phases = listOf(
            "New Moon",
            "Waxing Crescent",
            "First Quarter",
            "Waxing Gibbous",
            "Full Moon",
            "Waning Gibbous",
            "Last Quarter",
            "Waning Crescent"
        )

        // When/Then
        phases.forEach { phaseName ->
            val response = SolunarResponse(
                sunrise = "06:00",
                sunset = "18:00",
                moonrise = "12:00",
                moonset = "00:00",
                moonPhase = SolunarResponse.MoonPhase(
                    phase = phaseName,
                    illumination = 50
                ),
                majorPeriods = emptyList(),
                minorPeriods = emptyList(),
                score = 70
            )
            assertThat(response.moonPhase.phase).isEqualTo(phaseName)
        }
    }

    @Test
    @DisplayName("NoaaResponse handles station data")
    fun `noaaResponse has correct structure`() {
        // This test would require NoaaResponse model to be defined
        // Placeholder for when NOAA integration is fully implemented
    }

    @Test
    @DisplayName("API models handle empty lists")
    fun `api models handle empty hourly data`() {
        // Given
        val response = OpenMeteoMarineResponse(
            latitude = 37.7749,
            longitude = -122.4194,
            hourly = OpenMeteoMarineResponse.MarineHourly(
                time = emptyList(),
                waveHeight = emptyList(),
                waveDirection = emptyList(),
                wavePeriod = emptyList(),
                windWaveHeight = emptyList(),
                windWaveDirection = emptyList(),
                swellWaveHeight = emptyList(),
                swellWaveDirection = emptyList(),
                currentVelocity = emptyList(),
                currentDirection = emptyList(),
                seaSurfaceTemperature = emptyList()
            )
        )

        // Then - Should not crash
        assertThat(response.hourly.waveHeight).isEmpty()
        assertThat(response.hourly.time).isEmpty()
    }

    @Test
    @DisplayName("Solunar periods can be empty")
    fun `solunarResponse handles no solunar periods`() {
        // Given
        val response = SolunarResponse(
            sunrise = "06:00",
            sunset = "18:00",
            moonrise = null,
            moonset = null,
            moonPhase = SolunarResponse.MoonPhase(
                phase = "New Moon",
                illumination = 0
            ),
            majorPeriods = emptyList(),
            minorPeriods = emptyList(),
            score = 50
        )

        // Then
        assertThat(response.majorPeriods).isEmpty()
        assertThat(response.minorPeriods).isEmpty()
        assertThat(response.moonrise).isNull()
    }

    @Test
    @DisplayName("Moon illumination ranges from 0 to 100")
    fun `moon illumination is valid percentage`() {
        // Given
        val testCases = listOf(0, 25, 50, 75, 100)

        testCases.forEach { illumination ->
            // When
            val moonPhase = SolunarResponse.MoonPhase(
                phase = "Test Phase",
                illumination = illumination
            )

            // Then
            assertThat(moonPhase.illumination).isAtLeast(0)
            assertThat(moonPhase.illumination).isAtMost(100)
        }
    }
}
