package com.jones.data.repository

import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    // Remote data sources
    suspend fun fetchCurrentWeather(latitude: Double, longitude: Double, apiKey: String)

    suspend fun fetchForecast(latitude: Double, longitude: Double, apiKey: String)

    // Local data sources
    fun getCurrentWeatherFromDb(cityId: Int): Flow<CurrentWeatherEntity?>

    fun getForecastFromDb(cityId: Int): Flow<List<ForecastEntity>>

    // Clear cache
    suspend fun clearAllWeatherData()

    suspend fun clearCurrentWeather()

    suspend fun clearForecastData(cityId: Int)
}
