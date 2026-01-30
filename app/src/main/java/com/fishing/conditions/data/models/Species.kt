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

object FishSpeciesDatabase {

    fun getAllSpecies(): List<Species> = listOf(
        // ---------------- INSHORE ----------------
        Species(
            id = "redfish",
            name = "Redfish (Red Drum)",
            scientificName = "Sciaenops ocellatus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(59.0, 81.0, 70.0),
            preferredDepth = Species.DepthRange(2.0, 10.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Active during moving tides, often found in shallow flats and estuaries along the Atlantic coast."
        ),
        Species(
            id = "speckled_trout",
            name = "Speckled Trout",
            scientificName = "Cynoscion nebulosus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(59.0, 75.0, 68.0),
            preferredDepth = Species.DepthRange(3.0, 12.0),
            preferredWindSpeed = Species.WindRange(0.0, 12.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FIRST_QUARTER, Species.MoonPhase.LAST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Prefers calm inshore waters and tidal movement, active near grass flats."
        ),
        Species(
            id = "flounder",
            name = "Flounder",
            scientificName = "Paralichthys lethostigma",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(54.0, 72.0, 63.0),
            preferredDepth = Species.DepthRange(2.0, 20.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.FALLING_TIDE, Species.TidePhase.LOW_TIDE),
            description = "Bottom dweller, active during tide changes and low light conditions."
        ),

        // ---------------- OFFSHORE ----------------
        Species(
            id = "bluefin_tuna",
            name = "Atlantic Bluefin Tuna",
            scientificName = "Thunnus thynnus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(57.0, 77.0, 68.0),
            preferredDepth = Species.DepthRange(65.0, 1000.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Highly migratory, prefers cooler Atlantic waters, often offshore along the US East Coast."
        ),
        Species(
            id = "blackfin_tuna",
            name = "Blackfin Tuna",
            scientificName = "Thunnus atlanticus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(68.0, 86.0, 77.0),
            preferredDepth = Species.DepthRange(10.0, 300.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.WAXING_GIBBOUS, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Smaller tuna species, common in warm Atlantic waters near Florida and the Caribbean."
        ),
        Species(
            id = "yellowfin_tuna",
            name = "Yellowfin Tuna",
            scientificName = "Thunnus albacares",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(64.0, 88.0, 75.0),
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Pelagic species, prefers warm water and offshore currents, commonly caught near the Gulf Stream."
        ),
        Species(
            id = "mahi_mahi",
            name = "Mahi Mahi (Dolphin Fish)",
            scientificName = "Coryphaena hippurus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(68.0, 84.0, 77.0),
            preferredDepth = Species.DepthRange(5.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.WAXING_GIBBOUS, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Found near floating debris and current lines; popular for sport fishing offshore Florida."
        ),
        Species(
            id = "red_grouper",
            name = "Red Grouper",
            scientificName = "Epinephelus morio",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(61.0, 81.0, 72.0),
            preferredDepth = Species.DepthRange(15.0, 150.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Structure-oriented bottom feeder, common along reefs and wrecks off the Atlantic coast."
        ),
        Species(
            id = "king_mackerel",
            name = "King Mackerel",
            scientificName = "Scomberomorus cavalla",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(70.0, 84.0, 77.0),
            preferredDepth = Species.DepthRange(5.0, 80.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Large predator, follows bait schools; common offshore and near coastal inlets."
        ),
        Species(
            id = "wahoo",
            name = "Wahoo",
            scientificName = "Acanthocybium solandri",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(72.0, 86.0, 79.0),
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.WAXING_GIBBOUS),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Fastest fish in the ocean, found around offshore structure and weed lines."
        ),
        Species(
            id = "cobia",
            name = "Cobia",
            scientificName = "Rachycentron canadum",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(70.0, 86.0, 78.0),
            preferredDepth = Species.DepthRange(5.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Strong swimmer, often found near buoys, wrecks, and other structures offshore Florida."
        ),
        Species(
            id = "spanish_mackerel",
            name = "Spanish Mackerel",
            scientificName = "Scomberomorus maculatus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(70.0, 82.0, 75.0),
            preferredDepth = Species.DepthRange(1.0, 50.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FIRST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Fast-swimming predator, found nearshore and around reefs."
        )
    )

    fun getSpeciesById(id: String): Species? = getAllSpecies().find { it.id == id }

    fun getSpeciesByCategory(category: Species.FishCategory): List<Species> =
        getAllSpecies().filter { it.category == category }
}
