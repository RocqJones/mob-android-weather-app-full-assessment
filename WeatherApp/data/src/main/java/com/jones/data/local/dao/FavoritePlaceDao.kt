package com.jones.data.local.dao

import androidx.room.*
import com.jones.data.local.entity.FavoritePlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePlaceDao {
    @Query("SELECT * FROM favorite_places ORDER BY addedAt DESC")
    fun getAllFavoritePlaces(): Flow<List<FavoritePlaceEntity>>

    @Query("SELECT * FROM favorite_places WHERE id = :id")
    suspend fun getFavoritePlaceById(id: Int): FavoritePlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlace(place: FavoritePlaceEntity)

    @Delete
    suspend fun deleteFavoritePlace(place: FavoritePlaceEntity)

    @Query("DELETE FROM favorite_places WHERE id = :id")
    suspend fun deleteFavoritePlaceById(id: Int)

    @Query("DELETE FROM favorite_places")
    suspend fun deleteAllFavoritePlaces()
}

