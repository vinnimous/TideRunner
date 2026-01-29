package com.fishing.conditions.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.MarineConditions
import org.junit.Rule
import org.junit.Test

class FishingTimesGraphTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun fishingTimesGraph_displaysTitle() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("🎣 Best Fishing Times Today").assertExists()
    }

    @Test
    fun fishingTimesGraph_displaysSpeciesName() {
        // Given
        val conditions = createTestConditions()
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = redfish
            )
        }

        // Then
        composeTestRule.onNodeWithText(redfish.name).assertExists()
    }

    @Test
    fun fishingTimesGraph_displaysTimeLabels() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("12 AM").assertExists()
        composeTestRule.onNodeWithText("6 AM").assertExists()
        composeTestRule.onNodeWithText("12 PM").assertExists()
        composeTestRule.onNodeWithText("6 PM").assertExists()
    }

    @Test
    fun fishingTimesGraph_displaysLegend() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("Fishing Score").assertExists()
        composeTestRule.onNodeWithText("Major Period").assertExists()
        composeTestRule.onNodeWithText("High Tide").assertExists()
        composeTestRule.onNodeWithText("Now").assertExists()
    }

    @Test
    fun fishingTimesGraph_displaysPeakTimes() {
        // Given
        val now = System.currentTimeMillis()
        val conditions = createTestConditions(
            majorPeriods = listOf(
                MarineConditions.SolunarPeriod(
                    startTime = now + 6 * 60 * 60 * 1000, // 6 AM
                    endTime = now + 8 * 60 * 60 * 1000, // 8 AM
                    type = "major"
                )
            )
        )

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("🌟 Peak Times:", substring = true).assertExists()
    }

    @Test
    fun fishingTimesGraph_handlesNullSolunarData() {
        // Given
        val conditions = createTestConditions(
            majorPeriods = null,
            minorPeriods = null
        )

        // When
        composeTestRule.setContent {
            FishingTimesGraph(
                conditions = conditions,
                species = null
            )
        }

        // Then - should still render without crashing
        composeTestRule.onNodeWithText("🎣 Best Fishing Times Today").assertExists()
    }

    private fun createTestConditions(
        majorPeriods: List<MarineConditions.SolunarPeriod>? = emptyList(),
        minorPeriods: List<MarineConditions.SolunarPeriod>? = emptyList()
    ): MarineConditions {
        val now = System.currentTimeMillis()
        return MarineConditions(
            latitude = 37.7749,
            longitude = -122.4194,
            timestamp = now,
            waterTemperature = 20.0,
            waveHeight = 1.0,
            waveDirection = 180.0,
            wavePeriod = 5.0,
            currentSpeed = 0.5,
            currentDirection = 90.0,
            windSpeed = 5.0,
            windDirection = 270.0,
            windGust = 7.0,
            airTemperature = 18.0,
            pressure = 1013.0,
            humidity = 70.0,
            cloudCover = 30.0,
            visibility = 10000.0,
            precipitation = 0.0,
            tideHeight = null,
            nextHighTide = MarineConditions.TideEvent(
                time = now + 4 * 60 * 60 * 1000,
                height = 2.5,
                type = "high"
            ),
            nextLowTide = MarineConditions.TideEvent(
                time = now + 10 * 60 * 60 * 1000,
                height = 0.5,
                type = "low"
            ),
            currentTidePhase = null,
            sunrise = now + 6 * 60 * 60 * 1000,
            sunset = now + 18 * 60 * 60 * 1000,
            moonrise = null,
            moonset = null,
            moonPhase = null,
            moonIllumination = 0.5,
            majorPeriods = majorPeriods,
            minorPeriods = minorPeriods,
            solunarScore = 70,
            dataSource = "Test",
            forecastHours = 0
        )
    }
}
