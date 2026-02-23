"""
Page Object for the TideRunner MapScreen.

Handles all interactions with the main map view:
  - Tapping a map coordinate to trigger a marine forecast
  - Tapping the "My Location" FAB
  - Opening / closing the overflow menu

The map uses OSMDroid rendered inside an AndroidView composable.
Because Compose wraps it in a View, we interact with it via raw tap
coordinates rather than accessibility IDs.
"""

import time

from appium.webdriver import Remote as AppiumDriver
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common.exceptions import NoSuchElementException
from helpers.driver_utils import (
    find_by_text,
    find_by_accessibility_id,
    grant_location_permissions,
    wait_for_text,
)
from helpers.adb_location import set_appium_location

# How long to poll for the loading spinner to disappear after a tap (seconds)
_POST_TAP_SETTLE_TIMEOUT = 15


class MapScreen:
    """
    Encapsulates all interactions with the main MapScreen composable.
    """

    def __init__(self, driver: AppiumDriver) -> None:
        self._driver = driver

    # ------------------------------------------------------------------
    # Launch / startup
    # ------------------------------------------------------------------

    def handle_first_launch(self) -> None:
        """
        Grant location permissions that appear on the first app launch.
        Should be called once immediately after the driver starts.
        """
        grant_location_permissions(self._driver)

    # ------------------------------------------------------------------
    # Location selection
    # ------------------------------------------------------------------

    def set_location_and_tap_my_location_fab(self, lat: float, lon: float) -> None:
        """
        Set the device GPS location via the Appium geolocation command, then
        tap the "My Location" FAB to trigger MapViewModel.updateLocation().

        Flow:
          1. driver.set_location(lat, lon) — sets GPS via Appium W3C geolocation
             API (feeds into FusedLocationProviderClient).
          2. Tap the FAB whose contentDescription is "My Location".
             The FAB calls getCurrentLocation() → FusedLocationProviderClient
             → viewModel.updateLocation(lat, lon) → API fetch starts.
          3. Wait up to 15 s for the loading spinner to clear.

        See: https://appium.readthedocs.io/en/stable/en/commands/session/geolocation/set-geolocation/

        Args:
            lat: Target latitude  (e.g. 35.0196).
            lon: Target longitude (e.g. -76.6989).
        """
        # Step 1 — inject location via Appium geolocation command
        set_appium_location(self._driver, lat, lon)

        # Step 2 — tap the "My Location" FAB (contentDescription set in MapScreen.kt)
        fab = find_by_accessibility_id(self._driver, "My Location")
        if fab:
            fab.click()
            print(f"[map] Tapped 'My Location' FAB → ({lat}, {lon})")
        else:
            # Fallback: find by the FAB position at bottom-right of screen
            print("[map] 'My Location' FAB not found by accessibility id — using coordinate tap")
            size = self._driver.get_window_size()
            tap_x = int(size["width"] * 0.88)
            tap_y = int(size["height"] * 0.72)
            self._driver.tap([(tap_x, tap_y)])
            print(f"[map] Tapped FAB fallback position ({tap_x}, {tap_y})")

        # Step 3 — wait for the loading spinner to disappear
        self._wait_for_loading_to_finish(timeout=_POST_TAP_SETTLE_TIMEOUT)

    def wait_for_conditions_to_load(self, timeout: int = 15) -> bool:
        """
        Wait until the Conditions Panel header text appears, confirming
        that the API call completed and the UI has updated.

        Polls every 500 ms for up to `timeout` seconds (default 15 s).
        Returns True if loaded, False on timeout.
        """
        print(f"[map] Waiting up to {timeout}s for 'Fishing Conditions'...")
        deadline = time.time() + timeout
        while time.time() < deadline:
            if find_by_text(self._driver, "Fishing Conditions"):
                time.sleep(1.0)
                print("[map] Conditions panel loaded ✅")
                return True
            time.sleep(0.5)
        print("[map] ⚠️  Timed out waiting for conditions panel")
        return False

    def _wait_for_loading_to_finish(self, timeout: int = 15) -> None:
        """
        Wait up to `timeout` seconds for the CircularProgressIndicator
        (loading spinner) to disappear from the screen.

        Falls back gracefully if no spinner was shown (fast response).
        """
        print(f"[map] Waiting up to {timeout}s for loading to finish...")
        deadline = time.time() + timeout
        spinner_seen = False

        while time.time() < deadline:
            try:
                self._driver.find_element(
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    'new UiSelector().className("android.widget.ProgressBar")',
                )
                spinner_seen = True
                time.sleep(0.5)
            except NoSuchElementException:
                if spinner_seen:
                    print("[map] Loading spinner gone — UI settled ✅")
                else:
                    print("[map] No spinner detected — response was immediate")
                time.sleep(0.5)
                return

        print(f"[map] ⚠️  Loading did not finish within {timeout}s")

    # ------------------------------------------------------------------
    # Species selection
    # ------------------------------------------------------------------

    def open_species_filter(self) -> None:
        """
        Tap the "Filter by Species" card at the top of the screen to open
        the species dropdown popup.
        """
        elem = find_by_text(self._driver, "Filter by Species")
        if elem:
            elem.click()
            time.sleep(0.8)
        else:
            print("[map] 'Filter by Species' card not found — trying coordinate tap")
            size = self._driver.get_window_size()
            self._driver.tap([(size["width"] // 2, int(size["height"] * 0.10))])
            time.sleep(0.8)

    def select_species(self, species_name: str) -> bool:
        """
        Select a species by name from the open species dropdown.

        Returns True if found and tapped, False otherwise.
        """
        elem = find_by_text(self._driver, species_name)
        if elem:
            elem.click()
            time.sleep(1.0)
            print(f"[map] Selected species: {species_name}")
            return True
        print(f"[map] Species '{species_name}' not found in dropdown")
        return False

    # ------------------------------------------------------------------
    # UI state queries
    # ------------------------------------------------------------------

    def is_conditions_panel_visible(self) -> bool:
        return find_by_text(self._driver, "Fishing Conditions") is not None

    def wait_for_map_tiles(self, seconds: float = 3.0) -> None:
        """Wait for OSMDroid tiles to finish rendering."""
        time.sleep(seconds)
