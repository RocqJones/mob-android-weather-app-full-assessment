package com.jones.weatherapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jones.ui.screens.HomeScreen
import com.jones.ui.screens.WeatherDetailsScreen
import com.jones.ui.viewmodel.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WeatherHome.route
    ) {
        composable(Screen.WeatherHome.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                val uiState by weatherViewModel.uiState.collectAsState()

                HomeScreen(
                    uiState = uiState,
                    onRetry = { weatherViewModel.fetchWeather() },
                    navController = navController
                )
            }
        }

        composable(Screen.WeatherDetails.route) {
            val uiState by weatherViewModel.uiState.collectAsState()

            WeatherDetailsScreen(
                navController = navController,
                uiState = uiState,
                onRetry = { weatherViewModel.fetchWeather() }
            )
        }
    }
}
