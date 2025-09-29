package com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import io.ktor.client.HttpClient
import java.time.LocalDate
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionManager: UserSessionManager
) : WeatherRepository {

    override suspend fun getWeatherForDate(
        listing: PropertyListing,
        date: LocalDate
    ) {
        TODO("Not yet implemented")
    }

}