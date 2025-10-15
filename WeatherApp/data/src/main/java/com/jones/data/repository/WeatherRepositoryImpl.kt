package com.jones.data.repository

import android.util.Log
import com.jones.core.network.NetworkConnectivityService
import com.jones.data.local.dao.CurrentWeatherDao
import com.jones.data.local.dao.ForecastDao
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.data.remote.api.WeatherApiService
import kotlinx.coroutines.flow.Flow

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
                    id = response.id ?: 0,
                    cityName = response.name,
                    latitude = response.coord?.lat,
                    longitude = response.coord?.lon,
                    temperature = response.main?.temp,
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
    ) {
        // Only fetch from API if network is available
        if (!networkConnectivityService.isNetworkAvailable()) {
            return // Silent fail, use cached data
        }

        try {
            val response = apiService.getForecast(latitude, longitude, apiKey)

            val cityId = response.city?.id ?: 0
            val cityName = response.city?.name
            val lat = response.city?.coord?.lat
            val lon = response.city?.coord?.lon

            val entities =
                response.list?.map { item ->
                    ForecastEntity(
                        id = 0,
                        cityId = cityId,
                        cityName = cityName,
                        latitude = lat,
                        longitude = lon,
                        timestamp = item.dt,
                        temperature = item.main?.temp,
                        weatherDescription = item.weather?.firstOrNull()?.description,
                        weatherIcon = item.weather?.firstOrNull()?.icon,
                        dateText = item.dtTxt,
                    )
                } ?: emptyList()

            forecastDao.deleteForecastByCity(cityId)
            forecastDao.insertForecast(entities)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching forecast: ${e.message}")
        }
    }

    override fun getCurrentWeatherFromDb(cityId: Int): Flow<CurrentWeatherEntity?> {
        return currentWeatherDao.getCurrentWeather(cityId)
    }

    override fun getForecastFromDb(cityId: Int): Flow<List<ForecastEntity>> {
        return forecastDao.getForecast(cityId)
    }

    override suspend fun clearAllWeatherData() {
        currentWeatherDao.deleteAll()
        forecastDao.deleteAll()
    }

    override suspend fun clearCurrentWeather() {
        currentWeatherDao.deleteAll()
    }

    override suspend fun clearForecastData(cityId: Int) {
        forecastDao.deleteForecastByCity(cityId)
    }
}
