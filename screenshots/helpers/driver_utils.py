"""
General Appium driver utilities.

Provides wait helpers, permission grant shortcuts, and dialog dismissal
so individual tests stay focused on navigation rather than boilerplate.
"""

import time

from appium.webdriver import Remote as AppiumDriver
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common.exceptions import (
    NoSuchElementException,
    TimeoutException,
    WebDriverException,
)
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait


# ---------------------------------------------------------------------------
# Wait helpers
# ---------------------------------------------------------------------------

def wait_for_text(driver: AppiumDriver, text: str, timeout: int = 20) -> bool:
    """
    Wait until an element with the given visible text appears on screen.

    Returns True if found within timeout, False otherwise.
    """
    try:
        WebDriverWait(driver, timeout).until(
            EC.presence_of_element_located(
                (AppiumBy.ANDROID_UIAUTOMATOR, f'new UiSelector().text("{text}")')
            )
        )
        return True
    except TimeoutException:
        print(f"[wait] Timed out waiting for text: '{text}'")
        return False


def wait_for_accessibility_id(
    driver: AppiumDriver, acc_id: str, timeout: int = 20
) -> bool:
    """
    Wait until an element with the given content description appears.
    """
    try:
        WebDriverWait(driver, timeout).until(
            EC.presence_of_element_located((AppiumBy.ACCESSIBILITY_ID, acc_id))
        )
        return True
    except TimeoutException:
        print(f"[wait] Timed out waiting for accessibility id: '{acc_id}'")
        return False


def find_by_text(driver: AppiumDriver, text: str):
    """
    Find a single element by its exact visible text.
    Returns the element or None.
    """
    try:
        return driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR, f'new UiSelector().text("{text}")'
        )
    except NoSuchElementException:
        return None


def find_by_text_contains(driver: AppiumDriver, partial_text: str):
    """
    Find a single element whose text contains the given substring.
    Returns the element or None.
    """
    try:
        return driver.find_element(
            AppiumBy.ANDROID_UIAUTOMATOR,
            f'new UiSelector().textContains("{partial_text}")',
        )
    except NoSuchElementException:
        return None


def find_by_accessibility_id(driver: AppiumDriver, acc_id: str):
    """
    Find a single element by its content description / accessibility id.
    Returns the element or None.
    """
    try:
        return driver.find_element(AppiumBy.ACCESSIBILITY_ID, acc_id)
    except NoSuchElementException:
        return None


# ---------------------------------------------------------------------------
# Permission handling
# ---------------------------------------------------------------------------

def grant_location_permissions(driver: AppiumDriver) -> None:
    """
    Dismiss the location permission dialog that appears on first launch.

    Tries to tap "While using the app" first, falls back to "Allow".
    """
    permission_buttons = [
        "While using the app",
        "Only this time",
        "Allow",
        "ALLOW",
    ]
    # Give the dialog up to 5 s to appear
    time.sleep(2)
    for btn_text in permission_buttons:
        elem = find_by_text(driver, btn_text)
        if elem:
            elem.click()
            print(f"[permissions] Granted via: '{btn_text}'")
            time.sleep(1)
            return
    print("[permissions] No permission dialog found — already granted or not shown.")


def dismiss_any_dialog(driver: AppiumDriver) -> None:
    """
    Dismiss any visible system dialog by tapping the first positive button.
    """
    for btn in ["OK", "Allow", "Accept", "Close", "Dismiss"]:
        elem = find_by_text(driver, btn)
        if elem:
            elem.click()
            time.sleep(0.5)
            return


# ---------------------------------------------------------------------------
# Scroll helpers
# ---------------------------------------------------------------------------

def scroll_down(driver: AppiumDriver, swipes: int = 1) -> None:
    """
    Scroll down within the conditions panel using a swipe gesture.
    """
    size = driver.get_window_size()
    width = size["width"]
    height = size["height"]

    start_x = width // 2
    start_y = int(height * 0.65)
    end_y = int(height * 0.35)

    for _ in range(swipes):
        driver.swipe(start_x, start_y, start_x, end_y, duration=600)
        time.sleep(0.5)


def scroll_up(driver: AppiumDriver, swipes: int = 1) -> None:
    """
    Scroll up within the conditions panel using a swipe gesture.
    """
    size = driver.get_window_size()
    width = size["width"]
    height = size["height"]

    start_x = width // 2
    start_y = int(height * 0.35)
    end_y = int(height * 0.65)

    for _ in range(swipes):
        driver.swipe(start_x, start_y, start_x, end_y, duration=600)
        time.sleep(0.5)

