package com.jones.domain.repository

import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun fetchCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
    )

    suspend fun fetchForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        count: Int,
    )

    fun getCurrentWeatherFromDb(cityId: Int): Flow<CurrentWeather?>

    fun getForecastFromDb(): Flow<List<Forecast>>
}
