package com.jones.data.local.entity

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for ForecastEntity
 */
class ForecastEntityTest {
    @Test
    fun `ForecastEntity creates instance with all fields`() {
        // Given
        val entity =
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )

        // Then
        assertEquals(1, entity.id)
        assertEquals("Nairobi", entity.cityName)
        assertEquals(-1.2921, entity.latitude!!, 0.0001)
        assertEquals(36.8219, entity.longitude!!, 0.0001)
        assertEquals(1697529600L, entity.timestamp)
        assertEquals(298.15, entity.temperature!!, 0.0001)
        assertEquals("Clear", entity.weatherMain)
        assertEquals("clear sky", entity.weatherDescription)
        assertEquals("01d", entity.weatherIcon)
        assertEquals("2025-10-17 12:00:00", entity.dateText)
    }

    @Test
    fun `ForecastEntity handles nullable fields`() {
        // Given
        val entity =
            ForecastEntity(
                id = 0,
                cityName = null,
                latitude = null,
                longitude = null,
                timestamp = null,
                temperature = null,
                weatherMain = null,
                weatherDescription = null,
                weatherIcon = null,
                dateText = null,
            )

        // Then
        assertNull(entity.cityName)
        assertNull(entity.latitude)
        assertNull(entity.longitude)
        assertNull(entity.timestamp)
        assertNull(entity.temperature)
        assertNull(entity.weatherMain)
        assertNull(entity.weatherDescription)
        assertNull(entity.weatherIcon)
        assertNull(entity.dateText)
    }

    @Test
    fun `ForecastEntity equality works correctly`() {
        // Given
        val entity1 =
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )
        val entity2 =
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )

        // Then
        assertEquals(entity1, entity2)
    }

    @Test
    fun `ForecastEntity with different values are not equal`() {
        // Given
        val entity1 =
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )
        val entity2 =
            ForecastEntity(
                id = 2,
                cityName = "London",
                latitude = 51.5074,
                longitude = -0.1278,
                timestamp = 1697616000L,
                temperature = 285.15,
                weatherMain = "Rain",
                weatherDescription = "light rain",
                weatherIcon = "10d",
                dateText = "2025-10-18 12:00:00",
            )

        // Then
        assertNotEquals(entity1, entity2)
    }

    @Test
    fun `ForecastEntity copy works correctly`() {
        // Given
        val original =
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = -1.2921,
                longitude = 36.8219,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )

        // When
        val copy = original.copy(weatherMain = "Rain", temperature = 290.15)

        // Then
        assertEquals(original.cityName, copy.cityName)
        assertEquals("Rain", copy.weatherMain)
        assertEquals(290.15, copy.temperature!!, 0.0001)
    }

    @Test
    fun `ForecastEntity handles different weather conditions`() {
        // Create forecast entities for different weather conditions
        val clear =
            ForecastEntity(
                id = 1, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697529600L, temperature = 298.15, weatherMain = "Clear",
                weatherDescription = "clear sky", weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )
        val rain =
            ForecastEntity(
                id = 2, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697616000L, temperature = 290.15, weatherMain = "Rain",
                weatherDescription = "moderate rain", weatherIcon = "10d",
                dateText = "2025-10-18 12:00:00",
            )
        val snow =
            ForecastEntity(
                id = 3, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697702400L, temperature = 270.15, weatherMain = "Snow",
                weatherDescription = "light snow", weatherIcon = "13d",
                dateText = "2025-10-19 12:00:00",
            )
        val clouds =
            ForecastEntity(
                id = 4, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697788800L, temperature = 295.15, weatherMain = "Clouds",
                weatherDescription = "few clouds", weatherIcon = "02d",
                dateText = "2025-10-20 12:00:00",
            )

        // Then
        assertEquals("Clear", clear.weatherMain)
        assertEquals("Rain", rain.weatherMain)
        assertEquals("Snow", snow.weatherMain)
        assertEquals("Clouds", clouds.weatherMain)
    }

    @Test
    fun `ForecastEntity maintains chronological order by id`() {
        // Given
        val forecast1 =
            ForecastEntity(
                id = 1, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697529600L, temperature = 298.15, weatherMain = "Clear",
                weatherDescription = "clear sky", weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )
        val forecast2 =
            ForecastEntity(
                id = 2, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697616000L, temperature = 298.15, weatherMain = "Clear",
                weatherDescription = "clear sky", weatherIcon = "01d",
                dateText = "2025-10-18 12:00:00",
            )
        val forecast3 =
            ForecastEntity(
                id = 3, cityName = "City", latitude = 0.0, longitude = 0.0,
                timestamp = 1697702400L, temperature = 298.15, weatherMain = "Clear",
                weatherDescription = "clear sky", weatherIcon = "01d",
                dateText = "2025-10-19 12:00:00",
            )

        // Then
        assertEquals(1, forecast1.id)
        assertEquals(2, forecast2.id)
        assertEquals(3, forecast3.id)
    }

    @Test
    fun `ForecastEntity dateText format is preserved`() {
        // Given
        val dateText = "2025-10-17 18:00:00"
        val entity =
            ForecastEntity(
                id = 1,
                cityName = "City",
                latitude = 0.0,
                longitude = 0.0,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = dateText,
            )

        // Then
        assertEquals(dateText, entity.dateText)
    }

    @Test
    fun `ForecastEntity supports multiple cities`() {
        // Given
        val nairobiForecast =
            ForecastEntity(
                id = 1, cityName = "Nairobi", latitude = -1.2921, longitude = 36.8219,
                timestamp = 1697529600L, temperature = 298.15, weatherMain = "Clear",
                weatherDescription = "clear sky", weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00",
            )
        val londonForecast =
            ForecastEntity(
                id = 2, cityName = "London", latitude = 51.5074, longitude = -0.1278,
                timestamp = 1697529600L, temperature = 285.15, weatherMain = "Rain",
                weatherDescription = "light rain", weatherIcon = "10d",
                dateText = "2025-10-17 12:00:00",
            )

        // Then
        assertEquals("Nairobi", nairobiForecast.cityName)
        assertEquals("London", londonForecast.cityName)
        assertNotEquals(nairobiForecast.latitude, londonForecast.latitude)
    }
}
