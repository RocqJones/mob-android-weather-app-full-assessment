package com.jones.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jones.data.local.dao.CurrentWeatherDao
import com.jones.data.local.dao.ForecastDao
import com.jones.data.local.dao.FavoritePlaceDao
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.data.local.entity.FavoritePlaceEntity

@Database(
    entities = [CurrentWeatherEntity::class, ForecastEntity::class, FavoritePlaceEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    abstract fun forecastDao(): ForecastDao

    abstract fun favoritePlaceDao(): FavoritePlaceDao
}
