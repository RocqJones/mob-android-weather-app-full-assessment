package com.jones.ui.util

import com.jones.core.util.WeatherType
import com.jones.ui.R

/**
 * Extension function to get the drawable resource ID for a weather background.
 *
 * @param weatherMain The main weather condition from the API:"Clear", "Clouds", "Rain", "Snow")
 * @return The drawable resource ID for the corresponding background image
 */
fun getWeatherBackground(weatherMain: String?): Int {
    val drawableName = WeatherType.getDrawableNameForWeather(weatherMain)

    return when (drawableName) {
        "sunny" -> R.drawable.sunny
        "cloudy" -> R.drawable.cloudy
        "rainy" -> R.drawable.rainy
        "forest" -> R.drawable.forest
        else -> R.drawable.forest
    }
}

/**
 * Useful for non-Compose contexts for returning drawables name as a string.
 */
fun getWeatherBackgroundName(weatherMain: String?): String {
    return WeatherType.getDrawableNameForWeather(weatherMain)
}
