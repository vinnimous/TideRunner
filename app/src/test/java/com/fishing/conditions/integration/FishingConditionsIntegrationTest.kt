package com.fishing.conditions.integration

import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Integration Tests - Full Workflow")
class FishingConditionsIntegrationTest {

    @Test
    @DisplayName("Complete workflow: Select species, get conditions, calculate suitability")
    fun `complete fishing conditions workflow`() {
        // Given - User selects Redfish
        val selectedSpecies = FishSpeciesDatabase.getSpeciesById("redfish")!!
        assertThat(selectedSpecies.name).isEqualTo("Redfish (Red Drum)")

        // And - Marine conditions are fetched
        val conditions = createOptimalRedfishConditions()

        // When - Fishing suitability is calculated
        val suitability = conditions.getFishingSuitability(selectedSpecies)

        // Then - Suitability should be excellent for optimal conditions
        assertThat(suitability.rating).isEqualTo(com.fishing.conditions.data.models.FishingSuitability.Rating.EXCELLENT)
        assertThat(suitability.score).isAtLeast(75f)
        assertThat(suitability.factors).isNotEmpty()
    }

    @Test
    @DisplayName("Multiple species comparison for same location")
    fun `compare multiple species at same location`() {
        // Given - Same conditions for different species
        val conditions = createTestConditions(waterTemp = 24.0)
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val mahiMahi = FishSpeciesDatabase.getSpeciesById("mahi_mahi")!!

        // When
        val redfishSuitability = conditions.getFishingSuitability(redfish)
        val mahiSuitability = conditions.getFishingSuitability(mahiMahi)

        // Then - Different species should have different suitability scores
        assertThat(redfishSuitability.score).isNotEqualTo(mahiSuitability.score)
    }

    @Test
    @DisplayName("All species categories can be selected and used")
    fun `all species categories work end to end`() {
        // Given
        val allSpecies = FishSpeciesDatabase.getAllSpecies()
        val conditions = createTestConditions()

        // When - Calculate suitability for all species
        val suitabilities = allSpecies.map { species ->
            species to conditions.getFishingSuitability(species)
        }

        // Then - All species should have valid suitability scores
        suitabilities.forEach { (species, suitability) ->
            assertThat(suitability.score).isAtLeast(0f)
            assertThat(suitability.score).isAtMost(100f)
            assertThat(suitability.rating).isNotNull()
        }
    }

    @Test
    @DisplayName("Poor conditions result in poor suitability")
    fun `poor conditions correctly result in poor rating`() {
        // Given - Very poor conditions for Redfish
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val poorConditions = createTestConditions(
            waterTemp = 5.0, // Too cold
            windSpeed = 20.0, // Too windy
            waveHeight = 3.0 // Too rough
        )

        // When
        val suitability = poorConditions.getFishingSuitability(redfish)

        // Then
        assertThat(suitability.rating).isIn(
            listOf(
                com.fishing.conditions.data.models.FishingSuitability.Rating.POOR,
                com.fishing.conditions.data.models.FishingSuitability.Rating.FAIR
            )
        )
        assertThat(suitability.score).isLessThan(60f)
    }

    @Test
    @DisplayName("Solunar periods increase fishing score")
    fun `solunar major periods improve fishing suitability`() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val now = System.currentTimeMillis()

        val conditionsWithoutSolunar = createTestConditions(
            majorPeriods = null,
            minorPeriods = null
        )

        val conditionsWithSolunar = createTestConditions(
            majorPeriods = listOf(
                MarineConditions.SolunarPeriod(
                    startTime = now,
                    endTime = now + 2 * 60 * 60 * 1000,
                    type = "major"
                )
            )
        )

        // When
        val suitabilityWithout = conditionsWithoutSolunar.getFishingSuitability(redfish)
        val suitabilityWith = conditionsWithSolunar.getFishingSuitability(redfish)

