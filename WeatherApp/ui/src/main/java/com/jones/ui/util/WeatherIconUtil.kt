package com.jones.ui.util

import com.jones.core.util.WeatherType
import com.jones.ui.R

/**
 * Maps weather conditions to mipmap icon resources.
 */
fun getWeatherIcon(weatherMain: String?): Int {
    return when (WeatherType.fromWeatherMain(weatherMain)) {
        WeatherType.CLEAR -> R.mipmap.sun_light
        WeatherType.CLOUDS -> R.mipmap.cloudy_light
        WeatherType.RAIN -> R.mipmap.rain_light
        WeatherType.SNOW -> R.mipmap.heavy_snowfall
    }
}

