package com.jones.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for CurrentWeather domain model
 */
class CurrentWeatherTest {

    @Test
    fun `CurrentWeather creates instance with all fields`() {
        // Given & When
        val weather = CurrentWeather(
            id = 0,
            cityName = "Nairobi",
            latitude = -1.286389,
            longitude = 36.817223,
            temperature = 298.15,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timestamp = 1697529600L
        )

        // Then
        assertEquals(0, weather.id)
        assertEquals("Nairobi", weather.cityName)
        assertEquals(-1.286389, weather.latitude!!, 0.0001)
        assertEquals(36.817223, weather.longitude!!, 0.0001)
        assertEquals(298.15, weather.temperature!!, 0.01)
        assertEquals("Clear", weather.weatherMain)
        assertEquals("clear sky", weather.weatherDescription)
        assertEquals("01d", weather.weatherIcon)
        assertEquals(1697529600L, weather.timestamp)
    }

    @Test
    fun `CurrentWeather handles nullable fields`() {
        // Given & When
        val weather = CurrentWeather(
            id = 0,
            cityName = null,
            latitude = null,
            longitude = null,
            temperature = null,
            weatherMain = null,
            weatherDescription = null,
            weatherIcon = null,
            timestamp = null
        )

        // Then
        assertEquals(0, weather.id)
        assertEquals(null, weather.cityName)
        assertEquals(null, weather.latitude)
        assertEquals(null, weather.longitude)
        assertEquals(null, weather.temperature)
        assertEquals(null, weather.weatherMain)
        assertEquals(null, weather.weatherDescription)
        assertEquals(null, weather.weatherIcon)
        assertEquals(null, weather.timestamp)
    }

    @Test
    fun `CurrentWeather equality works correctly`() {
        // Given
        val weather1 = CurrentWeather(
            id = 0,
            cityName = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            temperature = 285.15,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timestamp = 1697529600L
        )

        val weather2 = CurrentWeather(
            id = 0,
            cityName = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            temperature = 285.15,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timestamp = 1697529600L
        )

        // Then
        assertEquals(weather1, weather2)
        assertEquals(weather1.hashCode(), weather2.hashCode())
    }

    @Test
    fun `CurrentWeather with different values are not equal`() {
        // Given
        val weather1 = CurrentWeather(
            id = 0,
            cityName = "Nairobi",
            latitude = -1.286389,
            longitude = 36.817223,
            temperature = 298.15,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timestamp = 1697529600L
        )

        val weather2 = CurrentWeather(
            id = 0,
            cityName = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            temperature = 285.15,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timestamp = 1697529600L
        )

        // Then
        assertNotEquals(weather1, weather2)
    }

    @Test
    fun `CurrentWeather copy works correctly`() {
        // Given
        val original = CurrentWeather(
            id = 0,
            cityName = "Tokyo",
            latitude = 35.6762,
            longitude = 139.6503,
            temperature = 295.15,
            weatherMain = "Clouds",
            weatherDescription = "few clouds",
            weatherIcon = "02d",
            timestamp = 1697529600L
        )

        // When
        val copy = original.copy(cityName = "Osaka")

        // Then
        assertEquals("Osaka", copy.cityName)
        assertEquals(original.latitude!!, copy.latitude!!, 0.0001)
        assertEquals(original.longitude!!, copy.longitude!!, 0.0001)
        assertEquals(original.temperature!!, copy.temperature!!, 0.01)
    }

    @Test
    fun `CurrentWeather handles different weather conditions`() {
        // Given & When
        val clear = CurrentWeather(
            id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
            temperature = 298.0, weatherMain = "Clear", weatherDescription = "clear sky",
            weatherIcon = "01d", timestamp = 0L
        )

        val rain = CurrentWeather(
            id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
            temperature = 290.0, weatherMain = "Rain", weatherDescription = "moderate rain",
            weatherIcon = "10d", timestamp = 0L
        )

        val snow = CurrentWeather(
            id = 0, cityName = "City", latitude = 0.0, longitude = 0.0,
            temperature = 270.0, weatherMain = "Snow", weatherDescription = "light snow",
            weatherIcon = "13d", timestamp = 0L
        )

        // Then
        assertEquals("Clear", clear.weatherMain)
        assertEquals("Rain", rain.weatherMain)
        assertEquals("Snow", snow.weatherMain)
    }
}
