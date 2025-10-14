package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.ListingSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertySummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
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
        //Quando l’utente è loggato, carica i suoi appuntamenti
        viewModelScope.launch {
            userSessionManager.currentUser.collect { user ->
                _currentUser.value = user
                if (user != null) loadAppointmentsForUser(user)
            }
        }
    }

    //Seleziona data per prenotazione o visualizzazione
    fun selectDate(date: LocalDate?) {
        _state.update { it.copy(selectedDate = date) }
    }

    //Carica tutti gli appuntamenti dell’utente corrente
    fun loadAppointmentsForUser(user: User) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val result = appointmentRepo.getAppointmentsByUser(user.id)
        when (result) {
            is ApiResult.Success -> {
                val appointments = result.data ?: emptyList()
                val unavailableDates = appointments.map { it.date }.toSet() // ✅ Fix qui

                _state.update {
                    it.copy(
                        isLoading = false,
                        appointments = appointments,
                        unavailableDates = unavailableDates
                    )
                }
            }

            is ApiResult.UnknownError -> {
                _state.update {
                    it.copy(isLoading = false, resultMessage = result.message)
                }
            }

            else -> {
                _state.update {
                    it.copy(isLoading = false, resultMessage = "Errore nel caricamento")
                }
            }
        }
    }

    //Prenotazione appuntamento
    fun bookAppointment(listing: PropertyListing) = viewModelScope.launch {
        val user = _currentUser.value ?: return@launch
        val date = _state.value.selectedDate ?: return@launch
        val agent = listing.agent ?: return@launch

        _state.update { it.copy(isLoading = true, resultMessage = null, success = false) }

        val listingSummary = ListingSummary(
            id = listing.id,
            title = listing.title,
            property = PropertySummary(
                city = listing.property.city,
                street = listing.property.street,
                civicNumber = listing.property.civicNumber
            )
        )

        val appointmentRequest = AppointmentRequest(
            listing = listingSummary,
            user = user,
            agent = agent,
            date = date.toString()
        )

        val result = appointmentRepo.bookAppointment(appointmentRequest)
        if (result is ApiResult.Success) {
            _state.update {
                it.copy(isLoading = false, success = true, resultMessage = "Appuntamento prenotato!")
            }
            loadAppointmentsForUser(user) // aggiorna lista
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