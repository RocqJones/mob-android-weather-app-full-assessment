package com.jones.weatherapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jones.core.location.Coordinates
import com.jones.ui.location.rememberLocationState
import com.jones.ui.screens.FavoritesScreen
import com.jones.ui.screens.HomeScreen
import com.jones.ui.screens.PlaceSearchScreen
import com.jones.ui.screens.WeatherDetailsScreen
import com.jones.ui.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(weatherViewModel: WeatherViewModel = koinViewModel()) {
    val navController = rememberNavController()

    val locationState = rememberLocationState { coordinates ->
        weatherViewModel.updateLocation(coordinates)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.WeatherHome.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.WeatherHome.route) {
            val uiState by weatherViewModel.uiState.collectAsState()

            HomeScreen(
                uiState = uiState,
                onRetry = { weatherViewModel.fetchWeather() },
                navController = navController,
            )
        }

        composable(Screen.WeatherDetails.route) {
            val uiState by weatherViewModel.uiState.collectAsState()

            WeatherDetailsScreen(
                navController = navController,
                uiState = uiState,
                onRetry = { weatherViewModel.fetchWeather() },
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                navController = navController,
                onPlaceSelected = { latitude, longitude ->
                    weatherViewModel.updateLocation(Coordinates(latitude, longitude))
                }
            )
        }

        composable(Screen.PlaceSearch.route) {
            PlaceSearchScreen(
                navController = navController,
                onPlaceSelected = { name, latitude, longitude ->
                    weatherViewModel.updateLocation(Coordinates(latitude, longitude))
                }
            )
        }
    }
}
