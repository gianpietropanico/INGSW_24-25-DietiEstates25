package com.example.ingsw_24_25_dietiestates25.weather

//data class WeatherInfo(
//    val weatherDataPerDay: Map<Int, List<WeatherData>>,
//    val currentWeatherData: WeatherData?
//)
data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?,
    val isFallback: Boolean = false,   // <-- aggiunto
    val message: String? = null        // <-- aggiunto
)
