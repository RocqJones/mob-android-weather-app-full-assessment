package com.jones.domain.model

data class CurrentWeather(
    val id: Int,
    val cityName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val temperature: Double?,
    val weatherMain: String?,
    val weatherDescription: String?,
    val weatherIcon: String?,
    val timestamp: Long?,
)
