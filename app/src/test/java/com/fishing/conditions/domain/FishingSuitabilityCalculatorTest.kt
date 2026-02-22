package com.fishing.conditions.domain

import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("FishingSuitabilityCalculator Tests")
class FishingSuitabilityCalculatorTest {

    private lateinit var calculator: FishingSuitabilityCalculator
    private lateinit var habitatEngine: com.fishing.conditions.data.habitat.HabitatSuitabilityEngine

    @BeforeEach
    fun setup() {
        habitatEngine = mockk()
        calculator = FishingSuitabilityCalculator(habitatEngine)
    }

    @Test
    @DisplayName("Should calculate suitability with HSI when available")
    fun `calculate returns suitability with HSI when habitat engine succeeds`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()
        val mockHsiResult = com.fishing.conditions.data.habitat.HabitatSuitabilityEngine.HabitatSuitabilityIndex(
            hsi = 0.85,
            components = mapOf(
                "structure" to 0.8,
                "temperatureGradient" to 0.7,
                "currentInteraction" to 0.9,
                "depthMatch" to 0.8,
                "environmental" to 0.9,
                "solunar" to 0.8
            ),
            rating = com.fishing.conditions.data.habitat.HabitatSuitabilityEngine.HabitatSuitabilityIndex.Rating.EXCELLENT
        )

        coEvery { habitatEngine.calculateHSI(any(), any(), any(), any()) } returns mockHsiResult

        // When
        val result = calculator.calculate(conditions, species, 40.05, -73.95)

        // Then - blended score: fsiScore*0.6 + (hsi*100)*0.4; hsi=0.85 → hsi part = 34pts
        assertThat(result.score).isGreaterThan(30f)    // at minimum HSI portion alone > 30
        assertThat(result.score).isAtMost(100f)
        assertThat(result.rating).isAnyOf(
            FishingSuitability.Rating.EXCELLENT,
            FishingSuitability.Rating.GOOD,
            FishingSuitability.Rating.FAIR
        )
        assertThat(result.hsi).isEqualTo(0.85f)
        assertThat(result.hsiComponents).isNotNull()
        assertThat(result.hsiComponents!!["structure"]).isEqualTo(0.8f)
    }

    @Test
    @DisplayName("Should fall back to base calculation when HSI fails")
    fun `calculate falls back to base calculation when HSI fails`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()

        coEvery { habitatEngine.calculateHSI(any(), any(), any(), any()) } throws RuntimeException("HSI failed")

        // When
        val result = calculator.calculate(conditions, species, 40.05, -73.95)

        // Then
        assertThat(result.score).isAtLeast(0f)
        assertThat(result.score).isAtMost(100f)
        assertThat(result.hsi).isNull()
        assertThat(result.hsiComponents).isNull()
    }

    @Test
    @DisplayName("Should include factors from base calculation")
    fun `calculate includes factors from base calculation`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()

        coEvery { habitatEngine.calculateHSI(any(), any(), any(), any()) } throws RuntimeException("HSI failed")

        // When
        val result = calculator.calculate(conditions, species, 40.05, -73.95)

        // Then
        assertThat(result.factors).isNotEmpty()
    }

    private fun createTestConditions(): MarineConditions {
        return MarineConditions(
            latitude = 40.05,
            longitude = -73.95,
            timestamp = System.currentTimeMillis(),
            waterTemperature = 70.0,
            waveHeight = 1.0,
            waveDirection = 180.0,
            wavePeriod = 5.0,
            currentSpeed = 1.0,
            currentDirection = 90.0,
            windSpeed = 10.0,
            windDirection = 270.0,
            windGust = 12.0,
            airTemperature = 65.0,
            pressure = 1013.0,
            humidity = 70.0,
            cloudCover = 30.0,
            visibility = 10000.0,
            precipitation = 0.0,
            tideHeight = 2.0,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = Species.TidePhase.RISING_TIDE,
            sunrise = System.currentTimeMillis(),
            sunset = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            moonrise = null,
            moonset = null,
            moonPhase = Species.MoonPhase.FULL_MOON,
            moonIllumination = 1.0,
            majorPeriods = emptyList(),
            minorPeriods = emptyList(),
            solunarScore = 4,
            dataSource = "Test",
            forecastHours = 0
        )
    }

    private fun createTestSpecies(): Species {
        return Species(
            id = "test_fish",
            name = "Test Fish",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(65.0, 75.0, 70.0),
            preferredDepth = Species.DepthRange(10.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            preferredCurrentSpeed = Species.CurrentRange(0.5, 2.0),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.5, 2.0),
                temperatureBreakSensitivity = 0.7,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.REEF
            )
        )
    }
}
