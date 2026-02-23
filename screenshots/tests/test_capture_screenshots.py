"""
TideRunner Play Store Screenshot Capture Suite
===============================================

Captures all screenshots needed for a Google Play Store listing.
Each test captures one meaningful screen state with a descriptive filename.

Primary locations used:
  - Lockwood Folly Inlet, NC  (nearshore — Red Drum, Flounder)
  - Frying Pan Tower, NC      (offshore — Mahi-Mahi, Wahoo, Tuna)

Run order matters — tests are numbered so pytest executes them in sequence,
keeping the app in the correct state between shots.

Usage:
  # From the screenshots/ directory:
  bash run_screenshots.sh

Screenshots are saved to:
  screenshots/output/phone/

Naming convention: {number}_{what_is_shown}.png
"""

import sys
import os
import time

import pytest

# Make helpers and screens importable
sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

from config.locations import (
    NEUSE_RIVER_POINT,
    LOCKWOOD_FOLLY_INLET,
    FRYING_PAN_TOWER,
    GULF_COAST,
    FLORIDA_KEYS,
)
from helpers.screenshot import take_screenshot
from helpers.app_guard import verify_screen_elements
from helpers.driver_utils import (
    wait_for_text,
    find_by_text,
    find_by_text_contains,
    scroll_down,
    scroll_up,
    grant_location_permissions,
)
from screens.map_screen import MapScreen
from screens.conditions_panel import ConditionsPanel


# ---------------------------------------------------------------------------
# Shot 1 — App launch: clean map overview before any location is tapped
# ---------------------------------------------------------------------------

