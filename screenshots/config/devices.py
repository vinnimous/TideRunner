"""
Device profile configuration for TideRunner screenshot capture.

The Pixel 4 AVD is the primary device used for Google Play Store screenshots.
Screen resolution: 1080x2280 @ 440dpi
"""

# Primary device: Pixel 4 AVD that is already set up locally
PIXEL_4 = {
    "avd_name": "Pixel_4",                  # Exact AVD name as reported by the emulator
    "device_name": "Pixel 4",
    "platform_version": "14.0",
    "screen_width": 1080,
    "screen_height": 2280,
    "output_folder": "phone",
}

# All profiles to run — add tablet profiles here later if needed
ALL_DEVICE_PROFILES = [PIXEL_4]

# App under test
APP_PACKAGE = "com.fishing.conditions.debug"
APP_ACTIVITY = "com.fishing.conditions.ui.MainActivity"

# Appium server
APPIUM_HOST = "http://127.0.0.1:4723"

