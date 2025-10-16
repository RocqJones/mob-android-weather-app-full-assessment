package com.jones.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import com.jones.ui.R
import com.jones.ui.components.*
import com.jones.ui.state.WeatherUiState
import com.jones.ui.util.getWeatherBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: WeatherUiState,
    onRetry: () -> Unit,
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { TextMedium(stringResource(R.string.home)) },
                actions = {
                    IconButton(onClick = {
                        navController?.navigate("place_search")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_place)
                        )
                    }
                    IconButton(onClick = {
                        navController?.navigate("favorites")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.favorites)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Add background image based on weather condition
            val weatherMain = when (uiState) {
                is WeatherUiState.Success -> uiState.currentWeather?.weatherMain
                is WeatherUiState.Offline -> uiState.currentWeather?.weatherMain
                else -> null
            }

            Image(
                painter = painterResource(id = getWeatherBackground(weatherMain)),
                contentDescription = "Weather Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            when (uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is WeatherUiState.Success -> {
                    WeatherContent(
                        currentWeather = uiState.currentWeather,
                        forecast = uiState.forecast,
                        isOnline = uiState.isOnline,
                        navController = navController
                    )
                }
                is WeatherUiState.Error -> {
                    ErrorScreen(
                        message = uiState.message,
                        onRetry = onRetry,
                        isOnline = uiState.isOnline
                    )
                }
                is WeatherUiState.Offline -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        OfflineMessage()
                        WeatherContent(
                            currentWeather = uiState.currentWeather,
                            forecast = uiState.forecast,
                            isOnline = false,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    currentWeather: CurrentWeather?,
    forecast: List<Forecast>?,
    isOnline: Boolean,
    navController: NavController?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            currentWeather?.let {
                CurrentWeatherCard(weather = it, navController)

                Spacer(modifier = Modifier.height(24.dp))
            } ?: run {
                TextRegular(
                    text = stringResource(R.string.no_current_weather_data_available),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            TextBold(
                text = "5-Day Forecast",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        when {
            forecast.isNullOrEmpty() -> {
                item {
                    TextRegular(
                        text = stringResource(R.string.no_forecast_data_available),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            else -> {
                items(forecast) { forecastItem ->
                    ForecastCard(forecast = forecastItem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
