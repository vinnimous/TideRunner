package com.fishing.conditions.data.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("FishSpeciesDatabase Tests")
class FishSpeciesDatabaseTest {

    @Test
    @DisplayName("Should retrieve species by ID")
    fun `getSpeciesById returns correct species`() {
        // When
        val redfish = FishSpeciesDatabase.getSpeciesById("redfish")

        // Then
        assertThat(redfish).isNotNull()
        assertThat(redfish?.name).isEqualTo("Redfish")
        assertThat(redfish?.id).isEqualTo("redfish")
    }

    @Test
    @DisplayName("Should return null for invalid ID")
    fun `getSpeciesById returns null for invalid id`() {
        // When
        val result = FishSpeciesDatabase.getSpeciesById("invalid_id")

        // Then
        assertThat(result).isNull()
    }

    @Test
    @DisplayName("Should return all species")
    fun `getAllSpecies returns all fish species`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        assertThat(allSpecies).isNotEmpty()
        assertThat(allSpecies.size).isAtLeast(13) // At least 13 species
    }

    @Test
    @DisplayName("Should include all tuna species")
    fun `getAllSpecies includes all tuna species`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        val tunaSpecies = allSpecies.filter { it.name.contains("Tuna") }
        assertThat(tunaSpecies.size).isAtLeast(3) // Atlantic Bluefin, Blackfin, Yellowfin
    }

    @Test
    @DisplayName("Should include all mackerel species")
    fun `getAllSpecies includes mackerel species`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        val mackerelSpecies = allSpecies.filter { it.name.contains("Mackerel") }
        assertThat(mackerelSpecies.size).isAtLeast(2) // Spanish, King
    }

    @Test
    @DisplayName("Should include wahoo")
    fun `getAllSpecies includes wahoo`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        val wahoo = allSpecies.find { it.name == "Wahoo" }
        assertThat(wahoo).isNotNull()
        assertThat(wahoo?.category).isEqualTo(Species.FishCategory.SALTWATER_OFFSHORE)
    }

    @Test
    @DisplayName("Should categorize species correctly")
    fun `species are categorized into inshore, offshore, and freshwater`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        val inshore = allSpecies.filter { it.category == Species.FishCategory.SALTWATER_INSHORE }
        val offshore = allSpecies.filter { it.category == Species.FishCategory.SALTWATER_OFFSHORE }
        val freshwater = allSpecies.filter { it.category == Species.FishCategory.FRESHWATER }

        assertThat(inshore).isNotEmpty()
        assertThat(offshore).isNotEmpty()
        assertThat(freshwater).isNotEmpty()
    }

    @Test
    @DisplayName("All species should have valid temperature ranges")
    fun `all species have valid temperature preferences`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        allSpecies.forEach { species ->
            assertThat(species.preferredWaterTemp.min).isLessThan(species.preferredWaterTemp.max)
            assertThat(species.preferredWaterTemp.optimal).isAtLeast(species.preferredWaterTemp.min)
            assertThat(species.preferredWaterTemp.optimal).isAtMost(species.preferredWaterTemp.max)
        }
    }

    @Test
    @DisplayName("All species should have valid depth ranges")
    fun `all species have valid depth preferences`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        allSpecies.forEach { species ->
            assertThat(species.preferredDepth.min).isAtLeast(0.0)
            assertThat(species.preferredDepth.min).isLessThan(species.preferredDepth.max)
            assertThat(species.preferredDepth.optimal).isAtLeast(species.preferredDepth.min)
            assertThat(species.preferredDepth.optimal).isAtMost(species.preferredDepth.max)
        }
    }

    @Test
    @DisplayName("All species should have descriptions")
    fun `all species have non-empty descriptions`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        allSpecies.forEach { species ->
            assertThat(species.description).isNotEmpty()
        }
    }

    @Test
    @DisplayName("All species should have scientific names")
    fun `all species have scientific names`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()

        // Then
        allSpecies.forEach { species ->
            assertThat(species.scientificName).isNotEmpty()
        }
    }

    @Test
    @DisplayName("All species IDs should be unique")
    fun `all species have unique IDs`() {
        // When
        val allSpecies = FishSpeciesDatabase.getAllSpecies()
        val ids = allSpecies.map { it.id }

        // Then
        assertThat(ids.distinct().size).isEqualTo(ids.size)
    }

    @Test
    @DisplayName("Atlantic Bluefin Tuna should have correct properties")
    fun `atlantic bluefin tuna has correct properties`() {
        // When
        val bluefin = FishSpeciesDatabase.getSpeciesById("atlantic_bluefin_tuna")

        // Then
        assertThat(bluefin).isNotNull()
        assertThat(bluefin?.category).isEqualTo(Species.FishCategory.SALTWATER_OFFSHORE)
        assertThat(bluefin?.preferredWaterTemp?.min).isAtLeast(10.0)
    }

    @Test
    @DisplayName("Spanish Mackerel should exist")
    fun `spanish mackerel exists in database`() {
        // When
        val spanishMackerel = FishSpeciesDatabase.getSpeciesById("spanish_mackerel")

        // Then
        assertThat(spanishMackerel).isNotNull()
        assertThat(spanishMackerel?.name).isEqualTo("Spanish Mackerel")
    }

    @Test
    @DisplayName("King Mackerel should exist")
    fun `king mackerel exists in database`() {
        // When
        val kingMackerel = FishSpeciesDatabase.getSpeciesById("king_mackerel")

        // Then
        assertThat(kingMackerel).isNotNull()
        assertThat(kingMackerel?.name).isEqualTo("King Mackerel")
    }
}
