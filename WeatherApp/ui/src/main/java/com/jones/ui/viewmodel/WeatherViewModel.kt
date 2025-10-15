package com.jones.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jones.core.constants.Constants
import com.jones.data.sync.WeatherSyncService
import com.jones.domain.use_case.GetCurrentWeatherUseCase
import com.jones.domain.use_case.GetForecastUseCase
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
    private val latitude = Constants.DEFAULT_LATITUDE
    private val longitude = Constants.DEFAULT_LONGITUDE
    private val apiKey = Constants.API_KEY

    init {
        startNetworkMonitoring()
        fetchWeather()
    }

    private fun startNetworkMonitoring() {
        weatherSyncService.startNetworkMonitoring {
            fetchWeather()
        }
    }

    fun fetchWeather() {
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val currentWeatherFlow = getCurrentWeatherUseCase(latitude, longitude, apiKey)
                val forecastFlow = getForecastUseCase(latitude, longitude, apiKey)

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