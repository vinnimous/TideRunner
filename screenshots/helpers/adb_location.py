"""
Geolocation injection helper.

Sets the device GPS location via the Appium W3C geolocation command:
  driver.set_location(lat, lon, altitude)

This is the documented Appium approach:
  https://appium.readthedocs.io/en/stable/en/commands/session/geolocation/set-geolocation/

It feeds directly into Android's FusedLocationProviderClient, which is what
TideRunner's MapScreen uses for the "My Location" FAB. No ADB console port
is required — the command goes through the UiAutomator2 server.
"""

import time

from appium.webdriver import Remote as AppiumDriver


def set_appium_location(
    driver: AppiumDriver,
    lat: float,
    lon: float,
    altitude: float = 0.0,
) -> None:
    """
    Set the device GPS location using the Appium geolocation command.

    Equivalent to the W3C WebDriver command:
      POST /session/{id}/location
      { "location": { "latitude": lat, "longitude": lon, "altitude": alt } }

    Args:
        driver:   Active Appium WebDriver session.
        lat:      Latitude in decimal degrees  (e.g. 35.0196).
        lon:      Longitude in decimal degrees (e.g. -76.6989).
        altitude: Altitude in metres (default 0).
    """
    driver.set_location(lat, lon, altitude)
    print(f"[location] Set via Appium geolocation: lat={lat}, lon={lon}, alt={altitude}")
    # Give FusedLocationProviderClient a moment to pick up the new fix
    time.sleep(1.5)
