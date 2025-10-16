package com.jones.ui.state

import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for WeatherUiState
 */
class WeatherUiStateTest {

    @Test
    fun `Loading state is singleton object`() {
        // Given & When
        val loading1 = WeatherUiState.Loading
        val loading2 = WeatherUiState.Loading

        // Then
        assertEquals(loading1, loading2)
        assertTrue(loading1 === loading2)
    }

    @Test
    fun `Success state holds weather data and online status`() {
        // Given
        val weather = CurrentWeather(
            id = 0,
            cityName = "Nairobi",
            latitude = -1.286389,
            longitude = 36.817223,
            temperature = 298.15,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timestamp = System.currentTimeMillis()
        )
        val forecast = listOf(
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d"
            )
        )

        // When
        val state = WeatherUiState.Success(weather, forecast, isOnline = true)

        // Then
        assertEquals(weather, state.currentWeather)
        assertEquals(forecast, state.forecast)
        assertTrue(state.isOnline)
    }

    @Test
    fun `Success state can have null weather data`() {
        // Given & When
        val state = WeatherUiState.Success(
            currentWeather = null,
            forecast = null,
            isOnline = true
        )

        // Then
        assertNull(state.currentWeather)
        assertNull(state.forecast)
        assertTrue(state.isOnline)
    }

    @Test
    fun `Error state holds error message and online status`() {
        // Given
        val errorMessage = "Network error occurred"

        // When
        val state = WeatherUiState.Error(errorMessage, isOnline = false)

        // Then
        assertEquals(errorMessage, state.message)
        assertFalse(state.isOnline)
    }

    @Test
    fun `Offline state holds cached weather data`() {
        // Given
        val weather = CurrentWeather(
            id = 0,
            cityName = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            temperature = 285.15,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timestamp = System.currentTimeMillis()
        )
        val forecast = emptyList<Forecast>()

        // When
        val state = WeatherUiState.Offline(weather, forecast)

        // Then
        assertEquals(weather, state.currentWeather)
        assertEquals(forecast, state.forecast)
    }

    @Test
    fun `Success states with different data are not equal`() {
        // Given
        val weather1 = CurrentWeather(
            id = 0, cityName = "City1", latitude = 0.0, longitude = 0.0,
            temperature = 298.0, weatherMain = "Clear", weatherDescription = "clear",
            weatherIcon = "01d", timestamp = 0L
        )
        val weather2 = CurrentWeather(
            id = 0, cityName = "City2", latitude = 0.0, longitude = 0.0,
            temperature = 290.0, weatherMain = "Rain", weatherDescription = "rain",
            weatherIcon = "10d", timestamp = 0L
        )

        // When
        val state1 = WeatherUiState.Success(weather1, null, true)
        val state2 = WeatherUiState.Success(weather2, null, true)

        // Then
        assertNotEquals(state1, state2)
    }

    @Test
    fun `Error states with different messages are not equal`() {
        // Given & When
        val error1 = WeatherUiState.Error("Error 1", true)
        val error2 = WeatherUiState.Error("Error 2", true)

        // Then
        assertNotEquals(error1, error2)
    }

    @Test
    fun `Success state with offline status`() {
        // Given
        val weather = CurrentWeather(
            id = 0, cityName = "Tokyo", latitude = 35.6762, longitude = 139.6503,
            temperature = 295.15, weatherMain = "Clouds", weatherDescription = "clouds",
            weatherIcon = "02d", timestamp = System.currentTimeMillis()
        )

        // When
        val state = WeatherUiState.Success(weather, null, isOnline = false)

        // Then
        assertFalse(state.isOnline)
        assertEquals(weather, state.currentWeather)
    }

    @Test
    fun `Offline state can have null weather data`() {
        // Given & When
        val state = WeatherUiState.Offline(null, null)

        // Then
        assertNull(state.currentWeather)
        assertNull(state.forecast)
    }

    @Test
    fun `different state types are not equal`() {
        // Given
        val loading = WeatherUiState.Loading
        val success = WeatherUiState.Success(null, null, true)
        val error = WeatherUiState.Error("error", true)
        val offline = WeatherUiState.Offline(null, null)

        // Then
        assertNotEquals(loading, success)
        assertNotEquals(loading, error)
        assertNotEquals(loading, offline)
        assertNotEquals(success, error)
        assertNotEquals(success, offline)
        assertNotEquals(error, offline)
    }
}

