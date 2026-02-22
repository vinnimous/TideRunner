# TideRunner 🎣

**Your AI-Powered Fishing Forecast Companion**

*Never wonder "Is today a good day to fish?" again.*

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com)

---

## 📱 About TideRunner

**TideRunner** is the ultimate fishing conditions analyzer that tells you exactly when and where to catch your target species. Using advanced algorithms and real-time marine data from multiple sources, TideRunner calculates a personalized **Habitat Suitability Index (HSI)** for your chosen fish, taking into account water temperature, tides, moon phases, weather conditions, bathymetry, structure analysis, current interactions, and optimal feeding times.

### 🎯 What Makes TideRunner Special?

**Smart Species Targeting** - Select from 16+ popular game fish (Redfish, Bass, Tuna, Mahi Mahi, and more), and TideRunner instantly analyzes current conditions against each species' unique preferences using our advanced Habitat Suitability Model.

**Real-Time Marine Intelligence** - We aggregate data from 6+ professional marine APIs to give you the most comprehensive conditions report: water temperature, wave height, tide predictions, wind speed, moon phase, solunar feeding periods, and now bathymetric data for structure analysis.

**Habitat Suitability Index (HSI)** - Get an instant 0-100 rating with color-coded results based on advanced habitat modeling:
- 🟢 **EXCELLENT (70+)**: Prime conditions - grab your gear!
- 🟡 **GOOD (50-70)**: Solid fishing opportunity
- 🟠 **FAIR (30-50)**: Might get some bites
- 🔴 **POOR (<30)**: Better to wait or try another spot

**Advanced Habitat Modeling** - Our proprietary HSI incorporates:
- Bathymetry analysis for depth and structure detection
- Slope gradient calculations for reef and wreck identification
- Current interaction modeling for up-current staging zones
- Temperature gradient analysis for thermoclines and breaks
- Species-specific weighting for accurate predictions

**100% Free to Use** - No subscriptions, no hidden costs, no ads. TideRunner uses free marine data APIs and OpenStreetMap, so you get professional-grade fishing intelligence without spending a dime or compromising your privacy.

### 🌊 Perfect For:

- **Recreational Anglers** - Plan your weekend fishing trips with confidence
- **Tournament Fishers** - Gain a competitive edge with data-driven insights
- **Charter Captains** - Provide clients with the best possible conditions
- **Surf Fishers** - Know exactly when the tide and conditions align
- **Offshore Enthusiasts** - Check offshore conditions before heading out
- **Bass Anglers** - Find optimal feeding times for freshwater species

### 🎁 Key Features at a Glance

✅ **16+ Pre-configured Species** - Saltwater (inshore & offshore) and freshwater fish  
✅ **Multi-Source Data** - Combines NOAA tides, solunar tables, marine forecasts  
✅ **Interactive Map** - Tap anywhere to check local fishing conditions  
✅ **Tide Predictions** - Know the exact times for high and low tides  
✅ **Solunar Calendar** - Major and minor feeding period predictions  
✅ **Moon Phase Tracking** - Fish are more active during certain moon phases  
✅ **Offline Capable** - Cached map tiles work without internet  
✅ **No Account Required** - Start fishing smarter immediately  

---

## 🏆 Features

### 🎣 Species-Specific Analysis
- **20 Pre-configured Fish Species** including:
  - **Saltwater Inshore**: Redfish, Speckled Trout, Flounder, Striped Bass, Bluefish, Black Sea Bass, Tautog, Scup, Weakfish, Summer Flounder
  - **Saltwater Offshore**: Atlantic Bluefin Tuna, Blackfin Tuna, Yellowfin Tuna, Mahi Mahi, Red Grouper, King Mackerel, Wahoo, Cobia, Spanish Mackerel, Swordfish, Atlantic Sailfish, Greater Amberjack
- All species temperature preferences are stored and displayed in **°F** (US customary)
- Each species has specific optimal conditions for:
  - Water temperature ranges (**°F**)
  - Depth preferences
  - Wind and wave tolerances
  - Moon phase preferences
  - Tide phase preferences

