package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.request.OfferRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.data.repository.agentRepo.AgentRepo
import com.example.ingsw_24_25_dietiestates25.data.repository.appointmentRepo.AppointmentRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.authRepo.AuthRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.offerRepo.OfferRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel  @Inject constructor (
    private val userSessionManager: UserSessionManager,
    private val offerRepo : OfferRepository,
    private val appointmentRepo: AppointmentRepository,
    private val propertyRepo : PropertyListingRepository
): ViewModel() {

    private val _state = MutableStateFlow(InboxState())
    val state: StateFlow<InboxState> = _state.asStateFlow()

    var user = userSessionManager.currentUser

    fun clearResultMessage() {
        _state.update { it.copy(resultMessage = null, success = false, localError = false) }
    }

    fun makeOffer( propertyId: String , agentEmail : String, amount : String  ) {

        if(amount.toDoubleOrNull() == null ){
            handleResult(ApiResult.UnknownError<String>("Devi inserire un numero"))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val agentName = offerRepo.getAgentNameByEmail(agentEmail)

            val result: ApiResult<Offer> = offerRepo.makeOffer(
                OfferRequest(
                    propertyId = propertyId,
                    buyerName = user.value!!.username,
                    agentName = agentName.data!!,
                    amount = amount.toDouble()
                )
            )

            if (result is ApiResult.Success) {

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result)
            }
        }
    }

    fun loadHistoryOffers(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<OfferSummary>> = offerRepo.getAllOffers()

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        historyOffers = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }

    fun loadHistoryAppointments(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<AppointmentSummary>> = appointmentRepo.getAllAppointments()

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        historyAppointments = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }


    fun loadOffers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<Offer>> = offerRepo.getOffersByUser(user.value!!.id)

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        offers = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }

    fun loadAppointments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<Appointment>> = appointmentRepo.getAppointmentsByUser(user.value!!.id)

            if (result is ApiResult.Success) {
                _state.update {
                    it.copy(
                        appointments = result.data ?: emptyList()
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }

    private fun handleResult(result: ApiResult<*>) {
        when (result) {
            is ApiResult.Authorized -> {
                _state.update{ it.copy(isLoading = false,success = true, resultMessage = null,localError = false)}
            }

            is ApiResult.Unauthorized -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = false, localError = true) }

            }

            is ApiResult.UnknownError -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message,success = false, localError = true) }
            }

            is ApiResult.Success -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, success = true) }
            }
        }
    }

    fun setSelectedProperty (propertyListing : PropertyListing){
        _state.update { current ->
            current.copy(selectedProperty = propertyListing)
        }

    }

    fun setSelectedOffer (offer : Offer){

        viewModelScope.launch {

            _state.update { it.copy(isLoading = true, resultMessage = null) }
            val result = propertyRepo.getListingById(offer.propertyId)

            if (result is ApiResult.Success) {

                _state.update { current ->
                    current.copy(
                        selectedOffer = offer,
                        selectedProperty = result.data ,
                        offerMessages = offer.messages
                    )
                }

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result)
            }
        }

    }

    fun getChatUser(): String {
        return if( user.value!!.email == state.value.selectedOffer!!.agentName) state.value.selectedOffer!!.buyerName
        else state.value.selectedOffer!!.agentName
    }


    fun setSelectedAppointment (appointment: Appointment){
        _state.update { current ->
            current.copy(selectedAppointment = appointment)
        }
    }

    fun acceptAppointment(appointmentId: String) {
        viewModelScope.launch {
            try {
                // Chiamata al backend tramite repository che restituisce ApiResult<Unit>
                val result: ApiResult<Unit> = appointmentRepo.acceptAppointment(appointmentId)

                // Gestione centralizzata dell'esito
                handleResult(result)

                if (result is ApiResult.Success) {
                    // Aggiorna lo stato locale solo se il backend ha risposto con successo
                    _state.update { current ->
                        current.copy(
                            selectedAppointment = current.selectedAppointment?.copy(
                                appointmentMessages = current.selectedAppointment.appointmentMessages.map {
                                    if (it.id == appointmentId) it.copy(accepted = true) else it
                                }
                            )
                        )
                    }
                    println("Appuntamento accettato con id = $appointmentId")
                }
            } catch (e: Exception) {
                println("Errore acceptAppointment: ${e.message}")
            }
        }
    }

    fun rejectAppointment(appointmentId: String) {
        viewModelScope.launch {
            try {
                val result: ApiResult<Unit> = appointmentRepo.rejectAppointment(appointmentId)

                handleResult(result)

                if (result is ApiResult.Success) {
                    _state.update { current ->
                        current.copy(
                            selectedAppointment = current.selectedAppointment?.copy(
                                appointmentMessages = current.selectedAppointment.appointmentMessages.map {
                                    if (it.id == appointmentId) it.copy(accepted = false) else it
                                }
                            )
                        )
                    }
                    println("Appuntamento rifiutato con id = $appointmentId")
                }
            } catch (e: Exception) {
                println("Errore rejectAppointment: ${e.message}")
            }
        }
    }

    fun bookNewAppointment(propertyId: String) {
        viewModelScope.launch {
            try {
                // TODO: aprire schermata calendario o fare chiamata backend
                println("Prenotato nuovo appuntamento per propertyId = $propertyId")
            } catch (e: Exception) {
                println("Errore bookNewAppointment: ${e.message}")
            }
        }
    }
}
