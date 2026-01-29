package com.fishing.conditions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fishing.conditions.domain.CoordinateConverter

@Composable
fun CoordinateDisplay(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Location",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lat: ${CoordinateConverter.toDegreesMinutesSeconds(latitude, true)}",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Lon: ${CoordinateConverter.toDegreesMinutesSeconds(longitude, false)}",
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Decimal: ${"%.5f".format(latitude)}, ${"%.5f".format(longitude)}",
                style = MaterialTheme.typography.caption
            )
        }
    }
}
