package com.jones.domain.model

data class Forecast(
    val id: Int = 0,
    val dateText: String?,
    val temperature: Double?,
    val weatherMain: String?,
    val weatherDescription: String?,
    val weatherIcon: String?
)

