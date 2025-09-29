package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.repository.appointmentRepo.AppointmentRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.weatherRepo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepo: AppointmentRepository,
    private val weatherRepo: WeatherRepository
): ViewModel() {

    private val _state = MutableStateFlow(BookAppointmentState())
    val state: StateFlow<BookAppointmentState> = _state.asStateFlow()


    fun loadUnavailableDates(listingId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val result = appointmentRepo.getAppointmentsByListing(listingId)
                if (result is ApiResult.Success) {
                    val bookedDates = result.data?.map { it.date } ?: emptyList()
                    _state.update { it.copy(unavailableDates = bookedDates, isLoading = false) }
                } else {
                    _state.update { it.copy(isLoading = false, resultMessage = result.message) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, resultMessage = e.message) }
            }
        }
    }

//    fun selectDate(date: LocalDate) {
//        _state.update { it.copy(selectedDate = date) }
//        loadWeather(listing, date)
//    }
//
//    private fun loadWeather(listing: PropertyListing, date: LocalDate) {
//        viewModelScope.launch {
//            try {
//                val weather = weatherRepo.getWeatherForDate(listing, date)
//                _state.update { it.copy(weatherInfo = weather) }
//            } catch (e: Exception) {
//                _state.update { it.copy(weatherInfo = "N/D") }
//            }
//        }
//    }
//
//    fun bookAppointment(listing: PropertyListing, user: User) {
//        val date = _state.value.selectedDate ?: return
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//            try {
//                val result = appointmentRepo.bookAppointment(listing, user, date)
//                if (result is ApiResult.Success) {
//                    _state.update { it.copy(isLoading = false, success = true, resultMessage = "Appuntamento prenotato!") }
//                } else {
//                    _state.update { it.copy(isLoading = false, resultMessage = result.message) }
//                }
//            } catch (e: Exception) {
//                _state.update { it.copy(isLoading = false, resultMessage = e.message) }
//            }
//        }
//    }

}