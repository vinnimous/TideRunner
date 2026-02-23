#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# TideRunner Screenshot Capture — convenience launcher
#
# Run this from anywhere:
#   cd /home/vinny/StudioProjects/TideRunner
#   bash screenshots/run_screenshots.sh
#
# Prerequisites:
#   1. Appium server running in another terminal:  appium --port 4723
#   2. Pixel 4 emulator running (started from Android Studio AVD Manager)
#   3. Debug APK installed on the emulator:
#        ./gradlew assembleDebug
#        adb install -r app/build/outputs/apk/debug/app-debug.apk
# ---------------------------------------------------------------------------

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VENV_PYTHON="$SCRIPT_DIR/.venv/bin/python"

# ── Android SDK — find and export ANDROID_HOME if not already set ──────────
# Appium's uiautomator2 driver requires this to locate adb and build tools.
# Android Studio on Linux installs to ~/Android/Sdk by default.

if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    CANDIDATE="$HOME/Android/Sdk"
    if [ -d "$CANDIDATE" ]; then
        export ANDROID_HOME="$CANDIDATE"
        export ANDROID_SDK_ROOT="$CANDIDATE"
        export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$PATH"
        echo "✅  Found Android SDK at $ANDROID_HOME"
    else
        echo ""
        echo "❌  ANDROID_HOME is not set and ~/Android/Sdk does not exist."
        echo "    Either:"
        echo "      a) Add to your ~/.bashrc:  export ANDROID_HOME=\$HOME/Android/Sdk"
        echo "      b) Or pass it inline:      ANDROID_HOME=~/Android/Sdk bash screenshots/run_screenshots.sh"
        echo ""
        exit 1
    fi
else
    echo "✅  Android SDK: ${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
fi

# Also make sure adb is on PATH
if ! command -v adb &>/dev/null; then
    export PATH="${ANDROID_HOME}/platform-tools:$PATH"
fi

# ── Sanity checks ──────────────────────────────────────────────────────────

if [ ! -f "$VENV_PYTHON" ]; then
    echo ""
    echo "❌  Virtual environment not found at $SCRIPT_DIR/.venv"
    echo "    Run this first:"
    echo "      cd $SCRIPT_DIR"
    echo "      python3 -m venv .venv"
    echo "      .venv/bin/pip install -r requirements.txt"
    echo ""
    exit 1
fi

if ! adb devices 2>/dev/null | grep -q "emulator"; then
    echo ""
    echo "⚠️   No emulator detected via adb. Make sure your Pixel 4 AVD is running."
    echo "    Continuing anyway in case adb path differs..."
    echo ""
fi

# ── Run ───────────────────────────────────────────────────────────────────

echo ""
echo "🎣  TideRunner Screenshot Capture"
echo "   Output folder: $SCRIPT_DIR/output/phone/"
echo ""

cd "$SCRIPT_DIR"
"$VENV_PYTHON" -m pytest tests/test_capture_screenshots.py -v "$@"

echo ""
echo "✅  Done. Screenshots saved to: $SCRIPT_DIR/output/phone/"

