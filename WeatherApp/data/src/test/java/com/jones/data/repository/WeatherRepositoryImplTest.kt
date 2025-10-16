package com.jones.data.repository

import com.jones.core.network.NetworkConnectivityService
import com.jones.data.local.dao.CurrentWeatherDao
import com.jones.data.local.dao.ForecastDao
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.data.remote.api.WeatherApiService
import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for WeatherRepositoryImpl
 * Tests the offline-first approach and network connectivity handling
 */
class WeatherRepositoryImplTest {
    private lateinit var apiService: WeatherApiService
    private lateinit var currentWeatherDao: CurrentWeatherDao
    private lateinit var forecastDao: ForecastDao
    private lateinit var networkConnectivityService: NetworkConnectivityService
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk(relaxed = true)
        currentWeatherDao = mockk(relaxed = true)
        forecastDao = mockk(relaxed = true)
        networkConnectivityService = mockk(relaxed = true)
        repository =
            WeatherRepositoryImpl(
                apiService,
                currentWeatherDao,
                forecastDao,
                networkConnectivityService,
            )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCurrentWeatherFromDb returns flow from dao`() =
        runTest {
            // Given
            val cityId = 0
            val entity =
                CurrentWeatherEntity(
                    id = cityId,
                    cityName = "Nairobi",
                    latitude = -1.2921,
                    longitude = 36.8219,
                    temperature = 298.15,
                    weatherMain = "Clear",
                    weatherDescription = "clear sky",
                    weatherIcon = "01d",
                    timestamp = 1697529600L,
                )

            val expectedDomainModel =
                CurrentWeather(
                    id = cityId,
                    cityName = "Nairobi",
                    latitude = -1.2921,
                    longitude = 36.8219,
                    temperature = 298.15,
                    weatherMain = "Clear",
                    weatherDescription = "clear sky",
                    weatherIcon = "01d",
                    timestamp = 1697529600L,
                )

            every { currentWeatherDao.getCurrentWeather(cityId) } returns flowOf(entity)

            // When
            val result = repository.getCurrentWeatherFromDb(cityId).first()

            // Then
            assertEquals(expectedDomainModel, result)
            verify { currentWeatherDao.getCurrentWeather(cityId) }
        }

    @Test
    fun `getCurrentWeatherFromDb returns null when no data`() =
        runTest {
            // Given
            val cityId = 0
            every { currentWeatherDao.getCurrentWeather(cityId) } returns flowOf(null)

            // When
            val result = repository.getCurrentWeatherFromDb(cityId).first()

            // Then
            assertEquals(null, result)
        }

    @Test
    fun `getForecastFromDb returns flow from dao`() =
        runTest {
            // Given
            val entities =
                listOf(
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
                    ),
                )

            val expectedDomainModels =
                listOf(
                    Forecast(
                        id = 1,
                        dateText = "2025-10-17 12:00:00",
                        temperature = 298.15,
                        weatherMain = "Clear",
                        weatherDescription = "clear sky",
                        weatherIcon = "01d",
                    ),
                )

            every { forecastDao.getForecast() } returns flowOf(entities)

            // When
            val result = repository.getForecastFromDb().first()

            // Then
            assertEquals(expectedDomainModels, result)
            verify { forecastDao.getForecast() }
        }

    @Test
    fun `getForecastFromDb returns empty list when no data`() =
        runTest {
            // Given
            every { forecastDao.getForecast() } returns flowOf(emptyList())

            // When
            val result = repository.getForecastFromDb().first()

            // Then
            assertEquals(0, result.size)
        }

    @Test
    fun `clearAllWeatherData deletes from both daos`() =
        runTest {
            // Given
            coEvery { currentWeatherDao.deleteAll() } just Runs
            coEvery { forecastDao.deleteAll() } just Runs

            // When
            repository.clearAllWeatherData()

            // Then
            coVerify { currentWeatherDao.deleteAll() }
            coVerify { forecastDao.deleteAll() }
        }

    @Test
    fun `clearCurrentWeather deletes only from current weather dao`() =
        runTest {
            // Given
            coEvery { currentWeatherDao.deleteAll() } just Runs

            // When
            repository.clearCurrentWeather()

            // Then
            coVerify { currentWeatherDao.deleteAll() }
            coVerify(exactly = 0) { forecastDao.deleteAll() }
        }

    @Test
    fun `fetchCurrentWeather does not fetch when network unavailable`() =
        runTest {
            // Given
            val latitude = -1.2921
            val longitude = 36.8219
            val apiKey = "test_api_key"

            every { networkConnectivityService.isNetworkAvailable() } returns false

            // When
            repository.fetchCurrentWeather(latitude, longitude, apiKey)

            // Then
            coVerify(exactly = 0) { apiService.getCurrentWeather(any(), any(), any()) }
            coVerify(exactly = 0) { currentWeatherDao.insertCurrentWeather(any()) }
        }

    @Test
    fun `fetchForecast does not fetch when network unavailable`() =
        runTest {
            // Given
            val latitude = -1.2921
            val longitude = 36.8219
            val apiKey = "test_api_key"
            val count = 7

            every { networkConnectivityService.isNetworkAvailable() } returns false

            // When
            repository.fetchForecast(latitude, longitude, apiKey, count)

            // Then
            coVerify(exactly = 0) { apiService.getForecast(any(), any(), any()) }
            coVerify(exactly = 0) { forecastDao.insertForecast(any()) }
        }
}
