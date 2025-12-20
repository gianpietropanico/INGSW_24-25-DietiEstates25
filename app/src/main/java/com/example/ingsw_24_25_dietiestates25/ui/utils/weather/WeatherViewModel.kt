package com.example.ingsw_24_25_dietiestates25.ui.utils.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing

import com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo.WeatherRepository
import com.example.ingsw_24_25_dietiestates25.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set


    fun loadWeatherInfo(propertyListing: PropertyListing, date: LocalDate) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            // Prende le coordinate dalla property
            val latitude = propertyListing.property.latitude
            val longitude = propertyListing.property.longitude

            if (latitude != null && longitude != null) {
                when (val result = weatherRepo.getWeatherData(latitude, longitude, date)) {
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } else {
                state = state.copy(
                    isLoading = false,
                    error = "Coordinates not available for this listing."
                )
            }
        }
    }
}