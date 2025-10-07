package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
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
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState
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

    fun createOffer(propertyId: String , agentEmail : String, amount : String){

        if ( user.value!!.role.name.contains("AGENT")) {
            handleResult(ApiResult.Unauthorized<String>("An agent cant propose an offer on listing"))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val agentName = offerRepo.getAgentNameByEmail(agentEmail)

            val result: ApiResult<Offer?> = offerRepo.makeOffer(
                OfferRequest(
                    propertyId = propertyId,
                    buyerName = user.value!!.username,
                    agentName = agentName.data!!,
                    amount = amount.toDouble(),
                    false
                )
            )

            if (result is ApiResult.Success) {
                handleResult(ApiResult.Created(Unit, result.message))
            } else {
                handleResult(result)
            }
        }
    }
    fun makeOffer( propertyId: String , agentEmail : String, amount : String) {

        if(amount.toDoubleOrNull() == null ){
            handleResult(ApiResult.UnknownError<String>("Devi inserire un numero"))
            return
        }

        if( _state.value.createOffer ){
            createOffer(propertyId, agentEmail, amount)
            _state.update { it.copy(createOffer = false) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result: ApiResult<Offer?> = offerRepo.makeOffer(
                OfferRequest(
                    propertyId = propertyId,
                    buyerName = state.value.selectedOffer!!.buyerName,
                    agentName = user.value!!.username,
                    amount = amount.toDouble(),
                    isAgent = user.value!!.role.name.contains("AGENT")
                )
            )

            if (result is ApiResult.Success) {
                handleResult(ApiResult.Created(Unit, result.message))
            } else {
                handleResult(result)
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

    fun acceptOffer(accepted : Boolean){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result : ApiResult<Unit> = if( accepted ){
                offerRepo.acceptOffer(state.value.selectedOffer!!.id)
            }else{
                offerRepo.declineOffer(state.value.selectedOffer!!.id)
            }

            if (result is ApiResult.Success) {

                state.value.offerMessages = offerRepo.loadOfferChat(state.value.selectedOffer!!.id).data!!.messages

                handleResult(ApiResult.Success(Unit, result.message))
            } else {
                handleResult(result = result)
            }
        }
    }

    fun loadOffers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            var result: ApiResult<List<Offer>> = offerRepo.getOffersByUser(user.value!!.username)

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
            is ApiResult.Created -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message, created = true) }
            }
        }
    }

    fun setSelectedProperty (propertyListing : PropertyListing, createOffer :Boolean){

        _state.update { current ->
            current.copy(selectedProperty = propertyListing, createOffer = createOffer)
        }

    }

    fun offerChatInit (offer : Offer?){

        if(offer == null ) {
            Log.d("OFFERCHATINIT", "errore nell'inizializzazione di offerchat : offer = null")
        }else {

            Log.d("OFFERCHATINIT", "INIZIALIZZO")
            viewModelScope.launch {

                _state.update { it.copy(isLoading = true, resultMessage = null) }

                val result = propertyRepo.getListingById(offer.propertyId)

                Log.d("SET SELECTED OFFER ", "${result.data}")

                if (result is ApiResult.Success) {
                    _state.update {
                        it.copy(
                            selectedOffer = offer,
                            selectedProperty = result.data,
                            offerMessages = offer.messages
                        )
                    }

                    handleResult(ApiResult.Success(Unit, result.message))
                } else {
                    handleResult(result)
                }
            }
        }

    }

    fun getUsername( ): String{
        return user.value!!.username
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

    fun setDialogHistoryOffer (showDialog : Boolean){

        if(!showDialog){
            _state.update { current ->
                current.copy(
                    historyOffersDialog = false
                )
            }
        }else{

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, resultMessage = null) }

                val result: ApiResult<List<OfferSummary>> = offerRepo.getOffersSummary(_state.value.selectedProperty!!.id)

                if (result is ApiResult.Success) {
                    _state.update {
                        it.copy(
                            historyOffersDialog = true,
                            historyOffers = result.data ?: emptyList()
                        )
                    }
                    handleResult(ApiResult.Success(Unit, result.message))
                } else {
                    handleResult(result = result)
                }
            }
        }
    }

    fun clearState (){
        _state.value = InboxState()
    }
}
