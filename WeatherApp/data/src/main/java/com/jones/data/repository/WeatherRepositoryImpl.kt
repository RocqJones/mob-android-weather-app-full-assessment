package com.jones.data.repository

import android.util.Log
import com.jones.core.network.NetworkConnectivityService
import com.jones.data.local.dao.CurrentWeatherDao
import com.jones.data.local.dao.ForecastDao
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.data.remote.api.WeatherApiService
import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import com.jones.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastDao: ForecastDao,
    private val networkConnectivityService: NetworkConnectivityService,
) : WeatherRepository {
    private val TAG = "WeatherRepositoryImpl"

    override suspend fun fetchCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
    ) {
        // Only fetch from API if network is available
        if (!networkConnectivityService.isNetworkAvailable()) {
            return // Silent fail, use cached data
        }

        try {
            val response = apiService.getCurrentWeather(latitude, longitude, apiKey)

            val entity =
                CurrentWeatherEntity(
                    id = 0,
                    cityName = response.name,
                    latitude = latitude,
                    longitude = longitude,
                    temperature = response.main?.temp,
                    weatherMain = response.weather?.firstOrNull()?.main,
                    weatherDescription = response.weather?.firstOrNull()?.description,
                    weatherIcon = response.weather?.firstOrNull()?.icon,
                    timestamp = response.dt,
                )

            currentWeatherDao.insertCurrentWeather(entity)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching current weather: ${e.message}")
        }
    }

    override suspend fun fetchForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        count: Int,
    ) {
        // Only fetch from API if network is available
        if (!networkConnectivityService.isNetworkAvailable()) {
            return // Silent fail, use cached data
        }

        try {
            val response = apiService.getForecast(latitude, longitude, apiKey)

            val cityName = response.city?.name

            val entities =
                response.list?.map { item ->
                    ForecastEntity(
                        id = 0, // Auto-generated
                        cityName = cityName,
                        latitude = latitude,
                        longitude = longitude,
                        timestamp = item.dt,
                        temperature = item.main?.temp,
                        weatherMain = item.weather?.firstOrNull()?.main,
                        weatherDescription = item.weather?.firstOrNull()?.description,
                        weatherIcon = item.weather?.firstOrNull()?.icon,
                        dateText = item.dtTxt,
                    )
                } ?: emptyList()

            forecastDao.deleteAll()
            forecastDao.insertForecast(entities)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching forecast: ${e.message}")
        }
    }

    override fun getCurrentWeatherFromDb(cityId: Int): Flow<CurrentWeather?> {
        return currentWeatherDao.getCurrentWeather(cityId).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun getForecastFromDb(): Flow<List<Forecast>> {
        return forecastDao.getForecast().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    // Additional repository methods (not part of domain interface)
    suspend fun clearAllWeatherData() {
        currentWeatherDao.deleteAll()
        forecastDao.deleteAll()
    }

    suspend fun clearCurrentWeather() {
        currentWeatherDao.deleteAll()
    }

    // Mapping functions
    private fun CurrentWeatherEntity.toDomainModel(): CurrentWeather {
        return CurrentWeather(
            id = id,
            cityName = cityName,
            latitude = latitude,
            longitude = longitude,
            temperature = temperature,
            weatherMain = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            timestamp = timestamp
        )
    }

    private fun ForecastEntity.toDomainModel(): Forecast {
        return Forecast(
            id = id,
            dateText = dateText,
            temperature = temperature,
            weatherMain = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon
        )
    }
}
