package com.fishing.conditions.data.habitat

import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("HabitatSuitabilityEngine Tests")
class HabitatSuitabilityEngineTest {

    private lateinit var engine: HabitatSuitabilityEngine
    private lateinit var bathymetryRepository: BathymetryRepository
    private lateinit var structureAnalyzer: StructureAnalyzer
    private lateinit var currentInteractionModel: CurrentInteractionModel
    private lateinit var temperatureGradientAnalyzer: TemperatureGradientAnalyzer

    @BeforeEach
    fun setup() {
        bathymetryRepository = BathymetryRepository()
        structureAnalyzer = StructureAnalyzer()
        currentInteractionModel = CurrentInteractionModel()
        temperatureGradientAnalyzer = TemperatureGradientAnalyzer()

        engine = HabitatSuitabilityEngine(
            bathymetryRepository,
            structureAnalyzer,
            currentInteractionModel,
            temperatureGradientAnalyzer
        )
    }

    @Test
    @DisplayName("Should calculate HSI for given conditions")
    fun `calculateHSI returns valid habitat suitability index`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()

        // When
        val result = engine.calculateHSI(40.05, -73.95, conditions, species)

        // Then
        assertThat(result.hsi).isAtLeast(0.0)
        assertThat(result.hsi).isAtMost(1.0)
        assertThat(result.components).hasSize(6)
        assertThat(result.components.values.all { it in 0.0..1.0 }).isTrue()
    }

    @Test
    @DisplayName("Should assign correct rating based on HSI")
    fun `calculateHSI assigns correct rating`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()

        // When
        val result = engine.calculateHSI(40.05, -73.95, conditions, species)

        // Then - verify rating is consistent with HSI value using engine thresholds
        val expectedRating = when {
            result.hsi >= 0.7 -> HabitatSuitabilityEngine.HabitatSuitabilityIndex.Rating.EXCELLENT
            result.hsi >= 0.5 -> HabitatSuitabilityEngine.HabitatSuitabilityIndex.Rating.GOOD
            result.hsi >= 0.3 -> HabitatSuitabilityEngine.HabitatSuitabilityIndex.Rating.FAIR
            else -> HabitatSuitabilityEngine.HabitatSuitabilityIndex.Rating.POOR
        }
        assertThat(result.rating).isEqualTo(expectedRating)
    }

    @Test
    @DisplayName("Should include all component scores")
    fun `calculateHSI includes all component scores`() = runTest {
        // Given
        val conditions = createTestConditions()
        val species = createTestSpecies()

        // When
        val result = engine.calculateHSI(40.05, -73.95, conditions, species)

        // Then
        assertThat(result.components).containsKey("structure")
        assertThat(result.components).containsKey("temperatureGradient")
        assertThat(result.components).containsKey("currentInteraction")
        assertThat(result.components).containsKey("depthMatch")
        assertThat(result.components).containsKey("environmental")
        assertThat(result.components).containsKey("solunar")
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