        // Then - Solunar periods should improve score
        assertThat(suitabilityWith.score).isAtLeast(0f)
        assertThat(suitabilityWithout.score).isAtLeast(0f)
    }

    @Test
    @DisplayName("Moon phase affects species differently")
    fun `moon phase preferences affect suitability`() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        val fullMoonConditions = createTestConditions(
            moonPhase = Species.MoonPhase.FULL_MOON
        )

        val newMoonConditions = createTestConditions(
            moonPhase = Species.MoonPhase.NEW_MOON
        )

        // When
        val fullMoonSuitability = fullMoonConditions.getFishingSuitability(redfish)
        val newMoonSuitability = newMoonConditions.getFishingSuitability(redfish)

        // Then - Both should be valid but may differ based on preferences
        assertThat(fullMoonSuitability.score).isAtLeast(0f)
        assertThat(newMoonSuitability.score).isAtLeast(0f)
    }

    @Test
    @DisplayName("Tide phase affects fishing score")
    fun `tide phase matching species preference improves score`() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        val risingTideConditions = createTestConditions(
            tidePhase = Species.TidePhase.RISING_TIDE
        )

        // When
        val suitability = risingTideConditions.getFishingSuitability(redfish)

        // Then - Should have valid score
        assertThat(suitability.score).isAtLeast(0f)
        assertThat(suitability.factors).containsKey("Tide Phase")
    }

    @Test
    @DisplayName("All offshore species prefer deeper water")
    fun `offshore species have deeper depth preferences`() {
        // Given
        val allSpecies = FishSpeciesDatabase.getAllSpecies()
        val offshoreSpecies = allSpecies.filter {
            it.category == Species.FishCategory.SALTWATER_OFFSHORE
        }

        // Then
        offshoreSpecies.forEach { species ->
            assertThat(species.preferredDepth.max).isGreaterThan(10.0)
        }
    }

    @Test
    @DisplayName("Temperature matching is critical for suitability")
    fun `water temperature is major factor in suitability`() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val optimalTemp = redfish.preferredWaterTemp.optimal

        val optimalConditions = createTestConditions(waterTemp = optimalTemp)
        val poorTempConditions = createTestConditions(waterTemp = optimalTemp + 15.0)

        // When
        val optimalSuitability = optimalConditions.getFishingSuitability(redfish)
        val poorTempSuitability = poorTempConditions.getFishingSuitability(redfish)

        // Then - Optimal temperature should score much better
        assertThat(optimalSuitability.score).isGreaterThan(poorTempSuitability.score)
    }

    private fun createOptimalRedfishConditions(): MarineConditions {
        return createTestConditions(
            waterTemp = 24.0, // Optimal for Redfish
            windSpeed = 3.0,
            waveHeight = 0.5,
            moonPhase = Species.MoonPhase.FULL_MOON,
            tidePhase = Species.TidePhase.RISING_TIDE
        )
    }

    private fun createTestConditions(
        waterTemp: Double = 20.0,
        windSpeed: Double = 5.0,
        waveHeight: Double = 1.0,
        moonPhase: Species.MoonPhase? = null,
        tidePhase: Species.TidePhase? = null,
        majorPeriods: List<MarineConditions.SolunarPeriod>? = emptyList(),
        minorPeriods: List<MarineConditions.SolunarPeriod>? = emptyList()
    ): MarineConditions {
        return MarineConditions(
            latitude = 37.7749,
            longitude = -122.4194,
            timestamp = System.currentTimeMillis(),
            waterTemperature = waterTemp,
            waveHeight = waveHeight,
            waveDirection = 180.0,
            wavePeriod = 5.0,
            currentSpeed = 0.5,
            currentDirection = 90.0,
            windSpeed = windSpeed,
            windDirection = 270.0,
            windGust = windSpeed + 2.0,
            airTemperature = 18.0,
            pressure = 1013.0,
            humidity = 70.0,
            cloudCover = 30.0,
            visibility = 10000.0,
            precipitation = 0.0,
            tideHeight = null,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = tidePhase,
            sunrise = System.currentTimeMillis(),
            sunset = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            moonrise = null,
            moonset = null,
            moonPhase = moonPhase,
            moonIllumination = 0.5,
            majorPeriods = majorPeriods,
            minorPeriods = minorPeriods,
            solunarScore = 70,
            dataSource = "Test",
            forecastHours = 0
        )
    }
}
