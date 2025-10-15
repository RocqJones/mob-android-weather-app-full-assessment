package com.jones.core.util

/**
 * Enum for different weather conditions.
 * Maps API weather types to background drawable resources.
 */
enum class WeatherType(val apiValue: String, val drawableName: String) {
    CLEAR("Clear", "sunny"),
    CLOUDS("Clouds", "cloudy"),
    RAIN("Rain", "rainy"),
    SNOW("Snow", "forest"),
    ;

    companion object {
        /**
         * Maps the weather main value from API/Entity to the corresponding WeatherType.
         */
        fun fromWeatherMain(weatherMain: String?): WeatherType {
            return entries.find {
                it.apiValue.equals(weatherMain, ignoreCase = true)
            } ?: SNOW
        }

        /**
         * Gets the drawable resource name for a given weather main value.
         */
        fun getDrawableNameForWeather(weatherMain: String?): String {
            return fromWeatherMain(weatherMain).drawableName
        }
    }
}
