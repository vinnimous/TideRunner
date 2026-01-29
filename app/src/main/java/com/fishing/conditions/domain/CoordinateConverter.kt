package com.fishing.conditions.domain

import kotlin.math.abs

object CoordinateConverter {

    fun toDegreesMinutesSeconds(decimal: Double, isLatitude: Boolean): String {
        val degrees = decimal.toInt()
        val minutesDecimal = abs(decimal - degrees) * 60
        val minutes = minutesDecimal.toInt()
        val seconds = (minutesDecimal - minutes) * 60

        val direction = when {
            isLatitude -> if (decimal >= 0) "N" else "S"
            else -> if (decimal >= 0) "E" else "W"
        }

        return "${abs(degrees)}° ${minutes}' ${"%.2f".format(seconds)}\" $direction"
    }

    fun toDecimalDegrees(degrees: Int, minutes: Int, seconds: Double, direction: String): Double {
        val decimal = abs(degrees) + (minutes / 60.0) + (seconds / 3600.0)
        return if (direction in listOf("S", "W")) -decimal else decimal
    }
}
