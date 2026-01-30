package com.fishing.conditions.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fishing.conditions.data.models.FishingSuitability
import com.fishing.conditions.data.models.MarineConditions
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConditionsPanel(
    conditions: MarineConditions,
    suitability: FishingSuitability?,
    modifier: Modifier = Modifier,
    selectedSpecies: com.fishing.conditions.data.models.Species? = null,
    selectedDate: Date = Date(),
    onDateSelected: (Date) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = if (isExpanded) 600.dp else 80.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with collapse/expand button and date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fishing Conditions",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatDateFull(selectedDate),
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colors.primary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                // Date selector (horizontal scroll for 10-day forecast)
                DateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider()

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
            // Fishing Suitability Score (if available)
            suitability?.let {
                FishingSuitabilityHeader(it)
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Fishing Times Graph
            FishingTimesGraph(
                conditions = conditions,
                species = selectedSpecies
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Marine Conditions Header
            Text(
                text = "Marine Conditions",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Water conditions
            Row(modifier = Modifier.fillMaxWidth()) {
                // Location coordinates
                Column(modifier = Modifier.weight(0.8f)) {
                    Text("Lat", style = MaterialTheme.typography.caption, color = Color.Gray)
                    Text("%.4f".format(conditions.latitude), style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Long", style = MaterialTheme.typography.caption, color = Color.Gray)
                    Text("%.4f".format(conditions.longitude), style = MaterialTheme.typography.body2)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    conditions.waterTemperature?.let {
                        ConditionItem("Water Temp", "${"%.1f".format(it)}°F")
                    }
                    conditions.waveHeight?.let {
                        ConditionItem("Wave Height", "${"%.2f".format(it)} ft")
                    }
                    conditions.currentSpeed?.let {
                        ConditionItem("Current", "${"%.2f".format(it)} mph")
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    conditions.windSpeed?.let {
                        ConditionItem("Wind Speed", "${"%.1f".format(it)} mph")
                    }
                    conditions.airTemperature?.let {
                        ConditionItem("Air Temp", "${"%.1f".format(it)}°F")
                    }
                    conditions.pressure?.let {
                        ConditionItem("Pressure", "${"%.0f".format(it)} hPa")
                    }
                }
            }

            // Tide information
            if (conditions.nextHighTide != null || conditions.nextLowTide != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tide Information",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    conditions.nextHighTide?.let { tide ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Next High Tide", style = MaterialTheme.typography.caption, color = Color.Gray)
                            Text(formatTime(tide.time), style = MaterialTheme.typography.body2)
                            Text("${String.format("%.2f", tide.height)} ft", style = MaterialTheme.typography.caption)
                        }
                    }
                    conditions.nextLowTide?.let { tide ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Next Low Tide", style = MaterialTheme.typography.caption, color = Color.Gray)
                            Text(formatTime(tide.time), style = MaterialTheme.typography.body2)
                            Text("${String.format("%.2f", tide.height)} ft", style = MaterialTheme.typography.caption)
                        }
                    }
                }

                conditions.currentTidePhase?.let { phase ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Current: ${phase.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            // Astronomical data
            if (conditions.sunrise != null || conditions.moonPhase != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sun & Moon",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        conditions.sunrise?.let {
                            ConditionItem("Sunrise", formatTime(it))
                        }
                        conditions.sunset?.let {
                            ConditionItem("Sunset", formatTime(it))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        conditions.moonPhase?.let {
                            Text("Moon Phase", style = MaterialTheme.typography.caption, color = Color.Gray)
                            Text(it.name.replace("_", " ").lowercase().replaceFirstChar { char -> char.uppercase() }, style = MaterialTheme.typography.body2)
                        }
                        conditions.moonIllumination?.let {
                            Text("${String.format("%.0f", it * 100)}% illuminated", style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }

            // Solunar periods
            if (!conditions.majorPeriods.isNullOrEmpty() || !conditions.minorPeriods.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solunar Periods (Best Fishing Times)",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                conditions.majorPeriods?.forEach { period ->
                    Text(
                        text = "🟢 Major: ${formatTime(period.startTime)} - ${formatTime(period.endTime)}",
                        style = MaterialTheme.typography.caption,
                        color = Color(0xFF4CAF50)
                    )
                }
                conditions.minorPeriods?.forEach { period ->
                    Text(
                        text = "🟡 Minor: ${formatTime(period.startTime)} - ${formatTime(period.endTime)}",
                        style = MaterialTheme.typography.caption,
                        color = Color(0xFFFFC107)
                    )
                }
            }

            // Data source
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Data: ${conditions.dataSource}",
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
                }
            }
        }
    }
}

@Composable
private fun FishingSuitabilityHeader(suitability: FishingSuitability) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Fishing Suitability",
                style = MaterialTheme.typography.subtitle2,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = suitability.rating.name,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = getSuitabilityColor(suitability.rating)
            )
        }

        // Score circle
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = getSuitabilityColor(suitability.rating).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${suitability.score.toInt()}",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = getSuitabilityColor(suitability.rating)
            )
        }
    }
}

@Composable
private fun ConditionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.caption, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.body2, fontWeight = FontWeight.Medium)
    }
}

private fun getSuitabilityColor(rating: FishingSuitability.Rating): Color {
    return when (rating) {
        FishingSuitability.Rating.EXCELLENT -> Color(0xFF4CAF50)
        FishingSuitability.Rating.GOOD -> Color(0xFF8BC34A)
        FishingSuitability.Rating.FAIR -> Color(0xFFFFC107)
        FishingSuitability.Rating.POOR -> Color(0xFFF44336)
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDateFull(date: Date): String {
    val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return sdf.format(date)
}

@Composable
private fun DateSelector(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val scrollState = rememberScrollState()
    val calendar = Calendar.getInstance()
    val today = Calendar.getInstance()

    Column {
        Text(
            text = "📅 Select Forecast Date",
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Generate 10 days of date cards
            for (dayOffset in 0..9) {
                calendar.time = today.time
                calendar.add(Calendar.DAY_OF_MONTH, dayOffset)
                val date = calendar.time
                val isSelected = isSameDay(date, selectedDate)

                // Calculate confidence based on day offset
                val confidence = when (dayOffset) {
                    0 -> 95
                    in 1..3 -> 90 - (dayOffset * 2)
                    in 4..7 -> 80 - (dayOffset * 2)
                    else -> 70 - (dayOffset * 2)
                }

                DateCard(
                    date = date,
                    isSelected = isSelected,
                    confidence = confidence,
                    dayOffset = dayOffset,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
private fun DateCard(
    date: Date,
    isSelected: Boolean,
    confidence: Int,
    dayOffset: Int,
    onClick: () -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = date

    Card(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick),
        elevation = if (isSelected) 6.dp else 2.dp,
        backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (dayOffset == 0) "Today" else SimpleDateFormat("EEE", Locale.getDefault()).format(date),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
            Text(
                text = SimpleDateFormat("MMM d", Locale.getDefault()).format(date),
                style = MaterialTheme.typography.body2,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$confidence%",
                style = MaterialTheme.typography.caption,
                color = if (isSelected) MaterialTheme.colors.onPrimary.copy(alpha = 0.8f) else Color.Gray
            )
        }
    }
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
