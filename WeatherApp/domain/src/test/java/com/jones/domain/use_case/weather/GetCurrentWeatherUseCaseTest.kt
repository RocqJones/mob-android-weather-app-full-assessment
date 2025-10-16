package com.jones.domain.use_case.weather

import com.jones.domain.model.CurrentWeather
import com.jones.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetCurrentWeatherUseCase
 */
class GetCurrentWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetCurrentWeatherUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCurrentWeatherUseCase(repository)
    }

    @Test
    fun `invoke triggers fetch and returns current weather from db`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val mockWeather = CurrentWeather(
            id = 0,
            cityName = "Nairobi",
            latitude = latitude,
            longitude = longitude,
            temperature = 298.15,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timestamp = 1697529600L
        )

        coEvery { repository.fetchCurrentWeather(latitude, longitude, apiKey) } returns Unit
        coEvery { repository.getCurrentWeatherFromDb(0) } returns flowOf(mockWeather)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey).first()

        // Then
        assertEquals(mockWeather, result)
        coVerify { repository.fetchCurrentWeather(latitude, longitude, apiKey) }
        coVerify { repository.getCurrentWeatherFromDb(0) }
    }

    @Test
    fun `invoke returns null when no weather data in db`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"

        coEvery { repository.fetchCurrentWeather(latitude, longitude, apiKey) } returns Unit
        coEvery { repository.getCurrentWeatherFromDb(0) } returns flowOf(null)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey).first()

        // Then
        assertNull(result)
        coVerify { repository.fetchCurrentWeather(latitude, longitude, apiKey) }
    }

    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        // Given
        val latitude = 40.7128
        val longitude = -74.0060
        val apiKey = "different_api_key"

        coEvery { repository.fetchCurrentWeather(latitude, longitude, apiKey) } returns Unit
        coEvery { repository.getCurrentWeatherFromDb(0) } returns flowOf(null)

        // When
        useCase.invoke(latitude, longitude, apiKey)

        // Then
        coVerify(exactly = 1) {
            repository.fetchCurrentWeather(latitude, longitude, apiKey)
        }
        coVerify(exactly = 1) {
            repository.getCurrentWeatherFromDb(0)
        }
    }

    @Test
    fun `invoke handles different weather conditions`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val rainyWeather = CurrentWeather(
            id = 0,
            cityName = "London",
            latitude = latitude,
            longitude = longitude,
            temperature = 285.15,
            weatherMain = "Rain",
            weatherDescription = "light rain",
            weatherIcon = "10d",
            timestamp = 1697529600L
        )

        coEvery { repository.fetchCurrentWeather(latitude, longitude, apiKey) } returns Unit
        coEvery { repository.getCurrentWeatherFromDb(0) } returns flowOf(rainyWeather)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey).first()

        // Then
        assertEquals("Rain", result?.weatherMain)
        assertEquals("light rain", result?.weatherDescription)
    }

    @Test
    fun `invoke works with different coordinates`() = runTest {
        // Given
        val latitude = 51.5074
        val longitude = -0.1278
        val apiKey = "test_api_key"

        coEvery { repository.fetchCurrentWeather(latitude, longitude, apiKey) } returns Unit
        coEvery { repository.getCurrentWeatherFromDb(0) } returns flowOf(null)

        // When
        useCase.invoke(latitude, longitude, apiKey)

        // Then
        coVerify { repository.fetchCurrentWeather(latitude, longitude, apiKey) }
    }
}
