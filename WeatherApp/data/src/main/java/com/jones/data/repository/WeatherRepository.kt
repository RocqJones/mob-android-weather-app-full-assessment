package com.jones.data.repository

import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    // Remote data sources
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

    // Local data sources
    fun getCurrentWeatherFromDb(cityId: Int): Flow<CurrentWeatherEntity?>

    fun getForecastFromDb(): Flow<List<ForecastEntity>>

    // Clear cache
    suspend fun clearAllWeatherData()

    suspend fun clearCurrentWeather()
}
