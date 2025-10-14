package com.jones.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityId: Int?,
    val cityName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long?,
    val temperature: Double?,
    val weatherDescription: String?,
    val weatherIcon: String?,
    val dateText: String?,
)
