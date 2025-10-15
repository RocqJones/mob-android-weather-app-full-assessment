package com.jones.ui.state

import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(
        val currentWeather: CurrentWeatherEntity?,
        val forecast: List<ForecastEntity>?,
        val isOnline: Boolean
    ) : WeatherUiState()
    data class Error(val message: String, val isOnline: Boolean) : WeatherUiState()
    data class Offline(
        val currentWeather: CurrentWeatherEntity?,
        val forecast: List<ForecastEntity>?
    ) : WeatherUiState()
}

