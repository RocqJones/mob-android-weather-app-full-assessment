package com.jones.ui.util

import com.jones.ui.R
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for weather utility functions
 */
class WeatherUtilTest {
    @Test
    fun `getWeatherBackground returns correct background for Clear weather`() {
        // Given & When
        val background = getWeatherBackground("Clear")

        // Then
        assertEquals(R.drawable.sunny, background)
    }

    @Test
    fun `getWeatherBackground returns correct background for Clouds weather`() {
        // Given & When
        val background = getWeatherBackground("Clouds")

        // Then
        assertEquals(R.drawable.cloudy, background)
    }

    @Test
    fun `getWeatherBackground returns correct background for Rain weather`() {
        // Given & When
        val background = getWeatherBackground("Rain")

        // Then
        assertEquals(R.drawable.rainy, background)
    }

    @Test
    fun `getWeatherBackground returns correct background for Snow weather`() {
        // Given & When
        val background = getWeatherBackground("Snow")

        // Then
        assertEquals(R.drawable.forest, background)
    }

    @Test
    fun `getWeatherBackground returns default background for null weather`() {
        // Given & When
        val background = getWeatherBackground(null)

        // Then
        assertEquals(R.drawable.cloudy, background)
    }

    @Test
    fun `getWeatherBackground returns default background for unknown weather`() {
        // Given & When
        val background = getWeatherBackground("Unknown")

        // Then
        assertEquals(R.drawable.cloudy, background)
    }

    @Test
    fun `getWeatherBackground handles case sensitivity`() {
        // Given & When
        val backgroundLower = getWeatherBackground("clear")
        val backgroundUpper = getWeatherBackground("CLEAR")
        val backgroundMixed = getWeatherBackground("ClEaR")

        // Then
        assertEquals(R.drawable.sunny, backgroundLower)
        assertEquals(R.drawable.sunny, backgroundUpper)
        assertEquals(R.drawable.sunny, backgroundMixed)
    }

    @Test
    fun `getWeatherIcon returns correct icon for Clear weather`() {
        // Given & When
        val icon = getWeatherIcon("Clear")

        // Then
        assertEquals(R.mipmap.sun_light, icon)
    }

    @Test
    fun `getWeatherIcon returns correct icon for Clouds weather`() {
        // Given & When
        val icon = getWeatherIcon("Clouds")

        // Then
        assertEquals(R.mipmap.cloudy_light, icon)
    }

    @Test
    fun `getWeatherIcon returns correct icon for Rain weather`() {
        // Given & When
        val icon = getWeatherIcon("Rain")

        // Then
        assertEquals(R.mipmap.rain_light, icon)
    }

    @Test
    fun `getWeatherIcon returns correct icon for Snow weather`() {
        // Given & When
        val icon = getWeatherIcon("Snow")

        // Then
        assertEquals(R.mipmap.heavy_snowfall, icon)
    }

    @Test
    fun `getWeatherIcon returns default icon for null weather`() {
        // Given & When
        val icon = getWeatherIcon(null)

        // Then
        assertEquals(R.mipmap.cloudy_light, icon)
    }

    @Test
    fun `getWeatherIcon returns default icon for unknown weather`() {
        // Given & When
        val icon = getWeatherIcon("Unknown")

        // Then
        assertEquals(R.mipmap.cloudy_light, icon)
    }

    @Test
    fun `getWeatherIcon handles case sensitivity`() {
        // Given & When
        val iconLower = getWeatherIcon("rain")
        val iconUpper = getWeatherIcon("RAIN")
        val iconMixed = getWeatherIcon("RaIn")

        // Then
        assertEquals(R.mipmap.rain_light, iconLower)
        assertEquals(R.mipmap.rain_light, iconUpper)
        assertEquals(R.mipmap.rain_light, iconMixed)
    }

    @Test
    fun `all weather types have corresponding backgrounds`() {
        // Given
        val weatherTypes = listOf("Clear", "Clouds", "Rain", "Snow")

        // When & Then
        weatherTypes.forEach { weatherType ->
            val background = getWeatherBackground(weatherType)
            // Should not throw exception and return a valid resource ID
            assert(background != 0)
        }
    }

    @Test
    fun `all weather types have corresponding icons`() {
        // Given
        val weatherTypes = listOf("Clear", "Clouds", "Rain", "Snow")

        // When & Then
        weatherTypes.forEach { weatherType ->
            val icon = getWeatherIcon(weatherType)
            // Should not throw exception and return a valid resource ID
            assert(icon != 0)
        }
    }
}
