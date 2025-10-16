package com.jones.data.local.entity

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for CurrentWeatherEntity
 */
class CurrentWeatherEntityTest {
    @Test
    fun `CurrentWeatherEntity creates instance with all fields`() {
        // Given
        val entity =
            CurrentWeatherEntity(
                id = 0,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                timestamp = 1697529600L,
            )

        // Then
        assertEquals(0, entity.id)
        assertEquals("Nairobi", entity.cityName)
        assertEquals(-1.2921, entity.latitude!!, 0.0001)
        assertEquals(36.8219, entity.longitude!!, 0.0001)
        assertEquals(298.15, entity.temperature!!, 0.0001)
        assertEquals("Clear", entity.weatherMain)
        assertEquals("clear sky", entity.weatherDescription)
        assertEquals("01d", entity.weatherIcon)
        assertEquals(1697529600L, entity.timestamp)
    }

    @Test
    fun `CurrentWeatherEntity handles nullable fields`() {
        // Given
        val entity =
            CurrentWeatherEntity(
                id = 0,
                cityName = null,
                latitude = null,
                longitude = null,
                temperature = null,
                weatherMain = null,
                weatherDescription = null,
                weatherIcon = null,
                timestamp = null,
            )

        // Then
        assertNull(entity.cityName)
        assertNull(entity.latitude)
        assertNull(entity.longitude)
        assertNull(entity.temperature)
        assertNull(entity.weatherMain)
        assertNull(entity.weatherDescription)
        assertNull(entity.weatherIcon)
        assertNull(entity.timestamp)
    }

    @Test
    fun `CurrentWeatherEntity equality works correctly`() {
        // Given
        val entity1 =
            CurrentWeatherEntity(
                id = 0,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                timestamp = 1697529600L,
            )
        val entity2 =
            CurrentWeatherEntity(
                id = 0,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                timestamp = 1697529600L,
            )

        // Then
        assertEquals(entity1, entity2)
    }

    @Test
    fun `CurrentWeatherEntity with different values are not equal`() {
        // Given
        val entity1 =
            CurrentWeatherEntity(
                id = 0,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                timestamp = 1697529600L,
            )
        val entity2 =
            CurrentWeatherEntity(
                id = 0,
                cityName = "London",
                latitude = 51.5074,
                longitude = -0.1278,
                temperature = 285.15,
                weatherMain = "Rain",
                weatherDescription = "light rain",
                weatherIcon = "10d",
                timestamp = 1697529600L,
            )

        // Then
        assertNotEquals(entity1, entity2)
    }

    @Test
    fun `CurrentWeatherEntity copy works correctly`() {
        // Given
        val original =
            CurrentWeatherEntity(
                id = 0,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                timestamp = 1697529600L,
            )

        // When
        val copy = original.copy(weatherMain = "Rain", weatherDescription = "light rain")

        // Then
        assertEquals(original.cityName, copy.cityName)
        assertEquals("Rain", copy.weatherMain)
        assertEquals("light rain", copy.weatherDescription)
    }

    @Test
    fun `CurrentWeatherEntity handles different weather conditions`() {
        // Test Clear
        val clear =
            CurrentWeatherEntity(
                id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
                temperature = 298.15, weatherMain = "Clear", weatherDescription = "clear sky",
                weatherIcon = "01d", timestamp = 1697529600L,
            )
        assertEquals("Clear", clear.weatherMain)

        // Test Rain
        val rain =
            CurrentWeatherEntity(
                id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
                temperature = 290.15, weatherMain = "Rain", weatherDescription = "moderate rain",
                weatherIcon = "10d", timestamp = 1697529600L,
            )
        assertEquals("Rain", rain.weatherMain)

        // Test Snow
        val snow =
            CurrentWeatherEntity(
                id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
                temperature = 270.15, weatherMain = "Snow", weatherDescription = "light snow",
                weatherIcon = "13d", timestamp = 1697529600L,
            )
        assertEquals("Snow", snow.weatherMain)

        // Test Clouds
        val clouds =
            CurrentWeatherEntity(
                id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
                temperature = 295.15, weatherMain = "Clouds", weatherDescription = "few clouds",
                weatherIcon = "02d", timestamp = 1697529600L,
            )
        assertEquals("Clouds", clouds.weatherMain)
    }
}
