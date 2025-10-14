package com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.weather.WeatherInfo
import com.example.ingsw_24_25_dietiestates25.ui.utils.Resource
import java.time.LocalDate

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double, date: LocalDate): Resource<WeatherInfo>
}