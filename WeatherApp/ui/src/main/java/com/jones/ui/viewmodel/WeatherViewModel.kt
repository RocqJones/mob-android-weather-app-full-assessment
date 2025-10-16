package com.jones.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jones.core.constants.Constants
import com.jones.core.location.Coordinates
import com.jones.data.sync.WeatherSyncService
import com.jones.domain.use_case.weather.GetCurrentWeatherUseCase
import com.jones.domain.use_case.weather.GetForecastUseCase
import com.jones.ui.state.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val weatherSyncService: WeatherSyncService
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private var currentCoordinates: Coordinates? = null
    private val cnt = Constants.DEFAULT_COUNT

    private val apiKey = Constants.API_KEY


    init {
        startNetworkMonitoring()
    }

    private fun startNetworkMonitoring() {
        weatherSyncService.startNetworkMonitoring {
            currentCoordinates?.let { coords ->
                fetchWeatherForLocation(coords.latitude, coords.longitude)
            }
        }
    }

    /**
     * Update the location and fetch weather for the new coordinates
     */
    fun updateLocation(coordinates: Coordinates) {
        currentCoordinates = coordinates
        fetchWeatherForLocation(coordinates.latitude, coordinates.longitude)
    }

    fun fetchWeather() {
        val coords = currentCoordinates
        when {
            coords != null -> {
                fetchWeatherForLocation(coords.latitude, coords.longitude)
            }
            else -> {
                fetchWeatherForLocation(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
            }
        }
    }

    private fun fetchWeatherForLocation(latitude: Double, longitude: Double) {
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val currentWeatherFlow = getCurrentWeatherUseCase(latitude, longitude, apiKey)
                val forecastFlow = getForecastUseCase(latitude, longitude, apiKey, cnt)

                // Combine both flows for reactive updates
                combine(currentWeatherFlow, forecastFlow) { currentWeather, forecast ->
                    val isOnline = weatherSyncService.isNetworkAvailable()
                    when {
                        isOnline -> {
                            WeatherUiState.Success(currentWeather, forecast, true)
                        }
                        else -> {
                            WeatherUiState.Offline(currentWeather, forecast)
                        }
                    }
                }.catch { e ->
                    val isOnline = weatherSyncService.isNetworkAvailable()
                    emit(WeatherUiState.Error(e.message ?: "Unknown error occurred", isOnline))
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                val isOnline = weatherSyncService.isNetworkAvailable()
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Unknown error occurred",
                    isOnline
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        weatherSyncService.stopNetworkMonitoring()
    }
}