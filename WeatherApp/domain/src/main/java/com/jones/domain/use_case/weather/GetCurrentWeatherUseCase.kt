package com.jones.domain.use_case.weather

import com.jones.domain.model.CurrentWeather
import com.jones.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

/**
 * Gets current weather with offline-first approach.
 * Returns cached data immediately and triggers API refresh if network is available.
 */
class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        apiKey: String,
    ): Flow<CurrentWeather?> {
        repository.fetchCurrentWeather(latitude, longitude, apiKey)

        return repository.getCurrentWeatherFromDb(0)
    }
}
