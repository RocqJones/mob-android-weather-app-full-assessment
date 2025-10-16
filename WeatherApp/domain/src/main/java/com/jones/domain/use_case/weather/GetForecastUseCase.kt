package com.jones.domain.use_case.weather

import com.jones.domain.model.Forecast
import com.jones.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

/**
 * Gets weather forecast with offline-first approach.
 * Returns cached data immediately and triggers API refresh if network is available.
 */
class GetForecastUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        count: Int,
    ): Flow<List<Forecast>> {
        repository.fetchForecast(latitude, longitude, apiKey, count)

        return repository.getForecastFromDb()
    }
}
