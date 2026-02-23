"""
Page Object for the TideRunner ConditionsPanel.

Handles interactions with the slide-up conditions panel that appears
after tapping a location on the map. Supports:
  - Expanding / collapsing the panel
  - Scrolling to reveal specific data sections
  - Reading the fishing suitability score
"""

import time

from appium.webdriver import Remote as AppiumDriver
from helpers.driver_utils import (
    find_by_text,
    find_by_text_contains,
    find_by_accessibility_id,
    scroll_down,
    scroll_up,
    wait_for_text,
)


class ConditionsPanel:
    """
    Encapsulates all interactions with the ConditionsPanel composable.
    """

    def __init__(self, driver: AppiumDriver) -> None:
        self._driver = driver

    # ------------------------------------------------------------------
    # Panel visibility
    # ------------------------------------------------------------------

    def is_visible(self) -> bool:
        return find_by_text(self._driver, "Fishing Conditions") is not None

    def expand(self) -> None:
        """
        Expand the conditions panel if it is currently collapsed.
        The expand/collapse toggle is the "Fishing Conditions" header row.
        """
        expand_icon = find_by_accessibility_id(self._driver, "Expand")
        if expand_icon:
            expand_icon.click()
            time.sleep(0.8)
            print("[panel] Expanded conditions panel")
        else:
            print("[panel] Panel already expanded (no 'Expand' icon found)")

    def collapse(self) -> None:
        """
        Collapse the conditions panel.
        """
        collapse_icon = find_by_accessibility_id(self._driver, "Collapse")
        if collapse_icon:
            collapse_icon.click()
            time.sleep(0.8)
            print("[panel] Collapsed conditions panel")

    def ensure_expanded(self) -> None:
        """
        Guarantee the panel is in the expanded state.
        """
        if find_by_accessibility_id(self._driver, "Expand"):
            self.expand()

    # ------------------------------------------------------------------
    # Scrolling to sections
    # ------------------------------------------------------------------

    def scroll_to_top(self) -> None:
        """
        Scroll back to the top of the conditions panel (suitability score).
        """
        scroll_up(self._driver, swipes=4)
        time.sleep(0.5)

    def scroll_to_marine_conditions(self) -> None:
        """
        Scroll until the "Marine Conditions" section header is visible.
        This section contains water temp, wave height, wind, and pressure.
        """
        self._scroll_until_text_visible("Marine Conditions", max_swipes=5)

    def scroll_to_tide_information(self) -> None:
        """
        Scroll until the "Tide Information" section is visible.
        """
        self._scroll_until_text_visible("Tide Information", max_swipes=6)

    def scroll_to_sun_and_moon(self) -> None:
        """
        Scroll until the "Sun & Moon" section is visible.
        """
        self._scroll_until_text_visible("Sun & Moon", max_swipes=7)

    def scroll_to_solunar_periods(self) -> None:
        """
        Scroll until the "Solunar Periods" section is visible.
        """
        self._scroll_until_text_visible("Solunar Periods", max_swipes=8)

    def scroll_to_fishing_times_graph(self) -> None:
        """
        Scroll to show the "Best Fishing Times Today" graph.
        The graph sits just below the suitability score.
        """
        self._scroll_until_text_visible("Best Fishing Times Today", max_swipes=4)

    def scroll_to_date_selector(self) -> None:
        """
        Scroll to show the 10-day date forecast strip.
        """
        self._scroll_until_text_visible("Select Forecast Date", max_swipes=4)

    # ------------------------------------------------------------------
    # Data reads
    # ------------------------------------------------------------------

    def get_suitability_score_text(self) -> str | None:
        """
        Read the numeric suitability score from the score badge (0–100).
        The badge is a Box containing a Text with the integer score.
        """
        for rating in ["EXCELLENT", "GOOD", "FAIR", "POOR"]:
            elem = find_by_text(self._driver, rating)
            if elem:
                return rating
        return None

    def get_suitability_rating(self) -> str | None:
        """
        Return the human-readable suitability rating label visible in the panel.
        """
        return self.get_suitability_score_text()

    # ------------------------------------------------------------------
    # Internal helpers
    # ------------------------------------------------------------------

    def _scroll_until_text_visible(self, text: str, max_swipes: int = 8) -> bool:
        """
        Scroll down one swipe at a time until the given text is on screen,
        up to max_swipes attempts.

        Returns True if text found, False if not found after max swipes.
        """
        for i in range(max_swipes):
            if find_by_text(self._driver, text) or find_by_text_contains(self._driver, text):
                print(f"[panel] Found '{text}' after {i} swipe(s)")
                return True
            scroll_down(self._driver, swipes=1)
            time.sleep(0.4)
        print(f"[panel] Could not find '{text}' after {max_swipes} swipes")
        return False

