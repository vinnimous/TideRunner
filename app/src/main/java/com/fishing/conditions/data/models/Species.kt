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
            preferredWaterTemp = Species.TemperatureRange(15.0, 27.0, 21.0),
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
            preferredWaterTemp = Species.TemperatureRange(15.0, 24.0, 20.0),
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
            preferredWaterTemp = Species.TemperatureRange(12.0, 22.0, 17.0),
            preferredDepth = Species.DepthRange(2.0, 20.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.FALLING_TIDE, Species.TidePhase.LOW_TIDE),
            description = "Bottom dweller, active during tide changes and low light conditions."
        ),
        Species(
            id = "striped_bass",
            name = "Striped Bass",
            scientificName = "Morone saxatilis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(10.0, 24.0, 16.0),
            preferredDepth = Species.DepthRange(3.0, 15.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Migratory species, excellent for fly fishing; found in estuaries and along rocky shores from Maine to Florida."
        ),
        Species(
            id = "bluefish",
            name = "Bluefish",
            scientificName = "Pomatomus saltatrix",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(13.0, 24.0, 18.0),
            preferredDepth = Species.DepthRange(2.0, 9.0),
            preferredWindSpeed = Species.WindRange(5.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.3, 1.2),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            description = "Aggressive predator, often found in schools; prefers choppy water and moving baitfish."
        ),
        Species(
            id = "black_sea_bass",
            name = "Black Sea Bass",
            scientificName = "Centropristis striata",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(10.0, 22.0, 16.0),
            preferredDepth = Species.DepthRange(3.0, 37.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Structure-oriented, found around wrecks and rocky bottoms; popular in the Mid-Atlantic region."
        ),
        Species(
            id = "tautog",
            name = "Tautog (Blackfish)",
            scientificName = "Tautoga onitis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(4.0, 18.0, 10.0),
            preferredDepth = Species.DepthRange(1.0, 18.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.HIGH_TIDE),
            description = "Bottom feeder, prefers rocky areas and wrecks; common from Maine to Virginia."
        ),
        Species(
            id = "scup",
            name = "Scup (Porgy)",
            scientificName = "Stenotomus chrysops",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(7.0, 22.0, 15.0),
            preferredDepth = Species.DepthRange(9.0, 73.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.LOW_TIDE),
            description = "Schooling fish, found over sandy and rocky bottoms; excellent table fare."
        ),
        Species(
            id = "weakfish",
            name = "Weakfish (Gray Trout)",
            scientificName = "Cynoscion regalis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(13.0, 24.0, 18.0),
            preferredDepth = Species.DepthRange(1.0, 9.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            description = "Light tackle favorite, found in bays and estuaries; known for its fight."
        ),
        Species(
            id = "summer_flounder",
            name = "Summer Flounder",
            scientificName = "Paralichthys dentatus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(13.0, 24.0, 18.0),
            preferredDepth = Species.DepthRange(5.0, 18.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            description = "Flatfish, ambushes prey on sandy bottoms; important commercial and recreational species."
        ),

        // ---------------- OFFSHORE ----------------
        Species(
            id = "bluefin_tuna",
            name = "Atlantic Bluefin Tuna",
            scientificName = "Thunnus thynnus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(14.0, 25.0, 20.0),
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
            preferredWaterTemp = Species.TemperatureRange(20.0, 30.0, 25.0),
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
            preferredWaterTemp = Species.TemperatureRange(18.0, 31.0, 24.0),
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
            preferredWaterTemp = Species.TemperatureRange(20.0, 29.0, 25.0),
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
            preferredWaterTemp = Species.TemperatureRange(16.0, 27.0, 22.0),
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
            preferredWaterTemp = Species.TemperatureRange(21.0, 29.0, 25.0),
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
            preferredWaterTemp = Species.TemperatureRange(22.0, 30.0, 26.0),
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
            preferredWaterTemp = Species.TemperatureRange(21.0, 30.0, 26.0),
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
            preferredWaterTemp = Species.TemperatureRange(21.0, 28.0, 24.0),
            preferredDepth = Species.DepthRange(1.0, 50.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FIRST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            description = "Fast-swimming predator, found nearshore and around reefs."
        ),
        Species(
            id = "swordfish",
            name = "Swordfish",
            scientificName = "Xiphias gladius",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(18.0, 27.0, 22.0),
            preferredDepth = Species.DepthRange(3.0, 800.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Large billfish, found in deep offshore waters; known for its distinctive sword-like bill."
        ),
        Species(
            id = "sailfish",
            name = "Atlantic Sailfish",
            scientificName = "Istiophorus albicans",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(21.0, 29.0, 25.0),
            preferredDepth = Species.DepthRange(0.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.HIGH_TIDE),
            description = "Fastest billfish, known for its spectacular jumps; found in warm offshore waters."
        ),
        Species(
            id = "amberjack",
            name = "Greater Amberjack",
            scientificName = "Seriola dumerili",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(18.0, 27.0, 22.0),
            preferredDepth = Species.DepthRange(9.0, 73.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            description = "Strong fighter, found around wrecks and reefs; popular offshore species."
        )
    )

    fun getSpeciesById(id: String): Species? = getAllSpecies().find { it.id == id }

    fun getSpeciesByCategory(category: Species.FishCategory): List<Species> =
        getAllSpecies().filter { it.category == category }
}
