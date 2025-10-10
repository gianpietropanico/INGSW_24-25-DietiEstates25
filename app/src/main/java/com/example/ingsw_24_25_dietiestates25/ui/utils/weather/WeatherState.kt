package com.example.ingsw_24_25_dietiestates25.ui.utils.weather

import com.example.ingsw_24_25_dietiestates25.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