### 🌊 Comprehensive Marine Data
Real-time conditions from multiple free APIs:
- **Water Conditions**: Temperature, wave height, current speed & direction
- **Weather**: Wind speed/direction, air temperature, pressure, humidity
- **Tide Data**: High/low tide predictions, current tide phase
- **Astronomical**: Sunrise/sunset, moonrise/moonset, moon phase & illumination
- **Solunar Periods**: Major and minor feeding times for optimal fishing

### 📊 Habitat Suitability Index (HSI) — Blended Score
- Advanced scoring (0-100) using a **60% FSI / 40% HSI blend** for maximum accuracy:

#### Fishing Suitability Index (FSI) — Environmental Score (60% weight)
  - **Water Temperature** (0-25 pts): Gaussian curve centered on species' optimal °F temp
  - **Wind Conditions** (0-15 pts): Gaussian-weighted against species' preferred wind range
  - **Wave Conditions** (0-10 pts): Gaussian-weighted against species' preferred wave range
  - **Moon Phase** (0-15 pts): Graduated scoring — preferred phases score highest, never zero
  - **Tide Phase** (0-15 pts): Graduated scoring — active tide phases always score higher than slack
  - **Solunar Activity** (0-20 pts): Gaussian peak at score 5 (peak activity periods)

#### Habitat Suitability Index (HSI) — Structural Score (40% weight)
  - **Structure Analysis**: Slope gradients, proximity to reefs/wrecks
  - **Depth Matching**: Optimal depth range for species
  - **Temperature Gradient**: Thermoclines and water breaks (uses real °F data)
  - **Current Interaction**: Up-current staging zones (uses real current speed)
  - **Environmental Factors**: Wind, waves, temperature (Gaussian-weighted)
  - **Solunar Activity**: Feeding period alignment (uses real solunar score)

- Species-specific weighting for accurate, realistic predictions
- Graceful degradation: if HSI data unavailable, FSI drives the score with no penalty
- Color-coded ratings: **Excellent** (70+), **Good** (50-70), **Fair** (30-50), **Poor** (<30)

### 🗺️ Interactive Map
- **OpenStreetMap** integration - 100% FREE, no API key required!
- **OpenSeaMap Nautical Charts** - Toggle on/off detailed sea charts with depth soundings, wrecks, reefs, and marine structures
- Tap anywhere on the map to get fishing conditions
- Coastal and marine feature visualization
- Offline map tile caching

### 📅 Forecast Features
- **10-Day Forecast**: Plan your fishing trips up to 10 days in advance
- **Confidence Ratings**: Each forecast day shows prediction confidence (95% for today down to ~50% for day 10)
- **Date Selection**: Horizontal scrollable date picker to view conditions for any day
- **Fishing Times Graph**: 24-hour graph showing best fishing times with:
  - Green curve for fishing quality score
  - Solunar period highlights (major and minor feeding times)
  - Tide markers (high and low tide times)
  - Current time indicator

### 🌐 Data Sources

#### Free APIs (No Key Required)
1. **Open-Meteo Marine** - Marine weather & ocean conditions
   - Wave height, wind, sea surface temperature
   - Ocean current velocity & direction
   - NO API KEY NEEDED!

2. **Solunar API** - Fishing activity periods
   - Major/minor feeding periods
   - Moon phase calculations
   - NO API KEY NEEDED!

#### Free Tier Available APIs
3. **Stormglass** - Comprehensive marine data (10 calls/day free)
   - Waves, currents, wind, water temp
   - Tide extremes predictions
   - Astronomical data

4. **NOAA CO-OPS** - US coastal tide & current data (FREE)
   - Real-time tide observations
   - Tide predictions
   - Current speed & direction

5. **IPGeolocation Astronomy** - Sun & moon data (free tier available)
   - Sunrise/sunset times
   - Moon phase & illumination
   - Moonrise/moonset

6. **OpenWeatherMap** - Weather data (free tier available)
   - General weather conditions
   - Wind & atmospheric data

---

## 🚀 How It Works

