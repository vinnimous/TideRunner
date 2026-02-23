"""
App health guard — verifies the app is in the foreground and restarts it if not.

Used before each screenshot capture to guarantee we're not taking a blank
screen, a crashed app, or the wrong activity.
"""

import time
import subprocess

from appium.webdriver import Remote as AppiumDriver
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common.exceptions import WebDriverException


def get_foreground_package(driver: AppiumDriver) -> str | None:
    """
    Returns the package name of the currently active (foreground) app,
    or None if it cannot be determined.
    """
    try:
        # Appium's currentPackage attribute — works on UiAutomator2
        return driver.current_package
    except WebDriverException:
        return None


def is_app_in_foreground(driver: AppiumDriver, app_package: str) -> bool:
    """
    Returns True if the given app package is currently in the foreground.
    """
    foreground = get_foreground_package(driver)
    result = foreground == app_package
    if not result:
        print(f"[guard] App not in foreground. Current: '{foreground}', expected: '{app_package}'")
    return result


def ensure_app_running(driver: AppiumDriver, app_package: str, app_activity: str) -> None:
    """
    Ensures the TideRunner app is running and in the foreground.

    If the app has been backgrounded, crashed, or is showing the wrong
    activity, this will bring it back to the foreground or restart it.

    Strategy:
      1. Check if the correct package is in the foreground.
      2. If not — try activate_app() first (fast resume).
      3. If that fails or the wrong activity is showing — full restart
         via terminate + activate.

    Args:
        driver:       Active Appium WebDriver.
        app_package:  e.g. "com.fishing.conditions.debug"
        app_activity: e.g. "com.fishing.conditions.ui.MainActivity"
    """
    if is_app_in_foreground(driver, app_package):
        return  # All good

    print(f"[guard] App not in foreground — attempting to resume...")

    try:
        # Fast path: bring existing instance to foreground
        driver.activate_app(app_package)
        time.sleep(2.0)

        if is_app_in_foreground(driver, app_package):
            print(f"[guard] App resumed successfully via activate_app()")
            return
    except WebDriverException as e:
        print(f"[guard] activate_app() failed: {e}")

    # Slow path: full restart
    print(f"[guard] Performing full app restart...")
    try:
        driver.terminate_app(app_package)
        time.sleep(1.0)
    except WebDriverException:
        pass  # Already dead — that's fine

    try:
        driver.activate_app(app_package)
        time.sleep(3.0)
        print(f"[guard] App restarted successfully")
    except WebDriverException as e:
        print(f"[guard] ⚠️  Could not restart app: {e}")


def verify_screen_elements(driver: AppiumDriver, expected_elements: list[str]) -> list[str]:
    """
    Checks that the expected UI elements are currently visible on screen.

    Args:
        driver:            Active Appium WebDriver.
        expected_elements: List of text strings or accessibility IDs that
                           should be visible before taking a screenshot.

    Returns:
        List of elements that were NOT found (empty = all present = good).
    """
    missing = []
    for element_text in expected_elements:
        found = False

        # Try exact text match first
        try:
            driver.find_element(
                AppiumBy.ANDROID_UIAUTOMATOR,
                f'new UiSelector().text("{element_text}")'
            )
            found = True
        except Exception:
            pass

        # Fall back to text contains
        if not found:
            try:
                driver.find_element(
                    AppiumBy.ANDROID_UIAUTOMATOR,
                    f'new UiSelector().textContains("{element_text}")'
                )
                found = True
            except Exception:
                pass

        # Fall back to accessibility ID
        if not found:
            try:
                driver.find_element(AppiumBy.ACCESSIBILITY_ID, element_text)
                found = True
            except Exception:
                pass

        if not found:
            missing.append(element_text)
            print(f"[verify] ⚠️  Expected element not found: '{element_text}'")
        else:
            print(f"[verify] ✅  Found: '{element_text}'")

    return missing

