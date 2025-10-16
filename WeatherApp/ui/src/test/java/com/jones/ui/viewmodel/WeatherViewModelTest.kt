package com.jones.ui.viewmodel

import com.jones.core.location.Coordinates
import com.jones.data.sync.WeatherSyncService
import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import com.jones.domain.use_case.weather.GetCurrentWeatherUseCase
import com.jones.domain.use_case.weather.GetForecastUseCase
import com.jones.ui.state.WeatherUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for WeatherViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    private lateinit var getForecastUseCase: GetForecastUseCase
    private lateinit var weatherSyncService: WeatherSyncService
    private lateinit var viewModel: WeatherViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentWeatherUseCase = mockk(relaxed = true)
        getForecastUseCase = mockk(relaxed = true)
        weatherSyncService = mockk(relaxed = true)

        every { weatherSyncService.startNetworkMonitoring(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel starts network monitoring on initialization`() {
        // When
        viewModel = WeatherViewModel(getCurrentWeatherUseCase, getForecastUseCase, weatherSyncService)

        // Then
        verify { weatherSyncService.startNetworkMonitoring(any()) }
    }

    @Test
    fun `updateLocation fetches weather for given coordinates`() = runTest {
        // Given
        val latitude = -1.286389
        val longitude = 36.817223
        val mockWeather = CurrentWeather(
            id = 0,
            cityName = "Nairobi",
            latitude = latitude,
            longitude = longitude,
            temperature = 298.15,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timestamp = System.currentTimeMillis()
        )
        val mockForecast = listOf(
            Forecast(
                id = 1,
                dateText = "2025-10-17 12:00:00",
                temperature = 298.15,
                weatherMain = "Clear",
                weatherDescription = "clear sky",
                weatherIcon = "01d"
            )
        )

        coEvery { getCurrentWeatherUseCase.invoke(latitude, longitude, any()) } returns flowOf(mockWeather)
        coEvery { getForecastUseCase.invoke(latitude, longitude, any(), any()) } returns flowOf(mockForecast)

        viewModel = WeatherViewModel(getCurrentWeatherUseCase, getForecastUseCase, weatherSyncService)

        // When
        viewModel.updateLocation(Coordinates(latitude, longitude))

        // Wait for state update
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Success || state is WeatherUiState.Offline)
        coVerify { getCurrentWeatherUseCase.invoke(latitude, longitude, any()) }
        coVerify { getForecastUseCase.invoke(latitude, longitude, any(), any()) }
    }

    @Test
    fun `fetchWeather uses default coordinates when no location set`() = runTest {
        // Given
        coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any()) } returns flowOf(null)
        coEvery { getForecastUseCase.invoke(any(), any(), any(), any()) } returns flowOf(emptyList())

        viewModel = WeatherViewModel(getCurrentWeatherUseCase, getForecastUseCase, weatherSyncService)

        // When
        viewModel.fetchWeather()

        // Wait for state update
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { getCurrentWeatherUseCase.invoke(any(), any(), any()) }
        coVerify { getForecastUseCase.invoke(any(), any(), any(), any()) }
    }

    @Test
    fun `uiState starts with Loading state`() {
        // Given & When
        viewModel = WeatherViewModel(getCurrentWeatherUseCase, getForecastUseCase, weatherSyncService)

        // Then
        assertTrue(viewModel.uiState.value is WeatherUiState.Loading)
    }

    @Test
    fun `updateLocation updates current coordinates`() = runTest {
        // Given
        val firstCoords = Coordinates(-1.286389, 36.817223)
        val secondCoords = Coordinates(40.7128, -74.0060)

        coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any()) } returns flowOf(null)
        coEvery { getForecastUseCase.invoke(any(), any(), any(), any()) } returns flowOf(emptyList())

        viewModel = WeatherViewModel(getCurrentWeatherUseCase, getForecastUseCase, weatherSyncService)

        // When
        viewModel.updateLocation(firstCoords)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateLocation(secondCoords)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { getCurrentWeatherUseCase.invoke(firstCoords.latitude, firstCoords.longitude, any()) }
        coVerify(exactly = 1) { getCurrentWeatherUseCase.invoke(secondCoords.latitude, secondCoords.longitude, any()) }
    }
}