**Step 1: Choose Your Target**  
Select the fish species you're after - whether it's Redfish in the shallows, Largemouth Bass in the lake, or Yellowfin Tuna offshore. Each species has unique preferences that TideRunner knows inside and out.

**Step 2: Pick Your Spot**  
Tap anywhere on the interactive map - your favorite fishing hole, a new location you want to try, or follow the coastline looking for optimal conditions.

**Step 3: Get Your Score**  
TideRunner instantly analyzes 15+ environmental factors and calculates a Habitat Suitability Index (HSI) specific to your chosen species. See exactly what's working in your favor (and what's not).

**Step 4: Time It Right**  
Check tide times, solunar feeding periods, and moon phase data. TideRunner tells you not just *where* to fish, but *when* the bite will be hottest.

**Step 5: Catch More Fish!**  
Armed with professional-grade intelligence, you'll spend less time guessing and more time reeling them in. 🎣

---

## 💡 Why TideRunner?

**For the Weekend Warrior:** Stop wasting precious time on slow days. TideRunner helps you plan trips when conditions are prime, maximizing your limited fishing time.

**For the Serious Angler:** Gain the competitive edge with data-driven insights. Know what the pros know - temperature breaks, tide timing, and feeding windows.

**For Charter Operations:** Impress clients with expert knowledge and put them on fish. When you can explain why "right now" is the perfect time, you build trust and get better tips.

**For Conservation-Minded Fishers:** Fish smarter, not harder. When you target the right species at the right time, you reduce stress on fish populations and practice sustainable angling.

---

## 📊 What TideRunner Analyzes

Every Habitat Suitability Index considers:

**🏔️ Bathymetry & Structure** - Depth analysis and slope gradients for reef/wreck detection  
**🌊 Current Interactions** - Up-current staging zones and flow patterns  
**🌡️ Temperature Gradients** - Thermoclines and water breaks  
**🌊 Wave & Current Conditions** - Too rough, too calm, or just right?  
**💨 Wind Speed & Direction** - Affecting bait movement and boat control  
**🌙 Moon Phase** - Full moon? New moon? It matters!  
**🌊 Tide Timing** - Rising, falling, slack, or peak - when do they feed?  
**⏰ Solunar Periods** - The secret weapon of tournament anglers  
**🌅 Sun Position** - Dawn and dusk bite times calculated  
**📍 Location-Specific Data** - Every spot is different

---

## 🏅 Species Database

### Saltwater - Inshore
**🐟 Redfish (Red Drum)** - *Sciaenops ocellatus*  
Optimal: 15-27°C | Prefers rising tides and full/new moon phases  
*"Active during moving tides, especially in shallow flats and marshes"*

**🐟 Speckled Trout** - *Cynoscion nebulosus*  
Optimal: 15-24°C | Loves calm conditions with light wind  
*"Prefers calm conditions with light wind, active during tidal movement"*

**🐟 Flounder** - *Paralichthys lethostigma*  
Optimal: 12-22°C | Bottom dweller, best on falling tides  
*"Active during tide changes and low light conditions"*

### Saltwater - Offshore
**🐟 Mahi Mahi (Dolphin)** - *Coryphaena hippurus*  
Optimal: 20-29°C | Found near floating debris and current lines  
*"Pelagic species, loves warm water and structure"*

**🐟 Atlantic Bluefin Tuna** - *Thunnus thynnus*  
Optimal: 14-25°C | Highly migratory, follows bait schools  
*"Apex predator, active in cooler offshore waters"*

**🐟 Yellowfin Tuna** - *Thunnus albacares*  
Optimal: 18-31°C | Highly migratory, prefers strong currents  
*"Chases bait in warm water temperature breaks"*

**🐟 Blackfin Tuna** - *Thunnus atlanticus*  
Optimal: 20-28°C | Smaller cousin of Yellowfin, schooling species  
*"Active in warm Gulf Stream waters"*

**🐟 Red Grouper** - *Epinephelus morio*  
Optimal: 16-27°C | Structure-oriented, tide dependent  
*"Active during tidal movement around reefs and wrecks"*

