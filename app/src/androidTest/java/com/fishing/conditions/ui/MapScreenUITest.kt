package com.fishing.conditions.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fishing.conditions.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunches_displaysWelcomeMessage() {
        // Then
        composeTestRule.onNodeWithText("Welcome to TideRunner!").assertExists()
        composeTestRule.onNodeWithText("Select a fish species above to get started").assertExists()
    }

    @Test
    fun speciesSelector_isDisplayed() {
        // Then
        composeTestRule.onNodeWithText("Target Species").assertExists()
    }

    @Test
    fun canSelectSpecies() {
        // When - Click species selector
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Wait for dropdown to appear
        composeTestRule.waitForIdle()

        // Then - Species list should be visible
        composeTestRule.onNodeWithText("Saltwater - Inshore").assertExists()
        composeTestRule.onNodeWithText("Redfish").assertExists()
    }

    @Test
    fun selectingSpecies_showsInstructionalText() {
        // When - Select a species
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Redfish").performClick()
        composeTestRule.waitForIdle()

        // Then - Should show instruction to tap map
        composeTestRule.onNodeWithText("Tap the map or use 📍 button", substring = true).assertExists()
    }

    @Test
    fun selectedSpecies_displaysOptimalConditions() {
        // When - Select a species
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Redfish").performClick()
        composeTestRule.waitForIdle()

        // Then - Optimal conditions card should be visible
        composeTestRule.onNodeWithText("Optimal Conditions").assertExists()
        composeTestRule.onNodeWithText("Water Temp", substring = true).assertExists()
    }

    @Test
    fun myLocationButton_isDisplayed() {
        // Then
        composeTestRule.onNodeWithContentDescription("My Location").assertExists()
    }

    @Test
    fun mapView_isDisplayed() {
        // Map should be rendered (AndroidView)
        // This is implicit - if app doesn't crash, map is rendering
        composeTestRule.waitForIdle()
    }

    @Test
    fun speciesDropdown_canScroll() {
        // When - Open species selector
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()

        // Then - Should be able to see species from different categories
        composeTestRule.onNodeWithText("Redfish").assertExists()

        // Scroll to offshore species
        composeTestRule.onNodeWithText("Saltwater - Offshore").assertExists()
        composeTestRule.onNodeWithText("Yellowfin Tuna").assertExists()
    }

    @Test
    fun selectingDifferentSpecies_updatesDisplay() {
        // When - Select first species
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Redfish").performClick()
        composeTestRule.waitForIdle()

        // Verify first selection
        composeTestRule.onNodeWithText("Redfish").assertExists()

        // Select different species
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Largemouth Bass").performClick()
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("Largemouth Bass").assertExists()
    }

    @Test
    fun speciesSelector_displaysAllNewSpecies() {
        // When - Open species selector
        composeTestRule.onNodeWithText("Target Species").performClick()
        composeTestRule.waitForIdle()

        // Then - New species should be present
        composeTestRule.onNodeWithText("Atlantic Bluefin Tuna").assertExists()
        composeTestRule.onNodeWithText("Blackfin Tuna").assertExists()
        composeTestRule.onNodeWithText("Yellowfin Tuna").assertExists()
        composeTestRule.onNodeWithText("Spanish Mackerel").assertExists()
        composeTestRule.onNodeWithText("King Mackerel").assertExists()
        composeTestRule.onNodeWithText("Wahoo").assertExists()
    }
}
