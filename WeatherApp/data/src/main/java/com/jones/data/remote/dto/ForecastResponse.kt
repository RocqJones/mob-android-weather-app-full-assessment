package com.jones.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTOs for the forecast response from the weather API
data class ForecastResponse(
    @SerializedName("cod") val cod: String?,
    @SerializedName("message") val message: Int?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("list") val list: List<ForecastItem>?,
    @SerializedName("city") val city: City?,
)

data class ForecastItem(
    @SerializedName("dt") val dt: Long?,
    @SerializedName("main") val main: Main?,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("pop") val pop: Double?,
    @SerializedName("sys") val sys: SysForecast?,
    @SerializedName("dt_txt") val dtTxt: String?,
)

data class City(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: Coord?,
    @SerializedName("country") val country: String?,
    @SerializedName("population") val population: Int?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?,
)

data class SysForecast(
    @SerializedName("pod") val pod: String?,
)
