package com.example.ingsw_24_25_dietiestates25.ui.utils.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo
import com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun loadWeather(listing: PropertyListing, date: LocalDate) {
        _state.value = WeatherState(isLoading = true)
        viewModelScope.launch {
            try {
                val weather = weatherRepo.getWeatherForDate(listing, date)
                _state.value = WeatherState(weatherForecast = weather)
            } catch (e: Exception) {
                _state.value = WeatherState(error = e.message ?: "Errore caricamento meteo")
            }
        }
    }
}