package com.example.ingsw_24_25_dietiestates25.ui.utils.weather

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo

data class WeatherState(
    val isLoading: Boolean = false,
    val weatherForecast: List<WeatherInfo> = emptyList(),
    val error: String? = null
)
