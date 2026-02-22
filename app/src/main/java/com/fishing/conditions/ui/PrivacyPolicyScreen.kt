package com.fishing.conditions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PrivacyHeading("Tide Runner – Privacy Policy")
            PrivacyBody("Effective date: February 22, 2026")
            Spacer(modifier = Modifier.height(8.dp))
            PrivacyBody(
                "This Privacy Policy describes how Tide Runner (\"the App\", \"we\", \"us\") " +
                    "handles information when you use our Android application. We are committed " +
                    "to protecting your privacy and being transparent about what data is accessed " +
                    "and how it is used."
            )

            PrivacySectionTitle("1. Information We Collect")
            PrivacyBody(
                "We do not collect, store, or transmit any personally identifiable information (PII). " +
                    "No account creation or login is required to use the App."
            )
            PrivacySubTitle("1.1 Location Data")
            PrivacyBody(
                "The App requests access to your device's precise location " +
                    "(ACCESS_FINE_LOCATION) and approximate location (ACCESS_COARSE_LOCATION) " +
                    "solely to center the map on your current position and to fetch marine and " +
                    "weather forecast data for that location. " +
                    "\n\n" +
                    "• Location data is used only in real time within the App.\n" +
                    "• Location coordinates are sent to third-party weather and tide APIs (see Section 3) as query parameters to retrieve relevant forecast data.\n" +
                    "• We do not store, log, or share your location history.\n" +
                    "• Location is never collected while the App is in the background."
            )
            PrivacySubTitle("1.2 Device & Usage Data")
            PrivacyBody(
                "We do not collect device identifiers, advertising IDs, crash telemetry, " +
                    "usage analytics, or any other diagnostic data. No analytics SDK is integrated " +
                    "into the App."
            )
            PrivacySubTitle("1.3 Local Cache")
            PrivacyBody(
                "Marine forecast data fetched from external APIs is cached locally on your " +
                    "device using a Room database to reduce unnecessary network requests and " +
                    "improve performance. This cached data contains only forecast values " +
                    "(e.g., wave height, sea temperature, tide levels) associated with a " +
                    "geographic coordinate. It is stored on-device and is never transmitted " +
                    "to us or any third party."
            )

            PrivacySectionTitle("2. Permissions Used")
            PrivacyBody("The App requests the following Android permissions:")
            PrivacyBody(
                "• ACCESS_FINE_LOCATION – Required to obtain your precise GPS coordinates " +
                    "for map centering and location-specific forecasts.\n" +
                    "• ACCESS_COARSE_LOCATION – Required alongside fine location as per " +
                    "Android platform requirements.\n" +
                    "• INTERNET – Required to fetch live marine, weather, and tide data " +
                    "from free public APIs.\n" +
                    "• ACCESS_NETWORK_STATE – Required to check network availability before " +
                    "making API requests.\n" +
                    "• ACCESS_WIFI_STATE – Required to determine the network connection type.\n" +
                    "• WRITE_EXTERNAL_STORAGE (Android ≤ 9 only) – Required by the map tile " +
                    "library (osmdroid) to cache map tiles on older devices."
            )

            PrivacySectionTitle("3. Third-Party Services")
            PrivacyBody(
                "The App communicates with the following third-party services. Your " +
                    "geographic coordinates (latitude and longitude) are sent to these services " +
                    "as part of API requests. No other personal data is transmitted."
            )
            PrivacyBody(
                "• Open-Meteo (open-meteo.com) – Weather and marine forecasts. " +
                    "Privacy policy: https://open-meteo.com/en/terms\n\n" +
                    "• NOAA CO-OPS API (tidesandcurrents.noaa.gov) – Tide predictions and " +
                    "water level data. A U.S. government public data service.\n\n" +
                    "• Solunar API – Solunar activity period calculations (moon phase, " +
                    "solar/lunar tables).\n\n" +
                    "• OpenStreetMap (openstreetmap.org) – Map tile rendering. " +
                    "Privacy policy: https://wiki.osmfoundation.org/wiki/Privacy_Policy\n\n" +
                    "• OpenSeaMap (openseamap.org) – Nautical chart overlay tiles.\n\n" +
                    "• IP-API / IP Geolocation – Used as a fallback to provide an initial map " +
                    "region when location permission is not granted. Only a network request " +
                    "originating from your device's IP address is made; no personal data " +
                    "is explicitly sent."
            )
            PrivacyBody(
                "We encourage you to review the privacy policies of these third-party " +
                    "services, as we have no control over their data practices."
            )

            PrivacySectionTitle("4. Data Sharing & Sale")
            PrivacyBody(
                "We do not sell, trade, rent, or share any personal data with third parties " +
                    "for marketing or commercial purposes. We have no backend servers, databases, " +
                    "or user accounts. The only data leaving your device is the location " +
                    "coordinates sent directly to the public APIs listed in Section 3."
            )

            PrivacySectionTitle("5. Data Retention")
            PrivacyBody(
                "We do not retain any user data. Forecast data cached locally on your device " +
                    "can be cleared at any time by clearing the App's storage via Android " +
                    "Settings → Apps → Tide Runner → Storage → Clear Cache / Clear Data."
            )

            PrivacySectionTitle("6. Children's Privacy")
            PrivacyBody(
                "The App is not directed at children under the age of 13. We do not " +
                    "knowingly collect personal information from children. If you believe a " +
                    "child has provided personal information through the App, please contact us " +
                    "so we can take appropriate action."
            )

            PrivacySectionTitle("7. Security")
            PrivacyBody(
                "All network communications use HTTPS. Since no personal data is stored on " +
                    "our servers (we have none), the risk of a data breach affecting user " +
                    "personal information is minimal. Map tiles and forecast data cached on " +
                    "your device are stored in the App's private storage directory."
            )

            PrivacySectionTitle("8. Your Rights")
            PrivacyBody(
                "Because we do not collect or store personal data, there is no user profile " +
                    "to access, correct, or delete. You may revoke location permission at any " +
                    "time via Android Settings → Apps → Tide Runner → Permissions. Revoking " +
                    "location permission will disable the \"My Location\" feature but the App " +
                    "will remain fully functional for manually tapped map locations."
            )

            PrivacySectionTitle("9. Changes to This Policy")
            PrivacyBody(
                "We may update this Privacy Policy from time to time. Any changes will be " +
                    "reflected in an updated version of this screen within the App and at:\n\n" +
                    "https://vinnimous.github.io/TideRunner/privacy-policy.html\n\n" +
                    "Continued use of the App after changes are posted constitutes acceptance " +
                    "of the updated policy."
            )

            PrivacySectionTitle("10. Contact Us")
            PrivacyBody(
                "If you have any questions or concerns about this Privacy Policy or the App's " +
                    "data practices, please contact us via the Google Play Store developer page.\n\n" +
                    "Full policy also available at:\n" +
                    "https://vinnimous.github.io/TideRunner/privacy-policy.html"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrivacyHeading(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun PrivacySectionTitle(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun PrivacySubTitle(text: String) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun PrivacyBody(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.85f),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}










