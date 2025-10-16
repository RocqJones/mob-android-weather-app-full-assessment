package com.jones.di

import com.jones.data.repository.FavoritePlaceRepositoryImpl
import com.jones.data.repository.WeatherRepositoryImpl
import com.jones.domain.repository.FavoritePlaceRepository
import com.jones.domain.repository.WeatherRepository
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
                networkConnectivityService = get(),
            )
        }

        single<FavoritePlaceRepository> {
            FavoritePlaceRepositoryImpl(
                favoritePlaceDao = get(),
            )
        }
    }
