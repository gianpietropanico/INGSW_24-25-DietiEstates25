package com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo


import com.example.ingsw_24_25_dietiestates25.ui.utils.Resource
import javax.inject.Inject
import com.example.ingsw_24_25_dietiestates25.data.remote.*
import com.example.ingsw_24_25_dietiestates25.data.weather.toWeatherInfo
import com.example.ingsw_24_25_dietiestates25.weather.WeatherInfo
import java.time.LocalDate

class WeatherRepositoryImpl @Inject constructor(
    private val apiClient: WeatherApiClient
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double, date: LocalDate): Resource<WeatherInfo> {
        return try {
            val dto = apiClient.getWeatherData(lat, long, date)
            val weatherInfo = dto.toWeatherInfo()
            Resource.Success(data = weatherInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}