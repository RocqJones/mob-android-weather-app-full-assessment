package com.jones.di

import androidx.room.Room
import com.jones.data.local.database.WeatherDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for our database-related dependencies.
 */
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { get<WeatherDatabase>().currentWeatherDao() }
    single { get<WeatherDatabase>().forecastDao() }
}