package com.jones.data.repository

import com.jones.data.local.dao.CurrentWeatherDao
import com.jones.data.local.dao.ForecastDao
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.data.remote.api.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastDao: ForecastDao
) : WeatherRepository {

    override suspend fun fetchCurrentWeather(latitude: Double, longitude: Double, apiKey: String) {
        val response = apiService.getCurrentWeather(latitude, longitude, apiKey)

        val entity = CurrentWeatherEntity(
            id = response.id ?: 0,
            cityName = response.name,
            latitude = response.coord?.lat,
            longitude = response.coord?.lon,
            temperature = response.main?.temp,
            weatherDescription = response.weather?.firstOrNull()?.description,
            weatherIcon = response.weather?.firstOrNull()?.icon,
            timestamp = response.dt
        )

        currentWeatherDao.insertCurrentWeather(entity)
    }

    override suspend fun fetchForecast(latitude: Double, longitude: Double, apiKey: String) {
        val response = apiService.getForecast(latitude, longitude, apiKey)

        val cityId = response.city?.id ?: 0
        val cityName = response.city?.name
        val lat = response.city?.coord?.lat
        val lon = response.city?.coord?.lon

        val entities = response.list?.map { item ->
            ForecastEntity(
                id = 0, // Auto-generated
                cityId = cityId,
                cityName = cityName,
                latitude = lat,
                longitude = lon,
                timestamp = item.dt,
                temperature = item.main?.temp,
                weatherDescription = item.weather?.firstOrNull()?.description,
                weatherIcon = item.weather?.firstOrNull()?.icon,
                dateText = item.dtTxt
            )
        } ?: emptyList()

        forecastDao.deleteForecastByCity(cityId)
        forecastDao.insertForecast(entities)
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
