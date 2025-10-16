package com.jones.data.repository

import com.jones.data.local.dao.FavoritePlaceDao
import com.jones.data.local.entity.FavoritePlaceEntity
import com.jones.domain.model.FavoritePlace
import com.jones.domain.repository.FavoritePlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritePlaceRepositoryImpl(
    private val favoritePlaceDao: FavoritePlaceDao,
) : FavoritePlaceRepository {
    override fun getAllFavoritePlaces(): Flow<List<FavoritePlace>> {
        return favoritePlaceDao.getAllFavoritePlaces().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFavoritePlaceById(id: Int): FavoritePlace? {
        return favoritePlaceDao.getFavoritePlaceById(id)?.toDomain()
    }

    override suspend fun insertFavoritePlace(place: FavoritePlace) {
        favoritePlaceDao.insertFavoritePlace(place.toEntity())
    }

    override suspend fun deleteFavoritePlace(place: FavoritePlace) {
        favoritePlaceDao.deleteFavoritePlace(place.toEntity())
    }

    override suspend fun deleteFavoritePlaceById(id: Int) {
        favoritePlaceDao.deleteFavoritePlaceById(id)
    }

    private fun FavoritePlaceEntity.toDomain(): FavoritePlace {
        return FavoritePlace(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            addedAt = addedAt,
        )
    }

    private fun FavoritePlace.toEntity(): FavoritePlaceEntity {
        return FavoritePlaceEntity(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            addedAt = addedAt,
        )
    }
}
