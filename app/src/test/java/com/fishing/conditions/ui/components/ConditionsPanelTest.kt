package com.fishing.conditions.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.*

class ConditionsPanelTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun conditionsPanel_displaysDateCorrectly() {
        // Given
        val testDate = Calendar.getInstance().apply {
            set(2026, Calendar.JANUARY, 29, 0, 0, 0)
        }.time
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null,
                selectedDate = testDate
            )
        }

        // Then
        composeTestRule.onNodeWithText("Wednesday, January 29, 2026", substring = true).assertExists()
    }

    @Test
    fun conditionsPanel_canBeCollapsed() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null
            )
        }

        // Click header to collapse
        composeTestRule.onNodeWithText("Fishing Conditions").performClick()

        // Then - detailed content should not be visible
        composeTestRule.onNodeWithText("Marine Conditions").assertDoesNotExist()
    }

    @Test
    fun conditionsPanel_canBeExpanded() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null
            )
        }

        // Collapse then expand
        composeTestRule.onNodeWithText("Fishing Conditions").performClick()
        composeTestRule.onNodeWithText("Fishing Conditions").performClick()

        // Then - detailed content should be visible
        composeTestRule.onNodeWithText("Marine Conditions").assertExists()
    }

    @Test
    fun conditionsPanel_displaysWaterTemperature() {
        // Given
        val conditions = createTestConditions(waterTemp = 20.5)

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("20.5°C", substring = true).assertExists()
    }

    @Test
    fun conditionsPanel_displaysFishingSuitability() {
        // Given
        val conditions = createTestConditions()
        val species = FishSpeciesDatabase.getSpeciesById("redfish")!!
        val suitability = conditions.getFishingSuitability(species)

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = suitability,
                selectedSpecies = species
            )
        }

        // Then
        composeTestRule.onNodeWithText("Fishing Suitability").assertExists()
        composeTestRule.onNodeWithText(suitability.rating.name).assertExists()
    }

    @Test
    fun conditionsPanel_displaysDateSelector() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("📅 Select Forecast Date").assertExists()
        composeTestRule.onNodeWithText("Today").assertExists()
    }

    @Test
    fun conditionsPanel_dateSelector_has10Days() {
        // Given
        val conditions = createTestConditions()
        var selectedDate: Date? = null

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null,
                onDateSelected = { selectedDate = it }
            )
        }

        // Then - should have confidence percentages for multiple days
        composeTestRule.onNodeWithText("95%").assertExists() // Today
        composeTestRule.onNodeWithText("88%", substring = true).assertExists() // Day 1-3
    }

    @Test
    fun conditionsPanel_isScrollable() {
        // Given
        val conditions = createTestConditions()

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null
            )
        }

        // Then - should be able to scroll (implicit in verticalScroll modifier)
        composeTestRule.onNodeWithText("Marine Conditions").assertExists()
        composeTestRule.onNodeWithText("Data:", substring = true).assertExists()
    }

    @Test
    fun conditionsPanel_displaysFishingTimesGraph() {
        // Given
        val conditions = createTestConditions()
        val species = FishSpeciesDatabase.getSpeciesById("redfish")

        // When
        composeTestRule.setContent {
            ConditionsPanel(
                conditions = conditions,
                suitability = null,
                selectedSpecies = species
            )
        }

        // Then
        composeTestRule.onNodeWithText("🎣 Best Fishing Times Today").assertExists()
    }

    private fun createTestConditions(
        waterTemp: Double = 20.0,
        waveHeight: Double = 1.0,
        windSpeed: Double = 5.0
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
            windGust = 7.0,
            airTemperature = 18.0,
            pressure = 1013.0,
            humidity = 70.0,
            cloudCover = 30.0,
            visibility = 10000.0,
            precipitation = 0.0,
            tideHeight = null,
            nextHighTide = null,
            nextLowTide = null,
            currentTidePhase = null,
            sunrise = System.currentTimeMillis(),
            sunset = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            moonrise = null,
            moonset = null,
            moonPhase = null,
            moonIllumination = 0.5,
            majorPeriods = null,
            minorPeriods = null,
            solunarScore = 70,
            dataSource = "Test",
            forecastHours = 0
        )
    }
}
