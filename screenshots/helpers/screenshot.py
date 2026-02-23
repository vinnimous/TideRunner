"""
Screenshot capture helper.

Saves screenshots to the output/ folder with descriptive, human-readable
file names that explain what is being shown — suitable for Play Store
submission and manual review.

File naming convention:
    {shot_number:02d}_{short_descriptor}.png
    e.g.  01_map_overview_gulf_coast.png
"""

import os
import time
from pathlib import Path

from PIL import Image

# Root output folder (relative to this file → screenshots/output/)
OUTPUT_ROOT = Path(__file__).parent.parent / "output"

# Minimum short-side dimension required by Google Play Store
PLAY_STORE_MIN_PX = 1000


def take_screenshot(driver, filename: str, device_profile: dict) -> Path:
    """
    Capture a screenshot and save it to the correct output sub-folder.

    Args:
        driver:         Active Appium WebDriver instance.
        filename:       Descriptive filename WITHOUT extension.
                        Example: "01_map_overview_gulf_coast"
        device_profile: Device dict from config/devices.py (must contain
                        "output_folder" key).

    Returns:
        Path to the saved PNG file.

    Raises:
        ValueError: If the captured image is smaller than Play Store minimum.
    """
    folder = OUTPUT_ROOT / device_profile["output_folder"]
    folder.mkdir(parents=True, exist_ok=True)

    dest = folder / f"{filename}.png"

    # Appium returns raw PNG bytes
    png_bytes = driver.get_screenshot_as_png()
    dest.write_bytes(png_bytes)

    # Validate dimensions for Play Store compliance
    with Image.open(dest) as img:
        width, height = img.size
        short_side = min(width, height)
        if short_side < PLAY_STORE_MIN_PX:
            raise ValueError(
                f"Screenshot '{filename}' is too small for Play Store: "
                f"{width}x{height} (min {PLAY_STORE_MIN_PX}px on short side)."
            )
        print(
            f"[screenshot] Saved: {dest.relative_to(OUTPUT_ROOT.parent)} "
            f"({width}x{height})"
        )

    return dest


def clear_output_folder(device_profile: dict) -> None:
    """
    Remove all PNGs from a device's output folder before a fresh run.
    """
    folder = OUTPUT_ROOT / device_profile["output_folder"]
    if folder.exists():
        for f in folder.glob("*.png"):
            f.unlink()
        print(f"[screenshot] Cleared: {folder}")

