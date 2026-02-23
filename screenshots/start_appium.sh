#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Start the Appium server with ANDROID_HOME correctly set.
#
# Run this INSTEAD of a bare `appium` command:
#   bash screenshots/start_appium.sh
#
# Keep this terminal open while running screenshots.
# ---------------------------------------------------------------------------

# Android Studio default SDK location on Linux
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$HOME/Android/Sdk}"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$PATH"

if [ ! -d "$ANDROID_HOME" ]; then
    echo ""
    echo "❌  Android SDK not found at $ANDROID_HOME"
    echo "    Update ANDROID_HOME in this script to match your SDK location."
    exit 1
fi

echo "✅  ANDROID_HOME=$ANDROID_HOME"
echo "✅  adb: $(which adb 2>/dev/null || echo 'not on PATH yet — will use SDK path')"
echo ""
echo "🚀  Starting Appium server on port 4723..."
echo ""

appium --port 4723

