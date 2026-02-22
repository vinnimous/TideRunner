package com.fishing.conditions.ui.components

import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ConditionsPanelTest {

    @Test
    fun `can compute suitability without crashing`() {
        val species = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val conditions = MarineConditions(
            latitude = 37.0,
            longitude = -122.0,
            timestamp = 1_700_000_000L,
            waterTemperature = 18.0,
            waveHeight = 1.0,
            waveDirection = null,
            wavePeriod = null,
            currentSpeed = null,
            currentDirection = null,
            windSpeed = 5.0,
            windDirection = 270.0,
            windGust = null,
            airTemperature = 19.0,
            pressure = 1013.0,
            humidity = 70.0,
            cloudCover = 30.0,
            visibility = 10.0,
            precipitation = 0.0,
            tideHeight = 0.5,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = Species.TidePhase.RISING_TIDE,
            sunrise = 1_700_000_000L,
            sunset = 1_700_010_000L,
            moonrise = null,
            moonset = null,
            moonPhase = Species.MoonPhase.FULL_MOON,
            moonIllumination = 100.0,
            majorPeriods = emptyList(),
            minorPeriods = emptyList(),
            solunarScore = 4,
            dataSource = "test",
            forecastHours = 24
        )

        val suitability = conditions.getFishingSuitability(species)
        assertThat(suitability.score).isAtLeast(0f)
    }
}
