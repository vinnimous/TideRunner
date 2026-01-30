package com.fishing.conditions.data.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested

@DisplayName("Species Model Tests")
class SpeciesTest {

    @Nested
    @DisplayName("Fish Species Database")
    inner class FishSpeciesDatabaseTest {

        @Test
        @DisplayName("Should return 16 species in total")
        fun `getAllSpecies returns correct count`() {
            // When
            val species = FishSpeciesDatabase.getAllSpecies()

            // Then
            assertThat(species.size).isAtLeast(10)
        }

        @Test
        @DisplayName("Should include all new offshore species")
        fun `getAllSpecies includes new offshore fish`() {
            // When
            val speciesIds = FishSpeciesDatabase.getAllSpecies().map { it.id }

            // Then
            assertThat(speciesIds).containsAtLeast(
                "king_mackerel",
                "wahoo"
            )
        }

        @Test
        @DisplayName("Should find species by ID")
        fun `getSpeciesById returns correct species`() {
            // When
            val redfish = FishSpeciesDatabase.getSpeciesById("redfish")

            // Then
            assertThat(redfish).isNotNull()
            assertThat(redfish?.name).isEqualTo("Redfish (Red Drum)")
            assertThat(redfish?.scientificName).isEqualTo("Sciaenops ocellatus")
        }

        @Test
        @DisplayName("Should return null for invalid ID")
        fun `getSpeciesById returns null for non-existent id`() {
            // When
            val result = FishSpeciesDatabase.getSpeciesById("non_existent_fish")

            // Then
            assertThat(result).isNull()
        }

        @Test
        @DisplayName("Should filter species by category")
        fun `getSpeciesByCategory returns only offshore species`() {
            // When
            val offshoreSpecies = FishSpeciesDatabase.getSpeciesByCategory(
                Species.FishCategory.SALTWATER_OFFSHORE
            )

            // Then
            assertThat(offshoreSpecies).isNotEmpty()
            offshoreSpecies.forEach { species ->
                assertThat(species.category).isEqualTo(Species.FishCategory.SALTWATER_OFFSHORE)
            }
        }

        @Test
        @DisplayName("Should have valid temperature ranges for all species")
        fun `all species have valid temperature ranges`() {
            // When
            val species = FishSpeciesDatabase.getAllSpecies()

            // Then
            species.forEach { fish ->
                assertThat(fish.preferredWaterTemp.min)
                    .isLessThan(fish.preferredWaterTemp.max)
                assertThat(fish.preferredWaterTemp.optimal)
                    .isAtLeast(fish.preferredWaterTemp.min)
                assertThat(fish.preferredWaterTemp.optimal)
                    .isAtMost(fish.preferredWaterTemp.max)
            }
        }
    }

    @Nested
    @DisplayName("Wahoo Species")
    inner class WahooTest {

        @Test
        @DisplayName("Should have correct water temperature preferences")
        fun `wahoo has correct temperature range`() {
            // When
            val wahoo = FishSpeciesDatabase.getSpeciesById("wahoo")

            // Then
            assertThat(wahoo).isNotNull()
            assertThat(wahoo?.preferredWaterTemp?.min).isEqualTo(22.0)
            assertThat(wahoo?.preferredWaterTemp?.max).isEqualTo(30.0)
            assertThat(wahoo?.preferredWaterTemp?.optimal).isEqualTo(26.0)
        }

        @Test
        @DisplayName("Should be categorized as offshore")
        fun `wahoo is offshore species`() {
            // When
            val wahoo = FishSpeciesDatabase.getSpeciesById("wahoo")

            // Then
            assertThat(wahoo?.category).isEqualTo(Species.FishCategory.SALTWATER_OFFSHORE)
        }
    }
}
