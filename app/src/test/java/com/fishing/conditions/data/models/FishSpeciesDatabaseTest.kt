package com.fishing.conditions.data.models

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class FishSpeciesDatabaseTest {

    @Test
    fun `species database is populated`() {
        // When
        val species = FishSpeciesDatabase.getAllSpecies()

        // Then
        assertThat(species).isNotEmpty()
    }

    @Test
    fun `species ids are unique`() {
        // When
        val ids = FishSpeciesDatabase.getAllSpecies().map { it.id }

        // Then
        assertThat(ids.distinct().size).isEqualTo(ids.size)
    }

    @Test
    fun `temperature ranges are ordered`() {
        // When
        FishSpeciesDatabase.getAllSpecies().forEach { s ->
            // Then
            assertThat(s.preferredWaterTemp.min).isLessThan(s.preferredWaterTemp.max)
        }
    }
}
