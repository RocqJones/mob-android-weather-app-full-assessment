package com.jones.domain.model

data class FavoritePlace(
    val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val addedAt: Long = System.currentTimeMillis()
)

