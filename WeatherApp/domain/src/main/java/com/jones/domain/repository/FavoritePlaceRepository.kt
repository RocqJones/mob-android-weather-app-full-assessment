package com.jones.domain.repository

import com.jones.domain.model.FavoritePlace
import kotlinx.coroutines.flow.Flow

interface FavoritePlaceRepository {
    fun getAllFavoritePlaces(): Flow<List<FavoritePlace>>
    suspend fun getFavoritePlaceById(id: Int): FavoritePlace?
    suspend fun insertFavoritePlace(place: FavoritePlace)
    suspend fun deleteFavoritePlace(place: FavoritePlace)
    suspend fun deleteFavoritePlaceById(id: Int)
}

