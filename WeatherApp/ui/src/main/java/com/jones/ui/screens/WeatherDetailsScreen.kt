package com.jones.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jones.data.local.entity.CurrentWeatherEntity
import com.jones.data.local.entity.ForecastEntity
import com.jones.ui.components.CurrentWeatherCard
import com.jones.ui.components.ForecastCard
import com.jones.ui.components.OfflineMessage
import com.jones.ui.state.WeatherUiState
import com.jones.ui.util.getWeatherBackground
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    navController: NavController,
    uiState: WeatherUiState,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                    WeatherDetailsContent(
                        currentWeather = uiState.currentWeather,
                        forecast = uiState.forecast,
                        isOnline = uiState.isOnline
                    )
                }
                is WeatherUiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetry = onRetry,
                        isOnline = uiState.isOnline
                    )
                }
                is WeatherUiState.Offline -> {
                    WeatherDetailsContent(
                        currentWeather = uiState.currentWeather,
                        forecast = uiState.forecast,
                        isOnline = false
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailsContent(
    currentWeather: CurrentWeatherEntity?,
    forecast: List<ForecastEntity>?,
    isOnline: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Show offline message only when offline
        if (!isOnline) {
            item {
                OfflineMessage()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            currentWeather?.let {
                CurrentWeatherCard(weather = it)
                Spacer(modifier = Modifier.height(16.dp))

                // Additional weather details
                WeatherDetailsCard(weather = it)
                Spacer(modifier = Modifier.height(24.dp))
            } ?: run {
                Text(
                    text = "No current weather data available",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            Text(
                text = "Extended Forecast",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (forecast.isNullOrEmpty()) {
            item {
                Text(
                    text = "No forecast data available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(forecast) { forecastItem ->
                ForecastCard(forecast = forecastItem)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun WeatherDetailsCard(weather: CurrentWeatherEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(label = "City", value = weather.cityName ?: "Unknown")

            weather.latitude?.let { lat ->
                weather.longitude?.let { lon ->
                    DetailRow(
                        label = "Coordinates",
                        value = "${"%.4f".format(lat)}, ${"%.4f".format(lon)}"
                    )
                }
            }

            weather.temperature?.let {
                DetailRow(
                    label = "Temperature",
                    value = "${kelvinToCelsius(it)}°C (${kelvinToFahrenheit(it)}°F)"
                )
            }

            weather.weatherDescription?.let {
                DetailRow(
                    label = "Condition",
                    value = it.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                    }
                )
            }

            weather.weatherIcon?.let {
                DetailRow(label = "Icon Code", value = it)
            }

            weather.timestamp?.let {
                DetailRow(
                    label = "Last Updated",
                    value = formatFullTimestamp(it)
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    isOnline: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isOnline) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "⚠️ No Internet Connection",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

private fun kelvinToCelsius(kelvin: Double): Int = (kelvin - 273.15).toInt()

private fun kelvinToFahrenheit(kelvin: Double): Int = ((kelvin - 273.15) * 9 / 5 + 32).toInt()

private fun formatFullTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE, MMM dd, yyyy 'at' HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}
