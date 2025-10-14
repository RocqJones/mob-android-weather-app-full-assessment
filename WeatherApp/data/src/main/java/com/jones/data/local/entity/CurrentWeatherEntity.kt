package com.jones.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey val id: Int,
    val cityName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val temperature: Double?,
    val weatherDescription: String?,
    val weatherIcon: String?,
    val timestamp: Long?
)
