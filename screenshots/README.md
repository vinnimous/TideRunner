# TideRunner Screenshot Framework

Captures Google Play Store screenshots using Appium 2 + Python.
Runs locally against a Pixel 4 emulator. No changes to app code required.

## Prerequisites

| Tool | Version | Install |
|------|---------|---------|
| Node.js | 18+ | https://nodejs.org |
| Appium | 2.x | `npm install -g appium@latest` |
| uiautomator2 driver | latest | `appium driver install uiautomator2` |
| Python | 3.11+ | https://python.org |
| Android Studio | latest | AVD Manager must have **Pixel_4_API_34** |

## One-Time Setup

```bash
# 1. Install Python dependencies
cd screenshots/
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt

# 2. Build and install the debug APK onto your running emulator
cd ..
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Running Screenshots

> **Root cause of `ANDROID_HOME` errors:** Appium's uiautomator2 driver needs
> `ANDROID_HOME` set in the **same terminal that runs `appium`**, not just the
> Python terminal. Use the provided scripts — they set it automatically.

```bash
# Terminal 1 — start Appium WITH the Android SDK env var set
bash screenshots/start_appium.sh

# Terminal 2 — start your Pixel 4 emulator from Android Studio AVD Manager
# then run the capture suite:
bash screenshots/run_screenshots.sh
```

That's it. Both scripts export `ANDROID_HOME=$HOME/Android/Sdk` automatically.

## Permanent Fix (do this once)

So you never have to worry about it again, add these lines to your `~/.bashrc`:

```bash
export ANDROID_HOME="$HOME/Android/Sdk"
export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"
```

Then reload: `source ~/.bashrc`

After that, a bare `appium` command will work from any terminal.

Screenshots are saved to `screenshots/output/phone/`.

## AVD Name

The framework uses the AVD named **`Pixel_4`** (confirmed from your emulator setup).
If you ever need to change it, update `config/devices.py` and run:

```python
PIXEL_4 = {
    "avd_name": "YOUR_AVD_NAME_HERE",  # ← change this
    ...
}
```

Find your AVD name with:
```bash
~/Android/Sdk/emulator/emulator -list-avds
```

## Screenshots Captured

| File | What It Shows |
|------|--------------|
| `01_map_overview_tap_any_spot_to_get_your_forecast.png` | Full OSM map on launch |
| `02_choose_your_target_species_from_16_fish.png` | Species dropdown open |
| `03_neuse_river_nc_inshore_redfish_suitability_score.png` | Redfish score — Neuse River (nearshore) |
| `04_neuse_river_nc_best_fishing_times_solunar_graph.png` | 24-hr solunar activity graph |
| `05_neuse_river_nc_real_time_marine_conditions.png` | Water temp, waves, wind, pressure |
| `06_neuse_river_nc_tide_high_low_times.png` | Tide high/low section |
| `07_moon_phase_sunrise_sunset_and_solunar_feeding_periods.png` | Sun & Moon + Solunar |
| `08_ten_day_forecast_strip_with_confidence_percentage.png` | 10-day date selector |
| `09_frying_pan_tower_offshore_mahi_mahi_forecast.png` | Mahi-Mahi score — Frying Pan Tower (offshore) |
| `10_frying_pan_tower_offshore_wave_height_wind_conditions.png` | Offshore marine conditions |

## Quickest Way to Run

A convenience script handles everything and uses the correct Python automatically:

```bash
# From the project root:
bash screenshots/run_screenshots.sh
```

## Troubleshooting

**`ImportError` / `ModuleNotFoundError`** — You are running the system `pytest`
instead of the venv one. Always use one of:
- `bash screenshots/run_screenshots.sh`  ← recommended
- `cd screenshots && python -m pytest ...` after `source .venv/bin/activate`

Never use a bare `pytest` command unless you have already activated the venv.

**`adb geo fix` warning** — harmless if the emulator console port isn't exposed.
The map tap still fires `updateLocation()` correctly.

**Conditions don't load** — the app hits live APIs (Open-Meteo, NOAA).
Ensure the emulator has internet access. Tests that fail to load are skipped,
not failed, so the rest of the run continues.

**Wrong AVD name** — update `config/devices.py` with your exact AVD name.
Find yours with: `~/Android/Sdk/emulator/emulator -list-avds`

**Element not found** — if a species name differs from what's in the test,
update the `select_species("...")` call in the test to match exactly what
appears in the app's species list.