**🐟 Spanish Mackerel** - *Scomberomorus maculatus*  
Optimal: 18-27°C | Fast-moving, surface feeder  
*"Follows bait schools, active during tide changes"*

**🐟 King Mackerel** - *Scomberomorus cavalla*  
Optimal: 21-29°C | Larger, offshore species  
*"Aggressive predator, prefers temperature breaks"*

**🐟 Wahoo** - *Acanthocybium solandri*  
Optimal: 22-28°C | Speed demon of the ocean  
*"Fastest fish in the sea, found near drop-offs and wrecks"*

### Freshwater
**🐟 Largemouth Bass** - *Micropterus salmoides*  
Optimal: 15-27°C | Most active dawn/dusk  
*"Cover-oriented predator, moon phase sensitive"*

**🐟 Black Crappie** - *Pomoxis nigromaculatus*  
Optimal: 12-23°C | Schooling fish, structure dependent  
*"Prefers calm conditions and suspended structure"*

**🐟 Channel Catfish** - *Ictalurus punctatus*  
Optimal: 18-29°C | Bottom feeder, night active  
*"Most active at night and in murky water"*

---

## 🆓 100% Free Operation

Unlike other fishing apps that charge monthly subscriptions or limit features behind paywalls, **TideRunner is completely free**. We believe fishing intelligence should be accessible to everyone.

**Free Data Sources:**
- ✅ Open-Meteo Marine API (unlimited)
- ✅ Solunar.org Fishing Times (unlimited)
- ✅ OpenStreetMap (unlimited)
- ✅ NOAA Tides & Currents (unlimited, US coastal)

**Optional Enhanced Sources:**
- Stormglass (10 free calls/day)
- IPGeolocation (free tier)
- OpenWeatherMap (free tier)

**No Strings Attached:**
- ❌ No registration required
- ❌ No credit card needed
- ❌ No data selling
- ❌ No annoying ads
- ✅ Just great fishing intelligence

---

## 🔒 Data Privacy & Security

**Your Privacy Matters** - TideRunner is designed with privacy-first principles. We collect zero personal data and respect your fishing intelligence.

### 📱 What We Collect
- **Nothing.** TideRunner does not collect, store, or transmit any personal information, location data, or usage analytics.
- **No Account Required** - Use all features without creating an account or providing any personal details.
- **No Tracking** - We don't use cookies, analytics services, or third-party trackers.

### 🌐 Data Usage
- **Local Processing** - All fishing condition calculations happen on your device.
- **API Calls Only** - We only request marine data from public APIs when you tap a location.
- **No Data Retention** - API responses are cached locally for performance but never stored on our servers.

### 🛡️ Security
- **Open Source** - Our code is publicly auditable on GitHub.
- **No Ads** - Zero advertising means zero ad tracking or data sharing.
- **Free APIs Only** - We use only free, public APIs that don't require personal API keys or accounts.

### 📋 Permissions
TideRunner only requests the minimum permissions needed for core functionality:
- **Location** (optional): For finding your current position on the map
- **Internet** (required): For fetching marine data from public APIs
- **Storage** (optional): For caching map tiles offline

**You control your data** - All permissions are optional except internet access for marine data.

---

## 🛠️ Technical Stack

- **Language**: Kotlin 1.9.22
- **UI**: Jetpack Compose with Material Design
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt 2.50
- **Networking**: Retrofit + OkHttp
- **Database**: Room 2.6.1 (local caching)
- **Maps**: OSMDroid (OpenStreetMap) - **100% Free, No API Key Required!**
- **Async**: Kotlin Coroutines + Flow
- **Testing**: JUnit 5, MockK, Truth, Turbine, Compose Test

---

## 📊 Performance

- **App Size**: ~9 MB (release build)
- **Minimum Android**: Android 6.0 (API 23)
- **Target Android**: Android 14 (API 34)
- **Battery Usage**: Minimal - no background services
- **Data Usage**: ~100 KB per location check (with caching)
- **Offline Support**: Map tiles cached for offline viewing

---

