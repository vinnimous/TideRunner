package com.fishing.conditions.ui.components

import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class FishingTimesGraphTest {

    @Test
    fun `solunar periods list can be empty`() {
        val conditions = MarineConditions(
            latitude = 0.0,
            longitude = 0.0,
            timestamp = 0L,
            waterTemperature = null,
            waveHeight = null,
            waveDirection = null,
            wavePeriod = null,
            currentSpeed = null,
            currentDirection = null,
            windSpeed = null,
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
            currentTidePhase = null,
            sunrise = null,
            sunset = null,
            moonrise = null,
            moonset = null,
            moonPhase = null,
            moonIllumination = null,
            majorPeriods = emptyList(),
            minorPeriods = emptyList(),
            solunarScore = null,
            dataSource = "test",
            forecastHours = 0
        )

        assertThat(conditions.majorPeriods).isEmpty()
        assertThat(conditions.minorPeriods).isEmpty()
    }

    @Test
    fun `species lookup returns data`() {
        val species = FishSpeciesDatabase.getSpeciesById("redfish")
        assertThat(species).isNotNull()
        assertThat(species?.preferredMoonPhase).isNotEmpty()
    }
}
