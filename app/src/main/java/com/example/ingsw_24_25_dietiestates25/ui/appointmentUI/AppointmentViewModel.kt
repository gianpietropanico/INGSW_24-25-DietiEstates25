package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.WeatherInfo
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.toLightCopy
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
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
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepo: AppointmentRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentScreenState())
    val state: StateFlow<AppointmentScreenState> = _state.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            userSessionManager.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun selectDate(date: LocalDate?) {
        _state.update { it.copy(selectedDate = date) }
    }

    fun bookAppointment(listing: PropertyListing) = viewModelScope.launch {
        val user = _currentUser.value ?: return@launch
        val date = _state.value.selectedDate ?: return@launch
        val agent = listing.agent ?: return@launch

        _state.update { it.copy(isLoading = true, resultMessage = null, success = false) }

        // Creiamo la versione leggera
        val lightProperty = listing.toLightCopy()

        val appointmentRequest = AppointmentRequest(
            listing = lightProperty,
            user = user,
            agent = agent,
            date = date
        )

        val result = appointmentRepo.bookAppointment(appointmentRequest)
        if (result is ApiResult.Success) {
            _state.update {
                it.copy(isLoading = false, success = true, resultMessage = "Appuntamento prenotato!")
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = false,
                    success = false,
                    resultMessage = result.message ?: "Errore durante la prenotazione"
                )
            }
        }
    }



    fun resetResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false) }
    }
}