## 🤝 Contributing

We welcome contributions from the fishing and developer communities!

**Ways to Contribute:**
- 🐛 Report bugs and issues
- 💡 Suggest new features or species
- 🔧 Submit pull requests
- 📝 Improve documentation
- 🌍 Add regional fishing species
- 🧪 Test on different devices

---

## 📜 License

This project is licensed under the MIT License.

---

## 🙏 Acknowledgments

**Data Providers:**
- Open-Meteo for marine forecast data
- NOAA for tide and current data
- Solunar.org for fishing time calculations
- OpenStreetMap contributors for mapping data

**Technology:**
- Google for Android development tools
- JetBrains for Kotlin
- The open-source community

---

## ⚖️ Disclaimer

TideRunner provides fishing condition analysis based on available data and algorithms. Actual fishing success depends on many factors including skill, equipment, location knowledge, regulations, and environmental conditions.

**Always:**
- Check local fishing regulations and licensing requirements
- Monitor weather warnings and marine forecasts
- Practice safe boating and fishing practices
- Respect catch limits and protected species
- Leave no trace and protect our waters

**Remember:** TideRunner is a tool to help you fish smarter, but the real magic happens when you're on the water! 🎣

---

## 📋 Changelog

### Version 1.2.0 (Latest)
- **🌡️ Imperial Temperatures Everywhere (Option A)**: All species temperature preferences now stored, compared, and displayed in **°F** — no more hidden Celsius-to-Fahrenheit conversions causing scoring mismatches. Water temp and air temp in the UI consistently show °F.
- **🌙 Graduated Moon & Tide Scoring (Option B)**: Moon phase and tide phase scoring is now graduated (not binary). Preferred phases still score highest (15 pts) but non-preferred phases earn partial credit, reflecting the fact that fish still bite at non-peak phases.
- **🔀 Blended FSI + HSI Score (Option C)**: Final suitability score is now a **60% FSI / 40% HSI blend**, combining rich environmental data with habitat/structure data for more accurate and realistic scores. If HSI data is unavailable, FSI alone drives the score with no penalty.
- **🔧 Fixed Hardcoded HSI Values**: `calculateCurrentInteractionScore`, `calculateTemperatureGradientScore`, and `calculateSolunarScore` in `HabitatSuitabilityEngine` now use real API data (actual current speed, actual water °F temp, actual solunar score) instead of hardcoded constants.
- **🌊 Fixed Wave Height Double-Conversion**: Wave height was being double-converted to feet in the UI. Now `waveHeight` is correctly shown as feet since it is already stored in feet.
- **🗑️ Removed Dead Code**: Deleted unused `FsiCalculator.kt` and `MarineDataRepositoryExtensions.kt` which were never called.
- **🧪 Updated Tests**: All test assertions updated to reflect °F-native species temperature ranges and the new blended scoring model.

### Version 1.1.0
- **🗺️ Nautical Charts as Default**: OpenSeaMap nautical charts are now the default map view instead of standard OpenStreetMap
- **🎯 Marine-Focused Design**: App optimized for marine use with nautical charts always visible
- **🔧 Code Cleanup**: Removed toggle functionality, cleaned up imports, fixed test data
- **🧪 Test Fixes**: Corrected visibility values in test cases, ensured all unit tests pass
- **📊 Improved Scoring**: Enhanced fishing suitability calculations with better accuracy

### Version 1.0.0
- **🎣 Initial Release**: Complete fishing conditions analyzer
- **🌊 Habitat Suitability Index (HSI)**: Advanced scoring system with structure analysis
- **🗺️ Interactive Map**: OpenStreetMap integration with OpenSeaMap nautical charts
- **📱 20 Fish Species**: Comprehensive species database with optimal conditions (all °F)
- **🔒 Privacy-First**: Zero data collection, no ads, no tracking
- **💯 100% Free**: No subscriptions, no API keys required

---

<div align="center">

**Made with ❤️ for anglers, by anglers**

*Tight lines and good tides!* 🌊🎣

**Current Version:** 1.1.0 | **License:** MIT

</div>
