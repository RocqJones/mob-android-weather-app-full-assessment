package com.jones.core.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts temperature from Kelvin to Celsius.
 * @param kelvin Temperature in Kelvin
 * @return Temperature in Celsius (rounded to nearest integer)
 */
fun kelvinToCelsius(kelvin: Double): Int = (kelvin - 273.15).toInt()

/**
 * Converts temperature from Kelvin to Fahrenheit.
 * @param kelvin Temperature in Kelvin
 * @return Temperature in Fahrenheit (rounded to nearest integer)
 */
fun kelvinToFahrenheit(kelvin: Double): Int = ((kelvin - 273.15) * 9 / 5 + 32).toInt()

/**
 * Formats timestamp to readable date-time string.
 * @param timestamp Unix timestamp in seconds
 * @param pattern Date format pattern (default: "MMM dd, yyyy HH:mm")
 * @return Formatted date-time string
 */
fun formatTimestamp(timestamp: Long, pattern: String = "MMM dd, yyyy HH:mm"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}

/**
 * Formats a Unix timestamp to a full date-time string with day of week.
 * @param timestamp Unix timestamp in seconds
 * @return Formatted full date-time string
 */
fun formatFullTimestamp(timestamp: Long): String {
    return formatTimestamp(timestamp, "EEEE, MMM dd, yyyy - HH:mm a")
}

/**
 * Converts from "yyyy-MM-dd HH:mm:ss" format to "EEEE, MMM dd, yyyy" format.
 * @param dateText Date string in format "yyyy-MM-dd HH:mm:ss"
 * @return Formatted date string with day of week, or original string if parsing fails
 */
fun formatForecastDate(dateText: String?): String {
    if (dateText.isNullOrBlank()) return "Unknown date"

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, MMM dd, yyyy - HH:mm a", Locale.getDefault())
        val date = inputFormat.parse(dateText)
        date?.let { outputFormat.format(it) } ?: dateText
    } catch (e: Exception) {
        dateText
    }
}
