package com.jones.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jones.core.util.formatFullTimestamp
import com.jones.core.util.kelvinToCelsius
import com.jones.core.util.kelvinToFahrenheit
import com.jones.domain.model.CurrentWeather
import com.jones.domain.model.Forecast
import com.jones.ui.R
import com.jones.ui.components.ForecastCard
import com.jones.ui.components.OfflineMessage
import com.jones.ui.components.TextBold
import com.jones.ui.components.TextMedium
import com.jones.ui.components.TextRegular
import com.jones.ui.state.WeatherUiState
import com.jones.ui.util.getWeatherBackground
import java.util.Locale

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
                title = { TextMedium(stringResource(R.string.details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
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
    currentWeather: CurrentWeather?,
    forecast: List<Forecast>?,
    isOnline: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (!isOnline) {
            item {
                OfflineMessage()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            currentWeather?.let {
                WeatherDetailsCard(weather = it)
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
                text = "Extended Forecast",
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

@Composable
private fun WeatherDetailsCard(weather: CurrentWeather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TextBold(
                text = stringResource(R.string.additional_information),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(label = stringResource(R.string.city), value = weather.cityName ?: "Unknown")

            weather.latitude?.let { lat ->
                weather.longitude?.let { lon ->
                    DetailRow(
                        label = stringResource(R.string.coordinates),
                        value = "${"%.4f".format(lat)}, ${"%.4f".format(lon)}"
                    )
                }
            }

            weather.temperature?.let {
                DetailRow(
                    label = stringResource(R.string.temperature),
                    value = "${kelvinToCelsius(it)}°C (${kelvinToFahrenheit(it)}°F)"
                )
            }

            weather.weatherDescription?.let {
                DetailRow(
                    label = stringResource(R.string.condition),
                    value = it.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                    }
                )
            }

            weather.weatherIcon?.let {
                DetailRow(label = stringResource(R.string.icon_code), value = it)
            }

            weather.timestamp?.let {
                DetailRow(
                    label = stringResource(R.string.last_updated),
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
        TextRegular(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        TextMedium(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
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
                TextMedium(
                    text = stringResource(R.string.no_internet_connection),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.error),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextBold(
            text = stringResource(R.string.something_went_wrong),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextRegular(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            TextMedium(stringResource(R.string.retry))
        }
    }
}
