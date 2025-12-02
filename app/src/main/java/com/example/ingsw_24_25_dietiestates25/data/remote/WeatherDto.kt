package com.example.ingsw_24_25_dietiestates25.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class WeatherDto(
//    @SerialName("hourly")
//    val weatherData: WeatherDataDto
//)

@Serializable
data class WeatherDto(
    @SerialName("hourly")
    val weatherData: WeatherDataDto? = null,
    val isFallback: Boolean = false,
    val message: String? = null
)