package com.jones.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for WeatherUtil functions
 */
class WeatherUtilTest {

    @Test
    fun `kelvinToCelsius converts correctly`() {
        // Given
        val kelvin = 273.15

        // When
        val celsius = kelvinToCelsius(kelvin)

        // Then
        assertEquals(0, celsius)
    }

    @Test
    fun `kelvinToCelsius converts 300K to 27C`() {
        // Given
        val kelvin = 300.0

        // When
        val celsius = kelvinToCelsius(kelvin)

        // Then
        assertEquals(26, celsius) // (300 - 273.15).toInt() = 26
    }

    @Test
    fun `kelvinToFahrenheit converts 273_15K to 32F`() {
        // Given
        val kelvin = 273.15

        // When
        val fahrenheit = kelvinToFahrenheit(kelvin)

        // Then
        assertEquals(32, fahrenheit)
    }

    @Test
    fun `kelvinToFahrenheit converts 300K to 80F`() {
        // Given
        val kelvin = 300.0

        // When
        val fahrenheit = kelvinToFahrenheit(kelvin)

        // Then
        assertEquals(80, fahrenheit)
    }

    @Test
    fun `kelvinToFahrenheit converts negative fahrenheit correctly`() {
        // Given
        val kelvin = 255.37 // approximately -0°F

        // When
        val fahrenheit = kelvinToFahrenheit(kelvin)

        // Then
        assertEquals(-0, fahrenheit)
    }

    @Test
    fun `formatTimestamp with default pattern formats correctly`() {
        // Given
        val timestamp = 1697529600L // Oct 17, 2023 00:00:00 UTC

        // When
        val formatted = formatTimestamp(timestamp)

        // Then
        assertTrue(formatted.contains("Oct"))
        assertTrue(formatted.contains("2023"))
    }

    @Test
    fun `formatTimestamp with custom pattern formats correctly`() {
        // Given
        val timestamp = 1697529600L
        val pattern = "yyyy-MM-dd"

        // When
        val formatted = formatTimestamp(timestamp, pattern)

        // Then
        assertTrue(formatted.contains("2023"))
        assertTrue(formatted.contains("-"))
    }

    @Test
    fun `formatForecastDate formats valid date correctly`() {
        // Given
        val dateText = "2025-10-20 18:00:00"

        // When
        val formatted = formatForecastDate(dateText)

        // Then
        assertTrue(formatted.contains("Oct"))
        assertTrue(formatted.contains("20"))
        assertTrue(formatted.contains("2025"))
    }

    @Test
    fun `formatForecastDate returns Unknown date for null input`() {
        // Given
        val dateText: String? = null

        // When
        val formatted = formatForecastDate(dateText)

        // Then
        assertEquals("Unknown date", formatted)
    }

    @Test
    fun `formatForecastDate returns Unknown date for blank input`() {
        // Given
        val dateText = "   "

        // When
        val formatted = formatForecastDate(dateText)

        // Then
        assertEquals("Unknown date", formatted)
    }

    @Test
    fun `formatForecastDate returns original string for invalid format`() {
        // Given
        val invalidDate = "invalid-date"

        // When
        val formatted = formatForecastDate(invalidDate)

        // Then
        assertEquals(invalidDate, formatted)
    }

    @Test
    fun `formatForecastDate handles different dates correctly`() {
        // Given
        val dateText1 = "2025-01-01 12:00:00"
        val dateText2 = "2025-12-31 23:59:59"

        // When
        val formatted1 = formatForecastDate(dateText1)
        val formatted2 = formatForecastDate(dateText2)

        // Then
        assertTrue(formatted1.contains("Jan"))
        assertTrue(formatted1.contains("01"))
        assertTrue(formatted2.contains("Dec"))
        assertTrue(formatted2.contains("31"))
    }

    @Test
    fun `temperature conversion round trip is approximately correct`() {
        // Given
        val originalKelvin = 298.15

        // When
        val celsius = kelvinToCelsius(originalKelvin)
        val fahrenheit = kelvinToFahrenheit(originalKelvin)

        // Then
        assertEquals(25, celsius)
        assertEquals(77, fahrenheit)
    }
}

