package com.fishing.conditions.data.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested

@DisplayName("Marine Conditions Tests")
class MarineConditionsTest {

    @Nested
    @DisplayName("Fishing Suitability Calculator")
    inner class FishingSuitabilityTest {

        private fun createTestConditions(
            waterTemp: Double = 21.0,
            windSpeed: Double = 10.0,
            waveHeight: Double = 0.5,
            moonPhase: Species.MoonPhase = Species.MoonPhase.FULL_MOON,
            tidePhase: Species.TidePhase = Species.TidePhase.RISING_TIDE,
            solunarScore: Int = 4
        ): MarineConditions {
            return MarineConditions(
                latitude = 37.7749,
                longitude = -122.4194,
                timestamp = System.currentTimeMillis(),
                waterTemperature = waterTemp,
                waveHeight = waveHeight,
                waveDirection = null,
                wavePeriod = null,
                currentSpeed = null,
                currentDirection = null,
                windSpeed = windSpeed,
                windDirection = null,
                windGust = null,
                airTemperature = null,
                pressure = null,
                humidity = null,
                cloudCover = null,
                visibility = null,
                precipitation = null,
                tideHeight = null,
                nextHighTide = null,
                nextLowTide = null,
                currentTidePhase = tidePhase,
                sunrise = null,
                sunset = null,
                moonrise = null,
                moonset = null,
                moonPhase = moonPhase,
                moonIllumination = null,
                majorPeriods = null,
                minorPeriods = null,
                solunarScore = solunarScore,
                dataSource = "Test",
                forecastHours = 0
            )
        }

        @Test
        @DisplayName("Should calculate excellent suitability for optimal redfish conditions")
        fun `optimal conditions for redfish result in excellent rating`() {
            // Given
            val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
            val conditions = createTestConditions(
                waterTemp = 21.0, // Optimal for redfish
                windSpeed = 10.0,
                waveHeight = 0.5,
                moonPhase = Species.MoonPhase.FULL_MOON,
                tidePhase = Species.TidePhase.RISING_TIDE,
                solunarScore = 4
            )

            // When
            val suitability = conditions.getFishingSuitability(redfish)

            // Then
            assertThat(suitability.score).isAtLeast(75f)
            assertThat(suitability.rating).isEqualTo(FishingSuitability.Rating.EXCELLENT)
        }

        @Test
        @DisplayName("Should calculate poor suitability for wrong temperature")
        fun `wrong temperature results in poor rating`() {
            // Given
            val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
            val conditions = createTestConditions(
                waterTemp = 5.0, // Too cold for redfish
                windSpeed = 10.0,
                waveHeight = 0.5
            )

            // When
            val suitability = conditions.getFishingSuitability(redfish)

            // Then
            assertThat(suitability.score).isLessThan(70f)
            assertThat(suitability.rating).isNotEqualTo(FishingSuitability.Rating.EXCELLENT)
        }

        @Test
        @DisplayName("Should include all scoring factors")
        fun `suitability includes all factors`() {
            // Given
            val wahoo = FishSpeciesDatabase.getSpeciesById("wahoo")!!
            val conditions = createTestConditions(
                waterTemp = 26.0,
                windSpeed = 15.0,
                waveHeight = 1.0,
                moonPhase = Species.MoonPhase.FULL_MOON,
                tidePhase = Species.TidePhase.RISING_TIDE,
                solunarScore = 4
            )

            // When
            val suitability = conditions.getFishingSuitability(wahoo)

            // Then
            assertThat(suitability.factors).containsKey("Water Temperature")
            assertThat(suitability.factors).containsKey("Wind Conditions")
            assertThat(suitability.factors).containsKey("Wave Conditions")
            assertThat(suitability.factors).containsKey("Moon Phase")
            assertThat(suitability.factors).containsKey("Tide Phase")
            assertThat(suitability.factors).containsKey("Solunar Activity")
        }

        @Test
        @DisplayName("Should normalize score to 0-100 range")
        fun `score is always between 0 and 100`() {
            // Given
            val species = FishSpeciesDatabase.getAllSpecies()
            val conditions = createTestConditions()

            // When/Then
            species.forEach { fish ->
                val suitability = conditions.getFishingSuitability(fish)
                assertThat(suitability.score).isAtLeast(0f)
                assertThat(suitability.score).isAtMost(100f)
            }
        }

        @Test
        @DisplayName("Should give higher score for optimal vs suboptimal conditions")
        fun `optimal conditions score higher than suboptimal`() {
            // Given
            val kingMackerel = FishSpeciesDatabase.getSpeciesById("king_mackerel")!!

            val optimalConditions = createTestConditions(
                waterTemp = 25.0, // Optimal
                windSpeed = 10.0,
                waveHeight = 1.0,
                moonPhase = Species.MoonPhase.FULL_MOON,
                tidePhase = Species.TidePhase.RISING_TIDE,
                solunarScore = 4
            )

            val suboptimalConditions = createTestConditions(
                waterTemp = 22.0, // Min range
                windSpeed = 20.0,
                waveHeight = 1.8,
                moonPhase = Species.MoonPhase.WAXING_CRESCENT,
                tidePhase = Species.TidePhase.LOW_TIDE,
                solunarScore = 1
            )

            // When
            val optimalScore = optimalConditions.getFishingSuitability(kingMackerel).score
            val suboptimalScore = suboptimalConditions.getFishingSuitability(kingMackerel).score

            // Then
            assertThat(optimalScore).isGreaterThan(suboptimalScore)
        }
    }
}
