package com.jones.di

import com.jones.domain.use_case.weather.GetCurrentWeatherUseCase
import com.jones.domain.use_case.weather.GetForecastUseCase
import com.jones.domain.use_case.favourites.FavouritesPlacesUseCase
import com.jones.ui.viewmodel.FavoritesViewModel
import com.jones.ui.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Aggregated Koin modules in our app.
 */
val appModule =
    module {
        // Use cases
        single { GetCurrentWeatherUseCase(get()) }
        single { GetForecastUseCase(get()) }
        single { FavouritesPlacesUseCase(get()) }

        // ViewModel
        viewModel { WeatherViewModel(get(), get(), get()) }
        viewModel { FavoritesViewModel(get()) }
    }

val appModules =
    listOf(
        coreModule,
        networkModule,
        databaseModule,
        repositoryModule,
        appModule,
    )
