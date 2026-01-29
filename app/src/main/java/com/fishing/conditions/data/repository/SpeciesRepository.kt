package com.fishing.conditions.data.repository

import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.Species
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesRepository @Inject constructor() {

    fun getAllSpecies(): List<Species> {
        return FishSpeciesDatabase.getAllSpecies()
    }

    fun getSpeciesById(id: String): Species? {
        return FishSpeciesDatabase.getSpeciesById(id)
    }

    fun getSpeciesByCategory(category: Species.FishCategory): List<Species> {
        return FishSpeciesDatabase.getSpeciesByCategory(category)
    }
}
