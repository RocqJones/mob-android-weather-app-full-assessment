package com.jones.di

import com.jones.data.repository.WeatherRepository
import com.jones.data.repository.WeatherRepositoryImpl
import org.koin.dsl.module

/**
 * Koin module for our repository dependencies.
 */
val repositoryModule =
    module {
        single<WeatherRepository> {
            WeatherRepositoryImpl(
                apiService = get(),
                currentWeatherDao = get(),
                forecastDao = get(),
            )
        }
    }
