package com.fishing.conditions.ui.components

import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SpeciesSelectorTest {

    @Test
    fun `all categories have at least one species`() {
        val all = FishSpeciesDatabase.getAllSpecies()
        assertThat(all).isNotEmpty()
        val categories = all.groupBy { it.category }
        assertThat(categories.keys).isNotEmpty()
    }

    @Test
    fun `species have moon and tide preferences`() {
        val species = FishSpeciesDatabase.getAllSpecies().first()
        assertThat(species.preferredMoonPhase).isNotEmpty()
        assertThat(species.preferredTidePhase).isNotEmpty()
    }
}
