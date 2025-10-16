package com.jones.weatherapp.ui.navigation

sealed class Screen(val route: String) {
    object WeatherHome : Screen("weather_home")

    object WeatherDetails : Screen("weather_details")

    object Favorites : Screen("favorites")

    object PlaceSearch : Screen("place_search")
}
