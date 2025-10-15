package com.jones.domain.use_case

import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

/**
 * Gets current weather with offline-first approach.
 * Returns cached data immediately and triggers API refresh if network is available.
 */
class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(
        cityId: Int,
        latitude: Double,
        longitude: Double,
        apiKey: String,
    ): Flow<CurrentWeatherEntity?> {
        repository.fetchCurrentWeather(latitude, longitude, apiKey)
        return repository.getCurrentWeatherFromDb(cityId)
    }
}

