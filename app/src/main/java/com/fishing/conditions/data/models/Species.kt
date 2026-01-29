package com.fishing.conditions.data.models

data class Species(
    val id: String,
    val name: String,
    val scientificName: String,
    val category: FishCategory,
    val preferredWaterTemp: TemperatureRange,
    val preferredDepth: DepthRange,
    val preferredWindSpeed: WindRange,
    val preferredWaveHeight: WaveRange,
    val preferredMoonPhase: List<MoonPhase>,
    val preferredTidePhase: List<TidePhase>,
    val description: String,
    val iconResId: Int? = null
) {
    data class TemperatureRange(val min: Double, val max: Double, val optimal: Double)
    data class DepthRange(val min: Double, val max: Double)
    data class WindRange(val min: Double, val max: Double)
    data class WaveRange(val min: Double, val max: Double)

    enum class FishCategory {
        SALTWATER_INSHORE,
        SALTWATER_OFFSHORE,
        FRESHWATER,
        MIXED
    }

    enum class MoonPhase {
        NEW_MOON,
        WAXING_CRESCENT,
        FIRST_QUARTER,
        WAXING_GIBBOUS,
        FULL_MOON,
        WANING_GIBBOUS,
        LAST_QUARTER,
        WANING_CRESCENT
    }

    enum class TidePhase {
        HIGH_TIDE,
        LOW_TIDE,
        RISING_TIDE,
        FALLING_TIDE
    }
}

// Common fish species database
object FishSpeciesDatabase {
    fun getAllSpecies(): List<Species> = listOf(
        // Saltwater Inshore
        Species(
            id = "redfish",
            name = "Redfish (Red Drum)",
            scientificName = "Sciaenops ocellatus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(15.0, 27.0, 21.0),
            preferredDepth = Species.DepthRange(0.5, 3.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Active during moving tides, especially in shallow flats and marshes"
        ),
        Species(
            id = "speckled_trout",
            name = "Speckled Trout",
            scientificName = "Cynoscion nebulosus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(15.0, 24.0, 20.0),
            preferredDepth = Species.DepthRange(1.0, 4.0),
            preferredWindSpeed = Species.WindRange(0.0, 12.0),
            preferredWaveHeight = Species.WaveRange(0.0, 0.8),
            preferredMoonPhase = listOf(Species.MoonPhase.FIRST_QUARTER, Species.MoonPhase.LAST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Prefers calm conditions with light wind, active during tidal movement"
        ),
        Species(
            id = "flounder",
            name = "Flounder",
            scientificName = "Paralichthys lethostigma",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(12.0, 22.0, 17.0),
            preferredDepth = Species.DepthRange(0.5, 6.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.5),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.FALLING_TIDE, Species.TidePhase.LOW_TIDE),
            description = "Bottom dweller, active during tide changes and low light conditions"
        ),

        // Saltwater Offshore
        Species(
            id = "mahi_mahi",
            name = "Mahi Mahi (Dolphin)",
            scientificName = "Coryphaena hippurus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(20.0, 29.0, 24.0),
            preferredDepth = Species.DepthRange(10.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.5),
            preferredMoonPhase = listOf(Species.MoonPhase.WAXING_GIBBOUS, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Pelagic species, found near floating debris and current lines"
        ),
        Species(
            id = "tuna",
            name = "Yellowfin Tuna",
            scientificName = "Thunnus albacares",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(18.0, 31.0, 24.0),
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Highly migratory, prefers warm water and strong currents"
        ),
        Species(
            id = "grouper",
            name = "Red Grouper",
            scientificName = "Epinephelus morio",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(16.0, 27.0, 22.0),
            preferredDepth = Species.DepthRange(15.0, 120.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Structure-oriented bottom feeder, active during tidal movement"
        ),
        Species(
            id = "bluefin_tuna",
            name = "Atlantic Bluefin Tuna",
            scientificName = "Thunnus thynnus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(14.0, 25.0, 20.0),
            preferredDepth = Species.DepthRange(20.0, 300.0),
            preferredWindSpeed = Species.WindRange(0.0, 35.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.5),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Largest tuna species, highly migratory, prefers cooler water than yellowfin"
        ),
        Species(
            id = "blackfin_tuna",
            name = "Blackfin Tuna",
            scientificName = "Thunnus atlanticus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(20.0, 30.0, 25.0),
            preferredDepth = Species.DepthRange(10.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.5),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.WAXING_GIBBOUS),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Smaller tuna species, prefers warm Atlantic waters, excellent table fare"
        ),
        Species(
            id = "spanish_mackerel",
            name = "Spanish Mackerel",
            scientificName = "Scomberomorus maculatus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(20.0, 28.0, 24.0),
            preferredDepth = Species.DepthRange(1.0, 30.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.5),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FIRST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Fast-swimming predator, found near shore and around structure"
        ),
        Species(
            id = "king_mackerel",
            name = "King Mackerel (Kingfish)",
            scientificName = "Scomberomorus cavalla",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(21.0, 29.0, 25.0),
            preferredDepth = Species.DepthRange(5.0, 80.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Large mackerel, follows bait schools, active around temperature breaks"
        ),
        Species(
            id = "wahoo",
            name = "Wahoo",
            scientificName = "Acanthocybium solandri",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(22.0, 30.0, 26.0),
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.WAXING_GIBBOUS),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Fastest fish in the ocean, found around offshore structure and weed lines"
        ),

        // Freshwater
        Species(
            id = "largemouth_bass",
            name = "Largemouth Bass",
            scientificName = "Micropterus salmoides",
            category = Species.FishCategory.FRESHWATER,
            preferredWaterTemp = Species.TemperatureRange(15.0, 27.0, 21.0),
            preferredDepth = Species.DepthRange(0.5, 10.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 0.5),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = emptyList(),
            description = "Most active during dawn/dusk and around cover structure"
        ),
        Species(
            id = "crappie",
            name = "Black Crappie",
            scientificName = "Pomoxis nigromaculatus",
            category = Species.FishCategory.FRESHWATER,
            preferredWaterTemp = Species.TemperatureRange(12.0, 23.0, 18.0),
            preferredDepth = Species.DepthRange(1.0, 8.0),
            preferredWindSpeed = Species.WindRange(0.0, 10.0),
            preferredWaveHeight = Species.WaveRange(0.0, 0.3),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = emptyList(),
            description = "Schooling fish, prefers calm conditions and structure"
        ),
        Species(
            id = "catfish",
            name = "Channel Catfish",
            scientificName = "Ictalurus punctatus",
            category = Species.FishCategory.FRESHWATER,
            preferredWaterTemp = Species.TemperatureRange(18.0, 29.0, 24.0),
            preferredDepth = Species.DepthRange(1.0, 15.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.WANING_CRESCENT),
            preferredTidePhase = emptyList(),
            description = "Bottom feeder, most active at night and in murky water"
        )
    )

    fun getSpeciesById(id: String): Species? = getAllSpecies().find { it.id == id }

    fun getSpeciesByCategory(category: Species.FishCategory): List<Species> =
        getAllSpecies().filter { it.category == category }
}
