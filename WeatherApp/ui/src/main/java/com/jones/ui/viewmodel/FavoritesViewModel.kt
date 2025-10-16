package com.jones.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jones.domain.model.FavoritePlace
import com.jones.domain.use_case.favourites.FavouritesPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favouritesPlacesUseCase: FavouritesPlacesUseCase,
) : ViewModel() {
    private val _favoritePlaces = MutableStateFlow<List<FavoritePlace>>(emptyList())
    val favoritePlaces: StateFlow<List<FavoritePlace>> = _favoritePlaces.asStateFlow()

    init {
        loadFavoritePlaces()
    }

    private fun loadFavoritePlaces() {
        viewModelScope.launch {
            favouritesPlacesUseCase.getAllFavoritePlaces().collect { places ->
                _favoritePlaces.value = places
            }
        }
    }

    fun addFavoritePlace(
        name: String,
        latitude: Double,
        longitude: Double,
    ) {
        viewModelScope.launch {
            val place =
                FavoritePlace(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                )
            favouritesPlacesUseCase.addFavoritePlace(place)
        }
    }

    fun deleteFavoritePlace(id: Int) {
        viewModelScope.launch {
            favouritesPlacesUseCase.deleteFavoritePlace(id)
        }
    }
}
