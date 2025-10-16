package com.jones.ui.state

import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast

sealed class WeatherUiState {
    object Loading : WeatherUiState()

    data class Success(
        val currentWeather: CurrentWeather?,
        val forecast: List<Forecast>?,
        val isOnline: Boolean,
    ) : WeatherUiState()

    data class Error(val message: String, val isOnline: Boolean) : WeatherUiState()

    data class Offline(
        val currentWeather: CurrentWeather?,
        val forecast: List<Forecast>?,
    ) : WeatherUiState()
}
