"""
pytest conftest — Appium driver fixture for TideRunner screenshot capture.

Sets up and tears down the Appium WebDriver session for each test module.
The session is scoped to the module so the app stays alive across all
screenshot captures in a single run, avoiding expensive restarts.

Prerequisites before running:
  1. Appium 2 server running:    bash screenshots/start_appium.sh
  2. Emulator running:           Pixel_4 AVD started from Android Studio
  3. Debug APK installed:        ./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
  4. Python deps installed:      pip install -r requirements.txt
"""

import os
import sys
import time

import pytest
from appium import webdriver
from appium.options.common.base import AppiumOptions

# Make helpers/screens importable from any test file
sys.path.insert(0, os.path.dirname(__file__))

from config.devices import PIXEL_4, APP_PACKAGE, APP_ACTIVITY, APPIUM_HOST
from helpers.app_guard import ensure_app_running


def _build_options(device_profile: dict) -> AppiumOptions:
    """
    Build Appium capabilities using AppiumOptions for UiAutomator2.

    AppiumOptions.load_capabilities() is used instead of UiAutomator2Options
    because Appium-Python-Client 3.1.0 does not re-export UiAutomator2Options
    from appium.options — it only exists deep in the submodule tree and the
    top-level __init__.py is empty. AppiumOptions is the stable public API.
    """
    caps = {
        # W3C standard
        "platformName": "Android",
        # Appium-prefixed caps
        "appium:deviceName": device_profile["device_name"],
        "appium:platformVersion": device_profile["platform_version"],
        "appium:avd": device_profile["avd_name"],
        "appium:automationName": "UiAutomator2",
        # App under test — debug variant
        "appium:appPackage": APP_PACKAGE,
        "appium:appActivity": APP_ACTIVITY,
        # Don't reinstall / reset between sessions — speeds up iteration
        "appium:noReset": True,
        "appium:fullReset": False,
        # Auto-grant all runtime permissions (location, etc.)
        "appium:autoGrantPermissions": True,
        # Timeouts
        "appium:newCommandTimeout": 120,
        "appium:uiautomator2ServerLaunchTimeout": 60000,
    }
    return AppiumOptions().load_capabilities(caps)


@pytest.fixture(scope="module")
def driver():
    """
    Module-scoped Appium WebDriver fixture.

    The driver is created once per test module, keeping the app alive
    across all screenshot captures. This avoids the 10–15 s restart
    penalty between every individual shot.

    Yields the driver, then quits the session on teardown.
    """
    options = _build_options(PIXEL_4)

    print(f"\n[fixture] Connecting to Appium at {APPIUM_HOST}...")
    drv = webdriver.Remote(APPIUM_HOST, options=options)

    # Allow the app to fully launch and render before any test runs
    time.sleep(4)
    print("[fixture] App launched — ready for screenshot capture")

    yield drv

    print("\n[fixture] Tearing down Appium session")
    drv.quit()


@pytest.fixture(scope="module")
def device_profile():
    """
    Returns the device profile dict used by the current test module.
    Tests use this to pass to take_screenshot().
    """
    return PIXEL_4


@pytest.fixture(scope="module")
def app_guard(driver):
    """
    Returns a callable that any test can use to verify the app is running
    and bring it back to the foreground if it has been backgrounded or
    crashed.

    Usage in a test:
        def test_something(driver, device_profile, app_guard):
            app_guard()   # ← call before interacting with the app
    """
    def _guard():
        ensure_app_running(driver, APP_PACKAGE, APP_ACTIVITY)
    return _guard


@pytest.fixture(autouse=True)
def auto_app_health_check(request, driver):
    """
    Autouse fixture — runs automatically before EVERY test.

    Checks that the TideRunner app is in the foreground before each
    screenshot capture. If the app has crashed or been backgrounded
    (e.g. an OS dialog pushed it back), it will be automatically
    restarted so the test still has a valid app to interact with.

    This means you never need to manually call app_guard() — it happens
    silently before every single test.
    """
    # Only run the guard for tests that receive the 'driver' fixture
    # (i.e. actual screenshot tests, not collection-only runs)
    if "driver" in request.fixturenames:
        ensure_app_running(driver, APP_PACKAGE, APP_ACTIVITY)


