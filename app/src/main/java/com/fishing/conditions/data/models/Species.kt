package com.fishing.conditions.data.models

data class Species(
    val id: String,
    val name: String,
    val scientificName: String = "",
    val category: FishCategory,
    val preferredWaterTemp: TemperatureRange,
    val preferredDepth: DepthRange,
    val preferredWindSpeed: WindRange,
    val preferredWaveHeight: WaveRange,
    val preferredMoonPhase: List<MoonPhase>,
    val preferredTidePhase: List<TidePhase>,
    val habitatPreferences: HabitatPreferences,
    val description: String = "",
    val iconResId: Int? = null,
    val preferredCurrentSpeed: CurrentRange? = null
) {
    /** All temperature values stored in °F throughout the app */
    data class TemperatureRange(val min: Double, val max: Double, val optimal: Double = (min + max) / 2.0)

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

    data class HabitatPreferences(
        val preferredSlope: SlopeRange,
        val preferredCurrentSpeed: CurrentRange,
        val temperatureBreakSensitivity: Double,
        val structureProximityWeight: Double,
        val habitatType: HabitatType
    )

    data class SlopeRange(val min: Double, val max: Double, val optimal: Double = (min + max) / 2.0)
    data class CurrentRange(val min: Double, val max: Double, val optimal: Double = (min + max) / 2.0)

    enum class HabitatType {
        REEF,
        WRECK,
        SANDY_BOTTOM,
        MUDDY_BOTTOM,
        GRASS_FLATS,
        OPEN_OCEAN,
        ESTUARY,
        RIVER_MOUTH
    }

    companion object {
        fun TempRange(min: Double, max: Double, optimal: Double = (min + max) / 2.0): TemperatureRange =
            TemperatureRange(min, max, optimal)
    }
}

fun buildSpecies(
    id: String,
    name: String,
    category: Species.FishCategory,
    preferredWaterTemp: Species.TemperatureRange,
    preferredDepth: Species.DepthRange,
    preferredWindSpeed: Species.WindRange,
    preferredWaveHeight: Species.WaveRange,
    preferredMoonPhase: Species.MoonPhase,
    preferredTidePhase: Species.TidePhase,
    preferredCurrentSpeed: Species.CurrentRange,
    habitatPreferences: Species.HabitatPreferences,
    scientificName: String = "",
    description: String = ""
) = Species(
    id = id, name = name, scientificName = scientificName, category = category,
    preferredWaterTemp = preferredWaterTemp, preferredDepth = preferredDepth,
    preferredWindSpeed = preferredWindSpeed, preferredWaveHeight = preferredWaveHeight,
    preferredMoonPhase = listOf(preferredMoonPhase),
    preferredTidePhase = listOf(preferredTidePhase),
    habitatPreferences = habitatPreferences, description = description,
    preferredCurrentSpeed = preferredCurrentSpeed
)

// ─────────────────────────────────────────────────────────────────────────────
// All temperature ranges are stored in °F.
// Conversions used: °F = °C × 9/5 + 32
// ─────────────────────────────────────────────────────────────────────────────
object FishSpeciesDatabase {

