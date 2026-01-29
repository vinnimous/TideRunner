package com.fishing.conditions.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.Species
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class SpeciesSelectorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun speciesSelector_displaysPromptWhenNoSelection() {
        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Select a fish species").assertExists()
    }

    @Test
    fun speciesSelector_displaysSelectedSpecies() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = redfish,
                onSpeciesSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Redfish").assertExists()
        composeTestRule.onNodeWithText(redfish.scientificName).assertExists()
    }

    @Test
    fun speciesSelector_expandsDropdownOnClick() {
        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = {}
            )
        }

        // Click to expand
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Then - category headers should be visible
        composeTestRule.onNodeWithText("Saltwater - Inshore").assertExists()
        composeTestRule.onNodeWithText("Saltwater - Offshore").assertExists()
    }

    @Test
    fun speciesSelector_selectsSpeciesOnClick() {
        // Given
        var selectedSpecies: Species? = null

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = { selectedSpecies = it }
            )
        }

        // Expand dropdown
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Click on Redfish
        composeTestRule.onNodeWithText("Redfish").performClick()

        // Then
        assertThat(selectedSpecies?.id).isEqualTo("redfish")
    }

    @Test
    fun speciesSelector_displaysOptimalConditions() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = redfish,
                onSpeciesSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Optimal Conditions").assertExists()
        composeTestRule.onNodeWithText("Water Temp", substring = true).assertExists()
        composeTestRule.onNodeWithText("Depth", substring = true).assertExists()
    }

    @Test
    fun speciesSelector_displaysAllCategories() {
        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = {}
            )
        }

        // Expand dropdown
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Then - all categories should be present
        composeTestRule.onNodeWithText("Saltwater - Inshore").assertExists()
        composeTestRule.onNodeWithText("Saltwater - Offshore").assertExists()
        composeTestRule.onNodeWithText("Freshwater").assertExists()
    }

    @Test
    fun speciesSelector_isScrollable() {
        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = {}
            )
        }

        // Expand dropdown
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Then - LazyColumn makes it scrollable, all species should be accessible
        // Check for species from different categories
        composeTestRule.onNodeWithText("Redfish").assertExists()
        composeTestRule.onNodeWithText("Yellowfin Tuna").assertExists()
    }

    @Test
    fun speciesSelector_displaysCheckmarkOnSelected() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = redfish,
                onSpeciesSelected = {}
            )
        }

        // Expand dropdown
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Then - checkmark icon should be present next to selected species
        composeTestRule.onNodeWithContentDescription("Selected").assertExists()
    }

    @Test
    fun speciesSelector_displaysSpeciesDescription() {
        // Given
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")!!

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = redfish,
                onSpeciesSelected = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(redfish.description, substring = true).assertExists()
    }

    @Test
    fun speciesSelector_displaysAllSpecies() {
        // Given
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // When
        composeTestRule.setContent {
            SpeciesSelector(
                selectedSpecies = null,
                onSpeciesSelected = {}
            )
        }

        // Expand dropdown
        composeTestRule.onNodeWithText("Target Species").performClick()

        // Then - verify multiple species are present
        assertThat(allSpecies.size).isGreaterThan(10)
        composeTestRule.onNodeWithText("Redfish").assertExists()
        composeTestRule.onNodeWithText("Atlantic Bluefin Tuna").assertExists()
        composeTestRule.onNodeWithText("Largemouth Bass").assertExists()
    }
}
