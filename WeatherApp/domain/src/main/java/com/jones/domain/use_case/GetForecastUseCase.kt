package com.jones.domain.use_case

import com.jones.data.local.entity.ForecastEntity
import com.jones.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Gets weather forecast with offline-first approach.
 * Returns cached data immediately and triggers API refresh if network is available.
 */
class GetForecastUseCase(
    private val repository: WeatherRepository,
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        count: Int,
    ): Flow<List<ForecastEntity>> {
        return repository.getForecastFromDb().onStart {
            repository.fetchForecast(latitude, longitude, apiKey, count)
        }
    }
}
