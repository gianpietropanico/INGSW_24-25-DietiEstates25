package com.example.ingsw_24_25_dietiestates25.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class WeatherDataDto(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temperatures: List<Double>,
    @SerialName("weathercode")
    val weatherCodes: List<Int>,
    @SerialName("pressure_msl")
    val pressures: List<Double>,
    @SerialName("windspeed_10m")
    val windSpeeds: List<Double>,
    @SerialName("relativehumidity_2m")
    val humidities: List<Double>
)