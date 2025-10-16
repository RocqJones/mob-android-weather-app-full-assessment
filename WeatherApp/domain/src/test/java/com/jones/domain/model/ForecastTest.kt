package com.jones.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for Forecast domain model
 */
class ForecastTest {
    @Test
    fun `Forecast creates instance with all fields`() {
        // Given & When
        val forecast =
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        // Then
        assertEquals(1, forecast.id)
        assertEquals("2025-10-17 12:00:00", forecast.dateText)
        assertEquals(298.15, forecast.temperature!!, 0.01)
        assertEquals("Clear", forecast.weatherMain)
        assertEquals("clear sky", forecast.weatherDescription)
        assertEquals("01d", forecast.weatherIcon)
    }

    @Test
    fun `Forecast handles nullable fields`() {
        // Given & When
        val forecast =
            Forecast(
                id = 0,
                dateText = null,
                temperature = null,
                weatherMain = null,
                weatherDescription = null,
                weatherIcon = null,
            )

        // Then
        assertEquals(0, forecast.id)
        assertEquals(null, forecast.dateText)
        assertEquals(null, forecast.temperature)
        assertEquals(null, forecast.weatherMain)
        assertEquals(null, forecast.weatherDescription)
        assertEquals(null, forecast.weatherIcon)
    }

    @Test
    fun `Forecast equality works correctly`() {
        // Given
        val forecast1 =
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        val forecast2 =
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        // Then
        assertEquals(forecast1, forecast2)
        assertEquals(forecast1.hashCode(), forecast2.hashCode())
    }

    @Test
    fun `Forecast with different values are not equal`() {
        // Given
        val forecast1 =
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        val forecast2 =
            Forecast(
                id = 2,
                dateText = "2025-10-18 12:00:00",
                temperature = 295.15,
                weatherMain = "Clouds",
                weatherDescription = "few clouds",
                weatherIcon = "02d",
            )

        // Then
        assertNotEquals(forecast1, forecast2)
    }

    @Test
    fun `Forecast copy works correctly`() {
        // Given
        val original =
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        // When
        val copy = original.copy(temperature = 300.0)

        // Then
        assertEquals(300.0, copy.temperature!!, 0.01)
        assertEquals(original.id, copy.id)
        assertEquals(original.dateText, copy.dateText)
        assertEquals(original.weatherMain, copy.weatherMain)
    }

    @Test
    fun `Forecast handles different weather types`() {
        // Given & When
        val forecasts =
            listOf(
                Forecast(
                    id = 1,
                    dateText = "2025-10-17",
                    temperature = 298.0,
                    weatherMain = "Clear",
                    weatherDescription = "clear sky",
                    weatherIcon = "01d",
                ),
                Forecast(
                    id = 2,
                    dateText = "2025-10-18",
                    temperature = 290.0,
                    weatherMain = "Rain",
                    weatherDescription = "moderate rain",
                    weatherIcon = "10d",
                ),
                Forecast(
                    id = 3,
                    dateText = "2025-10-19",
                    temperature = 270.0,
                    weatherMain = "Snow",
                    weatherDescription = "light snow",
                    weatherIcon = "13d",
                ),
                Forecast(
                    id = 4,
                    dateText = "2025-10-20",
                    temperature = 295.0,
                    weatherMain = "Clouds",
                    weatherDescription = "scattered clouds",
                    weatherIcon = "03d",
                ),
            )

        // Then
        assertEquals(4, forecasts.size)
        assertEquals("Clear", forecasts[0].weatherMain)
        assertEquals("Rain", forecasts[1].weatherMain)
        assertEquals("Snow", forecasts[2].weatherMain)
        assertEquals("Clouds", forecasts[3].weatherMain)
    }

    @Test
    fun `Forecast with default id value`() {
        // Given & When
        val forecast =
            Forecast(
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
            )

        // Then
        assertEquals(0, forecast.id)
    }
}
