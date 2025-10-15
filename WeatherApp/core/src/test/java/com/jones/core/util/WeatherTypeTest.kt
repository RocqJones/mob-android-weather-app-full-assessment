package com.jones.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for WeatherType enum
 */
class WeatherTypeTest {

    @Test
    fun `fromWeatherMain returns CLEAR for Clear string`() {
        // Given
        val weatherMain = "Clear"

        // When
        val result = WeatherType.fromWeatherMain(weatherMain)

        // Then
        assertEquals(WeatherType.CLEAR, result)
    }

    @Test
    fun `fromWeatherMain returns CLOUDS for Clouds string`() {
        // Given
        val weatherMain = "Clouds"

        // When
        val result = WeatherType.fromWeatherMain(weatherMain)

        // Then
        assertEquals(WeatherType.CLOUDS, result)
    }

    @Test
    fun `fromWeatherMain returns RAIN for Rain string`() {
        // Given
        val weatherMain = "Rain"

        // When
        val result = WeatherType.fromWeatherMain(weatherMain)

        // Then
        assertEquals(WeatherType.RAIN, result)
    }

    @Test
    fun `fromWeatherMain returns SNOW for Snow string`() {
        // Given
        val weatherMain = "Snow"

        // When
        val result = WeatherType.fromWeatherMain(weatherMain)

        // Then
        assertEquals(WeatherType.SNOW, result)
    }

    @Test
    fun `fromWeatherMain is case insensitive`() {
        // Given
        val lowerCase = "clear"
        val upperCase = "CLEAR"
        val mixedCase = "ClEaR"

        // When
        val result1 = WeatherType.fromWeatherMain(lowerCase)
        val result2 = WeatherType.fromWeatherMain(upperCase)
        val result3 = WeatherType.fromWeatherMain(mixedCase)

        // Then
        assertEquals(WeatherType.CLEAR, result1)
        assertEquals(WeatherType.CLEAR, result2)
        assertEquals(WeatherType.CLEAR, result3)
    }

    @Test
    fun `fromWeatherMain returns CLOUDS as default for unknown weather`() {
        // Given
        val unknownWeather = "Unknown"

        // When
        val result = WeatherType.fromWeatherMain(unknownWeather)

        // Then
        assertEquals(WeatherType.CLOUDS, result)
    }

    @Test
    fun `fromWeatherMain returns CLOUDS for null input`() {
        // Given
        val nullWeather: String? = null

        // When
        val result = WeatherType.fromWeatherMain(nullWeather)

        // Then
        assertEquals(WeatherType.CLOUDS, result)
    }

    @Test
    fun `fromWeatherMain returns CLOUDS for empty string`() {
        // Given
        val emptyWeather = ""

        // When
        val result = WeatherType.fromWeatherMain(emptyWeather)

        // Then
        assertEquals(WeatherType.CLOUDS, result)
    }

    @Test
    fun `getDrawableNameForWeather returns correct drawable name for Clear`() {
        // Given
        val weatherMain = "Clear"

        // When
        val drawableName = WeatherType.getDrawableNameForWeather(weatherMain)

        // Then
        assertEquals("sunny", drawableName)
    }

    @Test
    fun `getDrawableNameForWeather returns correct drawable name for Clouds`() {
        // Given
        val weatherMain = "Clouds"

        // When
        val drawableName = WeatherType.getDrawableNameForWeather(weatherMain)

        // Then
        assertEquals("cloudy", drawableName)
    }

    @Test
    fun `getDrawableNameForWeather returns correct drawable name for Rain`() {
        // Given
        val weatherMain = "Rain"

        // When
        val drawableName = WeatherType.getDrawableNameForWeather(weatherMain)

        // Then
        assertEquals("rainy", drawableName)
    }

    @Test
    fun `getDrawableNameForWeather returns correct drawable name for Snow`() {
        // Given
        val weatherMain = "Snow"

        // When
        val drawableName = WeatherType.getDrawableNameForWeather(weatherMain)

        // Then
        assertEquals("forest", drawableName)
    }

    @Test
    fun `enum values have correct API values`() {
        assertEquals("Clear", WeatherType.CLEAR.apiValue)
        assertEquals("Clouds", WeatherType.CLOUDS.apiValue)
        assertEquals("Rain", WeatherType.RAIN.apiValue)
        assertEquals("Snow", WeatherType.SNOW.apiValue)
    }

    @Test
    fun `enum values have correct drawable names`() {
        assertEquals("sunny", WeatherType.CLEAR.drawableName)
        assertEquals("cloudy", WeatherType.CLOUDS.drawableName)
        assertEquals("rainy", WeatherType.RAIN.drawableName)
        assertEquals("forest", WeatherType.SNOW.drawableName)
    }
}

