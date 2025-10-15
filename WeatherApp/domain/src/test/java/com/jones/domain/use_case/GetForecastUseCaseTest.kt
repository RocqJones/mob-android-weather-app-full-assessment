package com.jones.domain.use_case

import com.jones.data.local.entity.ForecastEntity
import com.jones.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetForecastUseCase
 */
class GetForecastUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetForecastUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetForecastUseCase(repository)
    }

    @Test
    fun `invoke triggers fetch and returns forecast from db`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val count = 7
        val mockForecast = listOf(
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00"
            ),
            ForecastEntity(
                id = 2,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697616000L,
                temperature = 295.15,
                weatherMain = "Clouds",
                weatherDescription = "few clouds",
                weatherIcon = "02d",
                dateText = "2025-10-18 12:00:00"
            )
        )

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(mockForecast)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey, count).first()

        // Then
        assertEquals(2, result.size)
        assertEquals(mockForecast, result)
        coVerify { repository.fetchForecast(latitude, longitude, apiKey, count) }
        coVerify { repository.getForecastFromDb() }
    }

    @Test
    fun `invoke returns empty list when no forecast data in db`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val count = 7

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(emptyList())

        // When
        val result = useCase.invoke(latitude, longitude, apiKey, count).first()

        // Then
        assertTrue(result.isEmpty())
        coVerify { repository.fetchForecast(latitude, longitude, apiKey, count) }
    }

    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        // Given
        val latitude = 40.7128
        val longitude = -74.0060
        val apiKey = "different_api_key"
        val count = 5

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(emptyList())

        // When
        useCase.invoke(latitude, longitude, apiKey, count)

        // Then
        coVerify(exactly = 1) {
            repository.fetchForecast(latitude, longitude, apiKey, count)
        }
        coVerify(exactly = 1) {
            repository.getForecastFromDb()
        }
    }

    @Test
    fun `invoke handles different count values`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val count = 10

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(emptyList())

        // When
        useCase.invoke(latitude, longitude, apiKey, count)

        // Then
        coVerify { repository.fetchForecast(latitude, longitude, apiKey, count) }
    }

    @Test
    fun `invoke returns forecast with different weather types`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val count = 7
        val mixedWeatherForecast = listOf(
            ForecastEntity(
                id = 1,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697529600L,
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-17 12:00:00"
            ),
            ForecastEntity(
                id = 2,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697616000L,
                temperature = 290.15,
                weatherMain = "Rain",
                weatherDescription = "moderate rain",
                weatherIcon = "10d",
                dateText = "2025-10-18 12:00:00"
            ),
            ForecastEntity(
                id = 3,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697702400L,
                temperature = 293.15,
                weatherMain = "Snow",
                weatherDescription = "light snow",
                weatherIcon = "13d",
                dateText = "2025-10-19 12:00:00"
            )
        )

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(mixedWeatherForecast)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey, count).first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Clear", result[0].weatherMain)
        assertEquals("Rain", result[1].weatherMain)
        assertEquals("Snow", result[2].weatherMain)
    }

    @Test
    fun `invoke maintains forecast order from repository`() = runTest {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219
        val apiKey = "test_api_key"
        val count = 7
        val orderedForecast = (1..5).map { index ->
            ForecastEntity(
                id = index,
                cityName = "Nairobi",
                latitude = latitude,
                longitude = longitude,
                timestamp = 1697529600L + (index * 86400L),
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d",
                dateText = "2025-10-${17 + index} 12:00:00"
            )
        }

        coEvery { repository.fetchForecast(latitude, longitude, apiKey, count) } returns Unit
        coEvery { repository.getForecastFromDb() } returns flowOf(orderedForecast)

        // When
        val result = useCase.invoke(latitude, longitude, apiKey, count).first()

        // Then
        assertEquals(5, result.size)
        result.forEachIndexed { index, forecast ->
            assertEquals(index + 1, forecast.id)
        }
    }
}

