package com.fishing.conditions.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.fishing.conditions.data.models.FishSpeciesDatabase
import org.junit.Rule
import org.junit.Test

class SpeciesSelectorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun speciesSelector_displaysSelectedSpecies() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!
        var selectedSpecies = redfish

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = selectedSpecies,
                onSpeciesSelected = { selectedSpecies = it }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Redfish (Red Drum)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sciaenops ocellatus").assertIsDisplayed()
    }

    @Test
    fun speciesSelector_showsPromptWhenNoSpeciesSelected() {
        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = { }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Select a fish species").assertIsDisplayed()
    }

    @Test
    fun speciesSelector_opensDropdownOnClick() {
        // Given
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = { }
            )
        }

        // When
        composeTestRule.onNodeWithText("Select a fish species").performClick()

        // Then - should show category headers
        composeTestRule.onNodeWithText("Saltwater - Inshore").assertIsDisplayed()
        composeTestRule.onNodeWithText("Saltwater - Offshore").assertIsDisplayed()
    }

    @Test
    fun speciesSelector_displaysAllNewSpecies() {
        // Given
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = { }
            )
        }

        // When
        composeTestRule.onNodeWithText("Select a fish species").performClick()

        // Then - verify new species are in the list
        composeTestRule.onNodeWithText("Atlantic Bluefin Tuna").assertExists()
        composeTestRule.onNodeWithText("Blackfin Tuna").assertExists()
        composeTestRule.onNodeWithText("Spanish Mackerel").assertExists()
        composeTestRule.onNodeWithText("King Mackerel (Kingfish)").assertExists()
        composeTestRule.onNodeWithText("Wahoo").assertExists()
    }

    @Test
    fun speciesSelector_selectsSpeciesOnClick() {
        // Given
        var selectedSpecies: com.fishing.conditions.data.models.Species? = null
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = selectedSpecies,
                onSpeciesSelected = { selectedSpecies = it }
            )
        }

        // When
        composeTestRule.onNodeWithText("Select a fish species").performClick()
        composeTestRule.onNodeWithText("Wahoo").performClick()

        // Then
        composeTestRule.waitUntil(timeoutMillis = 1000) {
            selectedSpecies?.id == "wahoo"
        }
        composeTestRule.onNodeWithText("Wahoo").assertIsDisplayed()
    }
}