def test_01_map_overview_on_launch(driver, device_profile):
    """
    Capture the initial map view immediately after launch.

    Shows: Full OSM + OpenSeaMap tiles, the "Filter by Species" card at
    the top, and the "My Location" FAB. No conditions panel visible —
    this is the clean first impression before any location is selected.
    """
    map_screen = MapScreen(driver)
    map_screen.handle_first_launch()
    map_screen.wait_for_map_tiles(seconds=5)

    missing = verify_screen_elements(driver, ["Filter by Species"])
    if missing:
        pytest.fail(
            f"Shot 1: Map screen not ready. Missing: {missing}\n"
            "The app may not have launched or the map failed to render."
        )

    take_screenshot(
        driver,
        filename="01_map_overview_tap_any_spot_to_get_your_forecast",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 2 — Species selector open: showing all 16+ fish to choose from
# ---------------------------------------------------------------------------

def test_02_species_selector_full_list_open(driver, device_profile):
    """
    Capture the species selection dropdown fully open.

    Shows: The complete scrollable list of 16+ pre-configured fish species.
    Communicates the breadth of the app's species targeting capability.
    """
    map_screen = MapScreen(driver)
    map_screen.open_species_filter()

    # Wait up to 5 s for the dropdown to fully animate open and render items.
    # "Redfish (Red Drum)" is always the first item — use it as the canary.
    # wait_for_text polls every 500 ms so we don't race the popup animation.
    appeared = wait_for_text(driver, "Redfish (Red Drum)", timeout=5)
    if not appeared:
        pytest.fail(
            "Shot 2: Species dropdown did not open within 5 s.\n"
            "The 'Filter by Species' card may not have responded to the tap."
        )

    take_screenshot(
        driver,
        filename="02_choose_your_target_species_from_16_fish",
        device_profile=device_profile,
    )

    driver.back()
    time.sleep(0.5)


# ---------------------------------------------------------------------------
# Shot 3 — Lockwood Folly Inlet: nearshore suitability score (Red Drum)
# ---------------------------------------------------------------------------

def test_03_lockwood_folly_inlet_nearshore_suitability_score(driver, device_profile):
    """
    Capture the fishing suitability score at the Neuse River near Oriental, NC.

    Shows: The suitability score badge for Redfish (Red Drum) on classic NC
    inshore water where the Neuse meets Pamlico Sound. Shallow grass flats
    and strong tidal current make this a perfect demonstration of how tides
    and solunar timing drive the score.
    """
    map_screen = MapScreen(driver)
    panel = ConditionsPanel(driver)

    # Select Redfish — matches the exact species name in the app
    map_screen.open_species_filter()
    time.sleep(0.8)
    found = map_screen.select_species("Redfish (Red Drum)")
    if not found:
        elem = find_by_text_contains(driver, "Red Drum")
        if elem:
            elem.click()
            time.sleep(1.0)
        else:
            driver.back()
            time.sleep(0.5)

    # Set GPS to Neuse River via Appium geolocation, then tap the My Location FAB
    map_screen.set_location_and_tap_my_location_fab(
        NEUSE_RIVER_POINT["lat"], NEUSE_RIVER_POINT["lon"]
    )
    loaded = map_screen.wait_for_conditions_to_load(timeout=15)
    if not loaded:
        pytest.skip(
            "Neuse River conditions did not load — check network / API availability"
        )

    panel.ensure_expanded()
    panel.scroll_to_top()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Fishing Conditions", "Fishing Suitability"])
    if missing:
        pytest.fail(
            f"Shot 3: Conditions panel not showing expected content. Missing: {missing}\n"
            "The panel may be collapsed or the API response was empty."
        )

    take_screenshot(
        driver,
        filename="03_neuse_river_nc_inshore_redfish_suitability_score",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 4 — Best fishing times graph: 24-hour solunar activity bars
# ---------------------------------------------------------------------------

def test_04_best_fishing_times_solunar_activity_graph(driver, device_profile):
    """
    Capture the 24-hour fishing activity graph at the Neuse River, NC.

    Shows: The "Best Fishing Times Today" bar chart with solunar peaks.
    The tidal influence of Pamlico Sound creates strong, defined peaks
    that make this graph especially compelling for an inshore angler.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_fishing_times_graph()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Best Fishing Times Today"])
    if missing:
        pytest.fail(
            f"Shot 4: Fishing times graph not visible. Missing: {missing}\n"
            "The graph sits just below the suitability score — try scrolling up."
        )

    take_screenshot(
        driver,
        filename="04_neuse_river_nc_best_fishing_times_solunar_graph",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 5 — Marine conditions: water temp, waves, wind, pressure
# ---------------------------------------------------------------------------

def test_05_marine_conditions_water_temp_waves_wind(driver, device_profile):
    """
    Capture the marine conditions data grid at the Neuse River, NC.

    Shows: Water temperature, wave height, current speed, wind speed,
    air temperature, and barometric pressure sourced from Open-Meteo / NOAA.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_marine_conditions()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Marine Conditions"])
    if missing:
        pytest.fail(
            f"Shot 5: Marine conditions section not visible. Missing: {missing}\n"
            "The panel may need more scrolling or the API did not return water data."
        )

    take_screenshot(
        driver,
        filename="05_neuse_river_nc_real_time_marine_conditions",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 6 — Tide information: next high and low tide times
# ---------------------------------------------------------------------------

def test_06_tide_information_high_low_tide_times(driver, device_profile):
    """
    Capture the tide information section at the Neuse River, NC.

    Shows: Next high and low tide times with heights, and current tide
    phase. Pamlico Sound tidal influence creates consistent, fishable
    tidal movement — exactly why tide timing matters here.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_tide_information()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Tide Information"])
    if missing:
        pytest.fail(
            f"Shot 6: Tide section not visible. Missing: {missing}\n"
            "The NOAA tide API may not have returned data for this location."
        )

    take_screenshot(
        driver,
        filename="06_neuse_river_nc_tide_high_low_times",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 7 — Sun and moon: moon phase and solunar periods
# ---------------------------------------------------------------------------

def test_07_sun_moon_phase_and_solunar_peak_periods(driver, device_profile):
    """
    Capture the Sun & Moon and Solunar Periods sections.

    Shows: Sunrise/sunset times, moon phase name, moon illumination
    percentage, and colour-coded major (🟢) and minor (🟡) solunar periods.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_sun_and_moon()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Sun & Moon"])
    if missing:
        pytest.fail(
            f"Shot 7: Sun & Moon section not visible. Missing: {missing}\n"
            "The astronomical section may not have scrolled into view."
        )

    take_screenshot(
        driver,
        filename="07_moon_phase_sunrise_sunset_and_solunar_feeding_periods",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 8 — 10-day date selector: forecast confidence strip
# ---------------------------------------------------------------------------

def test_08_ten_day_forecast_date_selector_with_confidence(driver, device_profile):
    """
    Capture the 10-day forecast date selector.

    Shows: The horizontal scrollable strip of date cards, each showing the
    day, date, and forecast confidence percentage. Communicates this is a
    planning tool, not just a current-conditions checker.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_date_selector()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Select Forecast Date", "Today"])
    if missing:
        pytest.fail(
            f"Shot 8: Date selector strip not visible. Missing: {missing}\n"
            "The date selector sits at the top of the expanded panel."
        )

    take_screenshot(
        driver,
        filename="08_ten_day_forecast_strip_with_confidence_percentage",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 9 — Frying Pan Tower: offshore forecast (Mahi-Mahi)
# ---------------------------------------------------------------------------

def test_09_frying_pan_tower_offshore_mahi_forecast(driver, device_profile):
    """
    Capture the fishing forecast at Frying Pan Tower, NC.

    Shows: The suitability score for Mahi-Mahi at this iconic offshore
    structure, 34 miles off the NC coast. Demonstrates the app works for
    offshore fishing, not just inshore — wave height and wind carry more
    weight out here than tides.
    """
    map_screen = MapScreen(driver)
    panel = ConditionsPanel(driver)

    # Select Mahi Mahi — exact name as defined in FishSpeciesDatabase
    map_screen.open_species_filter()
    time.sleep(0.8)
    found = map_screen.select_species("Mahi Mahi (Dolphin Fish)")
    if not found:
        elem = find_by_text_contains(driver, "Mahi")
        if elem:
            elem.click()
            time.sleep(1.0)
        else:
            driver.back()
            time.sleep(0.5)

    # Set GPS to Frying Pan Tower via Appium geolocation, then tap the My Location FAB
    map_screen.set_location_and_tap_my_location_fab(
        FRYING_PAN_TOWER["lat"], FRYING_PAN_TOWER["lon"]
    )
    loaded = map_screen.wait_for_conditions_to_load(timeout=15)
    if not loaded:
        pytest.skip("Frying Pan Tower conditions did not load")

    panel.ensure_expanded()
    panel.scroll_to_top()
    time.sleep(1.5)

    missing = verify_screen_elements(driver, ["Fishing Conditions", "Fishing Suitability"])
    if missing:
        pytest.fail(
            f"Shot 9: Frying Pan Tower forecast not ready. Missing: {missing}\n"
            "The conditions panel may not have updated for the offshore location."
        )

    take_screenshot(
        driver,
        filename="09_frying_pan_tower_offshore_mahi_mahi_forecast",
        device_profile=device_profile,
    )


# ---------------------------------------------------------------------------
# Shot 10 — Frying Pan Tower: offshore marine conditions (waves, wind)
# ---------------------------------------------------------------------------

def test_10_frying_pan_tower_offshore_marine_conditions(driver, device_profile):
    """
    Capture the marine conditions section at Frying Pan Tower.

    Shows: Offshore wave height, swell, wind speed — the conditions that
    matter most when running 34 miles offshore. A compelling contrast to
    the calmer nearshore data shown in Shot 5.
    """
    panel = ConditionsPanel(driver)
    panel.ensure_expanded()
    panel.scroll_to_marine_conditions()
    time.sleep(1.0)

    missing = verify_screen_elements(driver, ["Marine Conditions"])
    if missing:
        pytest.fail(
            f"Shot 10: Offshore marine conditions not visible. Missing: {missing}\n"
            "Still showing data from the previous location — tap the map again."
        )

    take_screenshot(
        driver,
        filename="10_frying_pan_tower_offshore_wave_height_wind_conditions",
        device_profile=device_profile,
    )

