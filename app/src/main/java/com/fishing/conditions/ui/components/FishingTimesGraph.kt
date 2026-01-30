package com.fishing.conditions.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishing.conditions.data.models.MarineConditions
import com.fishing.conditions.data.models.Species
import java.util.*

@Composable
fun FishingTimesGraph(
    conditions: MarineConditions,
    species: Species?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface.copy(alpha = 0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎣 Best Fishing Times Today",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            species?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Generate hourly data for 24 hours
        val hourlyScores = calculateHourlyScores(conditions, species)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Graph
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val width = size.width
            val height = size.height
            val padding = 20f
            val graphWidth = width - (padding * 2)
            val graphHeight = height - (padding * 2)

            // Draw grid lines
            val gridColor = Color.Gray.copy(alpha = 0.2f)
            for (i in 0..4) {
                val y = padding + (graphHeight / 4 * i)
                drawLine(
                    color = gridColor,
                    start = Offset(padding, y),
                    end = Offset(width - padding, y),
                    strokeWidth = 1f
                )
            }

            // Draw solunar period backgrounds
            conditions.majorPeriods?.forEach { period ->
                val startHour = getHourFromTimestamp(period.startTime)
                val endHour = getHourFromTimestamp(period.endTime)
                drawSolunarPeriod(
                    startHour, endHour, graphWidth, graphHeight, padding,
                    Color(0xFF4CAF50).copy(alpha = 0.15f)
                )
            }
            conditions.minorPeriods?.forEach { period ->
                val startHour = getHourFromTimestamp(period.startTime)
                val endHour = getHourFromTimestamp(period.endTime)
                drawSolunarPeriod(
                    startHour, endHour, graphWidth, graphHeight, padding,
                    Color(0xFFFFC107).copy(alpha = 0.15f)
                )
            }

            // Draw tide markers
            conditions.nextHighTide?.let { tide ->
                val hour = getHourFromTimestamp(tide.time)
                val x = padding + (graphWidth / 24 * hour)
                drawLine(
                    color = Color(0xFF2196F3).copy(alpha = 0.5f),
                    start = Offset(x, padding),
                    end = Offset(x, height - padding),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }
            conditions.nextLowTide?.let { tide ->
                val hour = getHourFromTimestamp(tide.time)
                val x = padding + (graphWidth / 24 * hour)
                drawLine(
                    color = Color(0xFF9C27B0).copy(alpha = 0.5f),
                    start = Offset(x, padding),
                    end = Offset(x, height - padding),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }

            // Draw fishing score line
            val path = Path()
            val points = hourlyScores.mapIndexed { hour, score ->
                val x = padding + (graphWidth / 24 * hour)
                val y = height - padding - (graphHeight * (score / 100f))
                Offset(x, y)
            }

            if (points.isNotEmpty()) {
                path.moveTo(points[0].x, points[0].y)
                points.drop(1).forEach { point ->
                    path.lineTo(point.x, point.y)
                }

                // Draw gradient fill under the line
                val gradientPath = Path().apply {
                    moveTo(points[0].x, height - padding)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, height - padding)
                    close()
                }

                drawPath(
                    path = gradientPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.3f),
                            Color(0xFF4CAF50).copy(alpha = 0.05f)
                        ),
                        startY = padding,
                        endY = height - padding
                    )
                )

                // Draw the line
                drawPath(
                    path = path,
                    color = Color(0xFF4CAF50),
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                // Draw dots on the line
                points.forEachIndexed { hour, point ->
                    val isCurrentHour = hour == currentHour
                    drawCircle(
                        color = if (isCurrentHour) Color(0xFFFF5722) else Color(0xFF4CAF50),
                        radius = if (isCurrentHour) 6f else 4f,
                        center = point
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("12 AM", style = MaterialTheme.typography.caption, fontSize = 10.sp, color = Color.Gray)
            Text("6 AM", style = MaterialTheme.typography.caption, fontSize = 10.sp, color = Color.Gray)
            Text("12 PM", style = MaterialTheme.typography.caption, fontSize = 10.sp, color = Color.Gray)
            Text("6 PM", style = MaterialTheme.typography.caption, fontSize = 10.sp, color = Color.Gray)
            Text("12 AM", style = MaterialTheme.typography.caption, fontSize = 10.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LegendItem(color = Color(0xFF4CAF50), text = "Fishing Score")
            LegendItem(color = Color(0xFF4CAF50).copy(alpha = 0.3f), text = "Major Period")
            LegendItem(color = Color(0xFF2196F3), text = "High Tide")
            LegendItem(color = Color(0xFFFF5722), text = "Now")
        }

        // Peak times summary
        val peakHours = hourlyScores
            .mapIndexed { hour, score -> hour to score }
            .filter { it.second >= 70 }
            .sortedByDescending { it.second }
            .take(3)

        if (peakHours.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🌟 Peak Times: ${peakHours.joinToString(", ") { formatHour(it.first) }}",
                style = MaterialTheme.typography.caption,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSolunarPeriod(
    startHour: Int,
    endHour: Int,
    graphWidth: Float,
    graphHeight: Float,
    padding: Float,
    color: Color
) {
    val startX = padding + (graphWidth / 24 * startHour)
    val width = (graphWidth / 24 * (endHour - startHour))
    drawRect(
        color = color,
        topLeft = Offset(startX, padding),
        size = Size(width, graphHeight)
    )
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = RoundedCornerShape(4.dp))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            fontSize = 9.sp,
            color = Color.Gray
        )
    }
}

private fun calculateHourlyScores(
    conditions: MarineConditions,
    species: Species?
): List<Float> {
    val scores = mutableListOf<Float>()
    val calendar = Calendar.getInstance()

    for (hour in 0..23) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        var hourScore = 40f // Base score

        // Get hourly data (assuming hourly lists are indexed by hour from current time)
        val hourlyIndex = hour // Assuming lists start from current hour
        val airTemp = conditions.airTemperature ?: 70.0
        val windSpeed = conditions.windSpeed ?: 5.0
        val waveHeight = conditions.waveHeight ?: 1.0
        val pressure = conditions.pressure ?: 1013.0
        val humidity = conditions.humidity ?: 70.0

        // Solunar period bonus
        val timestamp = calendar.timeInMillis
        conditions.majorPeriods?.forEach { period ->
            if (timestamp in period.startTime..period.endTime) {
                hourScore += 25f
            }
        }
        conditions.minorPeriods?.forEach { period ->
            if (timestamp in period.startTime..period.endTime) {
                hourScore += 15f
            }
        }

        // Sunrise/sunset bonus (dawn and dusk are prime times)
        conditions.sunrise?.let { sunrise ->
            val sunriseHour = getHourFromTimestamp(sunrise)
            if (hour in (sunriseHour - 1)..(sunriseHour + 1)) {
                hourScore += 15f
            }
        }
        conditions.sunset?.let { sunset ->
            val sunsetHour = getHourFromTimestamp(sunset)
            if (hour in (sunsetHour - 1)..(sunsetHour + 1)) {
                hourScore += 15f
            }
        }

        // Tide bonus (changing tides are good)
        conditions.nextHighTide?.let { tide ->
            val tideHour = getHourFromTimestamp(tide.time)
            if (hour in (tideHour - 1)..tideHour) {
                hourScore += 10f
            }
        }
        conditions.nextLowTide?.let { tide ->
            val tideHour = getHourFromTimestamp(tide.time)
            if (hour in (tideHour - 1)..tideHour) {
                hourScore += 10f
            }
        }

        // Species-specific environmental adjustments
        species?.let { fish ->
            // Temperature bonus (air temp as proxy for water temp)
            val tempMinF = cToF(fish.preferredWaterTemp.min)
            val tempMaxF = cToF(fish.preferredWaterTemp.max)
            if (airTemp >= tempMinF && airTemp <= tempMaxF) {
                hourScore += 10f
            }

            // Wind speed bonus
            if (windSpeed <= fish.preferredWindSpeed.max) {
                hourScore += 8f
            }

            // Wave height bonus
            if (waveHeight <= fish.preferredWaveHeight.max) {
                hourScore += 8f
            }

            // Moon phase bonus
            conditions.moonPhase?.let { moon ->
                if (fish.preferredMoonPhase.contains(moon)) {
                    hourScore += 10f
                }
            }

            // Tide phase bonus
            conditions.currentTidePhase?.let { tide ->
                if (fish.preferredTidePhase.contains(tide)) {
                    hourScore += 10f
                }
            }

            // Time preference (simplified)
            if (fish.name.contains("Catfish", ignoreCase = true) && hour in 18..23) {
                hourScore += 10f
            }
            if (fish.name.contains("Bass", ignoreCase = true) && hour in 5..9) {
                hourScore += 10f
            }
        }

        // Moon illumination bonus (higher illumination = better fishing)
        conditions.moonIllumination?.let { illumination ->
            hourScore += (illumination * 10f).toFloat() // 0-10 bonus
        }

        // Barometric pressure bonus (stable high pressure is good)
        if (pressure > 1010) {
            hourScore += 5f
        }

        scores.add(hourScore.coerceIn(0f, 100f))
    }

    return scores
}

private fun getHourFromTimestamp(timestamp: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return calendar.get(Calendar.HOUR_OF_DAY)
}

private fun formatHour(hour: Int): String {
    return when {
        hour == 0 -> "12 AM"
        hour < 12 -> "$hour AM"
        hour == 12 -> "12 PM"
        else -> "${hour - 12} PM"
    }
}

private fun cToF(c: Double) = c * 9 / 5 + 32
