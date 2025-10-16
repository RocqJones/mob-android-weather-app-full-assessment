package com.jones.domain.use_case.favourites

import com.jones.domain.model.FavoritePlace
import com.jones.domain.repository.FavoritePlaceRepository
import kotlinx.coroutines.flow.Flow

class FavouritesPlacesUseCase(
    private val repository: FavoritePlaceRepository,
) {
    fun getAllFavoritePlaces(): Flow<List<FavoritePlace>> {
        return repository.getAllFavoritePlaces()
    }

    suspend fun addFavoritePlace(place: FavoritePlace) {
        repository.insertFavoritePlace(place)
    }

    suspend fun deleteFavoritePlace(id: Int) {
        repository.deleteFavoritePlaceById(id)
    }

    suspend fun getFavoritePlaceById(id: Int): FavoritePlace? {
        return repository.getFavoritePlaceById(id)
    }
}
