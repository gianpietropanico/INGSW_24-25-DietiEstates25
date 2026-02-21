package com.example.ingsw_24_25_dietiestates25.ui.appointmentUI

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.ListingSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertySummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.request.AppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferAppointmentRequest
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.repository.appointmentRepo.AppointmentRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo.OfferRepository
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
    private val offerRepository: OfferRepository,
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
                val unavailableDates = appointments.map { it.date }.toSet()

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
                    it.copy(isLoading = false, resultMessage = "Error loading")
                }
            }
        }
    }


    fun bookAppointment(listing: PropertyListing) = viewModelScope.launch {

        val user = _currentUser.value ?: return@launch
        val date = _state.value.selectedDate ?: return@launch
        val agent = listing.agent ?: return@launch

        _state.update {
            it.copy(
                isLoading = true,
                resultMessage = null,
                success = false
            )
        }

        val appointmentRequest = AppointmentRequest(
            listing = ListingSummary(
                id = listing.id,
                title = listing.title,
                property = PropertySummary(
                    city = listing.property.city,
                    street = listing.property.street,
                    civicNumber = listing.property.civicNumber,
                    images = listing.property.images
                )
            ),
            user = user,
            agent = agent,
            date = date.toString()
        )

        val resultAppointment = appointmentRepo.bookAppointment(appointmentRequest)

        if (resultAppointment !is ApiResult.Success) {
            _state.update {
                it.copy(
                    isLoading = false,
                    success = false,
                    resultMessage = resultAppointment.message ?: "Error during booking"
                )
            }
            return@launch
        }

        if (offerRepository.getOffersByUser(currentUser.value!!.id).data == null) {
            try {
                val resultOffer = offerRepository.createAppointmentOffer(
                    OfferAppointmentRequest(
                        property = listing,
                        buyerUser = user,
                        agent = agent,
                        appointment = resultAppointment.data!!
                    )
                )

                when (resultOffer) {
                    is ApiResult.Success -> {}
                    is ApiResult.Created -> {}
                    is ApiResult.UnknownError -> {}
                    else -> {}
                }
            } catch (_: Exception) {
            }
        }

        _state.update {
            it.copy(
                isLoading = false,
                success = true,
                resultMessage = "Appointment booked successfully!"
            )
        }

        loadAppointmentsForUser(user)
    }

    fun loadAppointmentsForListing(listingId: String, isForBooking: Boolean) = viewModelScope.launch {
        _state.update { it.copy(isLoadingForBooking = true) }

        val userId = if (!isForBooking) _currentUser.value?.id else null
        val result = appointmentRepo.getAppointmentsByListing(listingId, userId)

        when (result) {
            is ApiResult.Success -> {
                val appointments = result.data ?: emptyList()
                val unavailableDates = appointments.map { it.date }.toSet()

                _state.update {
                    it.copy(
                        isLoadingForBooking = false,
                        appointments = appointments,
                        unavailableDates = unavailableDates
                    )
                }
            }

            is ApiResult.UnknownError -> {
                _state.update { it.copy(isLoadingForBooking = false, resultMessage = result.message) }
            }

            else -> {
                _state.update { it.copy(isLoadingForBooking = false, resultMessage = "Error loading") }
            }
        }
    }
    fun resetResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false) }
    }

    fun clearResult() {
        _state.update { it.copy(resultMessage = null, success = false) }
    }

    fun resetState() {
        _state.value = AppointmentScreenState()
    }
}