    fun getAllSpecies(): List<Species> = listOf(
        // ── INSHORE ──────────────────────────────────────────────────────────
        Species(
            id = "redfish",
            name = "Redfish (Red Drum)",
            scientificName = "Sciaenops ocellatus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(59.0, 80.6, 69.8), // 15–27°C
            preferredDepth = Species.DepthRange(2.0, 10.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 30.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.9,
                habitatType = Species.HabitatType.ESTUARY
            ),
            description = "Active during moving tides, often found in shallow flats and estuaries along the Atlantic coast."
        ),
        Species(
            id = "speckled_trout",
            name = "Speckled Trout",
            scientificName = "Cynoscion nebulosus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(59.0, 75.2, 68.0), // 15–24°C
            preferredDepth = Species.DepthRange(3.0, 12.0),
            preferredWindSpeed = Species.WindRange(0.0, 12.0),
            preferredWaveHeight = Species.WaveRange(0.0, 2.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FIRST_QUARTER, Species.MoonPhase.LAST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.7,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.GRASS_FLATS
            ),
            description = "Prefers calm inshore waters and tidal movement, active near grass flats."
        ),
        Species(
            id = "flounder",
            name = "Flounder",
            scientificName = "Paralichthys lethostigma",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(53.6, 71.6, 62.6), // 12–22°C
            preferredDepth = Species.DepthRange(2.0, 20.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.FALLING_TIDE, Species.TidePhase.LOW_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 1.5, 0.5),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.SANDY_BOTTOM
            ),
            description = "Bottom dweller, active during tide changes and low light conditions."
        ),
        Species(
            id = "striped_bass",
            name = "Striped Bass",
            scientificName = "Morone saxatilis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(50.0, 75.2, 60.8), // 10–24°C
            preferredDepth = Species.DepthRange(3.0, 15.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 3.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 25.0, 15.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 4.0, 2.0),
                temperatureBreakSensitivity = 0.9,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.REEF
            ),
            description = "Migratory species; found in estuaries and along rocky shores from Maine to Florida."
        ),
        Species(
            id = "bluefish",
            name = "Bluefish",
            scientificName = "Pomatomus saltatrix",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(55.4, 75.2, 64.4), // 13–24°C
            preferredDepth = Species.DepthRange(2.0, 9.0),
            preferredWindSpeed = Species.WindRange(5.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.3, 1.2),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.9,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Aggressive predator, often found in schools; prefers choppy water and moving baitfish."
        ),
        Species(
            id = "black_sea_bass",
            name = "Black Sea Bass",
            scientificName = "Centropristis striata",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(50.0, 71.6, 60.8), // 10–22°C
            preferredDepth = Species.DepthRange(3.0, 37.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 30.0, 15.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.7,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.REEF
            ),
            description = "Structure-oriented, found around wrecks and rocky bottoms; popular in the Mid-Atlantic region."
        ),
        Species(
            id = "tautog",
            name = "Tautog (Blackfish)",
            scientificName = "Tautoga onitis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(39.2, 64.4, 50.0), // 4–18°C
            preferredDepth = Species.DepthRange(1.0, 18.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.WRECK
            ),
            description = "Bottom feeder, prefers rocky areas and wrecks; common from Maine to Virginia."
        ),
        Species(
            id = "scup",
            name = "Scup (Porgy)",
            scientificName = "Stenotomus chrysops",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(44.6, 71.6, 59.0), // 7–22°C
            preferredDepth = Species.DepthRange(9.0, 73.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.LOW_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 5.0, 2.5),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 1.0, 0.5),
                temperatureBreakSensitivity = 0.5,
                structureProximityWeight = 0.6,
                habitatType = Species.HabitatType.SANDY_BOTTOM
            ),
            description = "Schooling fish, found over sandy and rocky bottoms; excellent table fare."
        ),
        Species(
            id = "weakfish",
            name = "Weakfish (Gray Trout)",
            scientificName = "Cynoscion regalis",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(55.4, 75.2, 64.4), // 13–24°C
            preferredDepth = Species.DepthRange(1.0, 9.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.9,
                habitatType = Species.HabitatType.ESTUARY
            ),
            description = "Light tackle favorite, found in bays and estuaries; known for its fight."
        ),
        Species(
            id = "summer_flounder",
            name = "Summer Flounder",
            scientificName = "Paralichthys dentatus",
            category = Species.FishCategory.SALTWATER_INSHORE,
            preferredWaterTemp = Species.TemperatureRange(55.4, 75.2, 64.4), // 13–24°C
            preferredDepth = Species.DepthRange(5.0, 18.0),
            preferredWindSpeed = Species.WindRange(0.0, 15.0),
            preferredWaveHeight = Species.WaveRange(0.0, 1.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.SANDY_BOTTOM
            ),
            description = "Flatfish, ambushes prey on sandy bottoms; important commercial and recreational species."
        ),

        // ── OFFSHORE ─────────────────────────────────────────────────────────
        Species(
            id = "bluefin_tuna",
            name = "Atlantic Bluefin Tuna",
            scientificName = "Thunnus thynnus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(57.2, 77.0, 68.0), // 14–25°C
            preferredDepth = Species.DepthRange(65.0, 1000.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 50.0, 25.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 5.0, 2.5),
                temperatureBreakSensitivity = 0.9,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Highly migratory, prefers cooler Atlantic waters, often offshore along the US East Coast."
        ),
        Species(
            id = "blackfin_tuna",
            name = "Blackfin Tuna",
            scientificName = "Thunnus atlanticus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(68.0, 86.0, 77.0), // 20–30°C
            preferredDepth = Species.DepthRange(10.0, 300.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.WAXING_GIBBOUS, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 40.0, 20.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 4.0, 2.0),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Smaller tuna species, common in warm Atlantic waters near Florida and the Caribbean."
        ),
        Species(
            id = "yellowfin_tuna",
            name = "Yellowfin Tuna",
            scientificName = "Thunnus albacares",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(64.4, 87.8, 75.2), // 18–31°C
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 50.0, 25.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 5.0, 2.5),
                temperatureBreakSensitivity = 0.9,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Pelagic species, prefers warm water and offshore currents, commonly caught near the Gulf Stream."
        ),
        Species(
            id = "mahi_mahi",
            name = "Mahi Mahi (Dolphin Fish)",
            scientificName = "Coryphaena hippurus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(68.0, 84.2, 77.0), // 20–29°C
            preferredDepth = Species.DepthRange(5.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.WAXING_GIBBOUS, Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.9,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Found near floating debris and current lines; popular for sport fishing offshore Florida."
        ),
        Species(
            id = "red_grouper",
            name = "Red Grouper",
            scientificName = "Epinephelus morio",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(60.8, 80.6, 71.6), // 16–27°C
            preferredDepth = Species.DepthRange(15.0, 150.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 30.0, 15.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.7,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.REEF
            ),
            description = "Structure-oriented bottom feeder, common along reefs and wrecks off the Atlantic coast."
        ),
        Species(
            id = "king_mackerel",
            name = "King Mackerel",
            scientificName = "Scomberomorus cavalla",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(69.8, 84.2, 77.0), // 21–29°C
            preferredDepth = Species.DepthRange(5.0, 80.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.8,
                structureProximityWeight = 0.9,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Large predator, follows bait schools; common offshore and near coastal inlets."
        ),
        Species(
            id = "wahoo",
            name = "Wahoo",
            scientificName = "Acanthocybium solandri",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(71.6, 86.0, 78.8), // 22–30°C
            preferredDepth = Species.DepthRange(20.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 30.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.WAXING_GIBBOUS),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 20.0, 10.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.9,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Fastest fish in the ocean, found around offshore structure and weed lines."
        ),
        Species(
            id = "cobia",
            name = "Cobia",
            scientificName = "Rachycentron canadum",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(69.8, 86.0, 78.8), // 21–30°C
            preferredDepth = Species.DepthRange(5.0, 100.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.WRECK
            ),
            description = "Strong swimmer, often found near buoys, wrecks, and other structures offshore Florida."
        ),
        Species(
            id = "spanish_mackerel",
            name = "Spanish Mackerel",
            scientificName = "Scomberomorus maculatus",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(69.8, 82.4, 75.2), // 21–28°C
            preferredDepth = Species.DepthRange(1.0, 50.0),
            preferredWindSpeed = Species.WindRange(0.0, 20.0),
            preferredWaveHeight = Species.WaveRange(0.0, 4.0),
            preferredMoonPhase = listOf(Species.MoonPhase.NEW_MOON, Species.MoonPhase.FIRST_QUARTER),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Fast-swimming predator, found nearshore and around reefs."
        ),
        Species(
            id = "swordfish",
            name = "Swordfish",
            scientificName = "Xiphias gladius",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(64.4, 80.6, 71.6), // 18–27°C
            preferredDepth = Species.DepthRange(3.0, 800.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 6.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Large billfish, found in deep offshore waters; known for its distinctive sword-like bill."
        ),
        Species(
            id = "sailfish",
            name = "Atlantic Sailfish",
            scientificName = "Istiophorus albicans",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(69.8, 84.2, 77.0), // 21–29°C
            preferredDepth = Species.DepthRange(0.0, 200.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON),
            preferredTidePhase = listOf(Species.TidePhase.HIGH_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 10.0, 5.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 2.0, 1.0),
                temperatureBreakSensitivity = 0.6,
                structureProximityWeight = 0.7,
                habitatType = Species.HabitatType.OPEN_OCEAN
            ),
            description = "Fastest billfish, known for its spectacular jumps; found in warm offshore waters."
        ),
        Species(
            id = "amberjack",
            name = "Greater Amberjack",
            scientificName = "Seriola dumerili",
            category = Species.FishCategory.SALTWATER_OFFSHORE,
            preferredWaterTemp = Species.TemperatureRange(64.4, 80.6, 71.6), // 18–27°C
            preferredDepth = Species.DepthRange(9.0, 73.0),
            preferredWindSpeed = Species.WindRange(0.0, 25.0),
            preferredWaveHeight = Species.WaveRange(0.0, 5.0),
            preferredMoonPhase = listOf(Species.MoonPhase.FULL_MOON, Species.MoonPhase.NEW_MOON),
            preferredTidePhase = listOf(Species.TidePhase.RISING_TIDE, Species.TidePhase.FALLING_TIDE),
            habitatPreferences = Species.HabitatPreferences(
                preferredSlope = Species.SlopeRange(0.0, 30.0, 15.0),
                preferredCurrentSpeed = Species.CurrentRange(0.0, 3.0, 1.5),
                temperatureBreakSensitivity = 0.7,
                structureProximityWeight = 0.8,
                habitatType = Species.HabitatType.REEF
            ),
            description = "Strong fighter, found around wrecks and reefs; popular offshore species."
        )
    )

    fun getSpeciesById(id: String): Species? = getAllSpecies().find { it.id == id }

    fun getSpeciesByCategory(category: Species.FishCategory): List<Species> =
        getAllSpecies().filter { it.category == category }
}
