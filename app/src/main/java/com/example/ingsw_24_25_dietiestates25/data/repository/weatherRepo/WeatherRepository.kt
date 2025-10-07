package com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo
import java.time.LocalDate

interface WeatherRepository {
    suspend fun getWeatherForDate(listing: PropertyListing, date: LocalDate): List<WeatherInfo>
}