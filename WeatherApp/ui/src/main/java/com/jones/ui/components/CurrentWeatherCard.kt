package com.jones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jones.core.util.formatFullTimestamp
import com.jones.core.util.kelvinToCelsius
import com.jones.domain.model.CurrentWeather
import com.jones.ui.R
import com.jones.ui.util.getWeatherIcon
import java.util.Locale

@Composable
fun CurrentWeatherCard(
    weather: CurrentWeather,
    navController: NavController? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController?.navigate("weather_details")
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextBold(
                    text = weather.cityName ?: stringResource(R.string.unknown_location),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = getWeatherIcon(weather.weatherMain)),
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextBold(
                text = "${weather.temperature?.let { kelvinToCelsius(it) } ?: "--"}°C",
                style = MaterialTheme.typography.displayLarge
            )

            TextRegular(
                text = weather.weatherDescription?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                } ?: stringResource(R.string.no_description),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            weather.timestamp?.let {
                TextRegular(
                    text = "Last Updated: ${formatFullTimestamp(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
