package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentStatus
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.request.MessageRequest
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
import kotlin.String

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

    fun getCurrentUser ( ): User?{
        return userSessionManager.getCurrentUser()
    }

    fun returnStatus(status: String): String {
        return when (status) {
            "PENDING" -> "In progress"
            "ACCEPTED" -> "Accepted"
            "REJECTED" -> "Declined"
            else -> "UNKNOW STATE"
        }
    }
    //TETTO MASSIMO 10% che sarebbe 0.1
    fun calculateDiscount(price: Double, discountType: Int): Double {
        val discountPercent = when (discountType) {
            10 -> 0.1
            4 -> 0.04
            7 -> 0.07
            else -> 0.0
        }
        return price - (price * discountPercent)
    }

    fun checkPrice(price: Double): Boolean {
        val discount = calculateDiscount(state.value.selectedProperty!!.price.toDouble(), 10)
        return price >= discount
    }

    fun createOffer (amount : Double){

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result: ApiResult<Offer?> = offerRepo.createOffer(
                OfferRequest(
                    property = state.value.selectedProperty!!,
                    buyerUser = userSessionManager.getCurrentUser()!!,
                    agent = state.value.selectedProperty!!.agent!!,
                    amount = amount
                )
            )

            if (result is ApiResult.Success) {
                handleResult(ApiResult.Created(Unit, result.message))
            } else {
                handleResult(result)
            }
        }
    }

    fun makeOffer (amount: Double){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, resultMessage = null) }

            val result: ApiResult<Unit> = offerRepo.makeOffer(
                MessageRequest(
                    offerId = state.value.selectedOffer!!.id,
                    sender = userSessionManager.getCurrentUser()!!,
                    amount = amount
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

            val result: ApiResult<List<AppointmentSummary>> = appointmentRepo.getAllAppointments()

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

    fun loadAppointmentsForUser(userId : String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val result = appointmentRepo.getAppointmentsByUser(userId)
        when (result) {
            is ApiResult.Success -> {
                val appointments = result.data ?: emptyList()
                //val unavailableDates = appointments.map { it.date }.toSet()

                _state.update {
                    it.copy(
                        isLoading = false,
                        userAppointments = appointments,
                        //unavailableDates = unavailableDates
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

            val result: ApiResult<List<Offer>> = offerRepo.getOffersByUser(user.value!!.username)

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

            val result: ApiResult<List<Appointment>> = appointmentRepo.getAppointmentsByUser(user.value!!.id)

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

            is ApiResult.NotFound<*> -> {
                _state.update { it.copy(isLoading = false, resultMessage = result.message,success = false, localError = true) }
            }
        }
    }

    fun setOfferScreen(property: PropertyListing) {
        viewModelScope.launch {
            _state.update { it.copy(selectedProperty = property) }

            val userId = userSessionManager.getCurrentUser()?.id.toString()

            when (val result = offerRepo.getOffer(property.id, userId)) {


                is ApiResult.Success -> {

                    val offer = result.data

                    println("===== DEBUG OFFER =====")
                    println("offer           = $offer")
                    println("offer?.listing  = ${offer?.listing}")
                    println("offer?.messages = ${offer?.messages}")
                    println("offer?.buyerUser = ${offer?.buyerUser}")
                    println("offer?.agentUser = ${offer?.agentUser}")
                    println("offer?.buyerUser?.username = ${offer?.buyerUser?.username}")
                    println("offer?.agentUser?.username = ${offer?.agentUser?.username}")
                    println("user.value?.username = ${user.value?.username}")
                    println("=========================")

                    val offerUser = if ( result.data!!.buyerUser.username ==  user.value!!.username) result.data.agentUser else result.data.buyerUser
                    _state.update { it.copy(
                        selectedOffer = result.data,
                        selectedProperty = result.data.listing,
                        offerMessages = result.data.messages,
                        selectedOfferUser = offerUser
                        ,createOffer = false) }

                }

                is ApiResult.UnknownError -> {
                    _state.update { it.copy(selectedProperty = property, createOffer = true) }
                }

                is ApiResult.Authorized<*> -> TODO()
                is ApiResult.Created<*> -> TODO()
                is ApiResult.Unauthorized<*> -> TODO()
                is ApiResult.NotFound<*> -> TODO()
            }
        }
    }



    fun offerChatInit (offer : Offer?){

        if(offer == null ) {
            Log.d("OFFERCHATINIT", "error initializing offerchat : offer = null")

        }else {

            viewModelScope.launch {

                val offerUser = if ( offer.buyerUser.username ==  user.value!!.username) offer.agentUser else offer.buyerUser

                _state.update {
                    it.copy(
                        selectedOffer = offer,
                        selectedProperty = offer.listing,
                        offerMessages = offer.messages,
                        selectedOfferUser = offerUser
                    )
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
                val result: ApiResult<Unit> = appointmentRepo.acceptAppointment(appointmentId)
                handleResult(result)

                if (result is ApiResult.Success) {
                    _state.update { current ->
                        current.copy(
                            appointments = current.appointments.map {
                                if (it.id.toString() == appointmentId) it.copy(status = AppointmentStatus.ACCEPTED)
                                else it
                            },
                            selectedAppointment = current.selectedAppointment?.takeIf { it.id.toString() == appointmentId }
                                ?.copy(status = AppointmentStatus.ACCEPTED)
                        )
                    }
                }
            } catch (e: Exception) {
                println("Errore acceptAppointment: ${e.message}")
            }
        }
    }

    fun declineAppointment(appointmentId: String) {
        viewModelScope.launch {
            try {
                val result: ApiResult<Unit> = appointmentRepo.declineAppointment(appointmentId)
                handleResult(result)

                if (result is ApiResult.Success) {
                    _state.update { current ->
                        current.copy(
                            appointments = current.appointments.map {
                                if (it.id.toString() == appointmentId) it.copy(status = AppointmentStatus.REJECTED)
                                else it
                            },
                            selectedAppointment = current.selectedAppointment?.takeIf { it.id.toString() == appointmentId }
                                ?.copy(status = AppointmentStatus.REJECTED)
                        )
                    }
                }
            } catch (e: Exception) {
                println("Error declineAppointment: ${e.message}")
            }
        }
    }

//    fun bookNewAppointment(propertyId: String) {
//        viewModelScope.launch {
//            try {
//                // TODO: aprire schermata calendario o fare chiamata backend
//                println("Prenotato nuovo appuntamento per propertyId = $propertyId")
//            } catch (e: Exception) {
//                println("Errore bookNewAppointment: ${e.message}")
//            }
//        }
//    }

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
    fun setSelectedListing(listing: PropertyListing) {

        _state.update {
            it.copy(selectedProperty = listing)
        }

        Log.d("SETLISTING","${_state.value.selectedProperty}")

    }
    fun clearState (){
        _state.value = InboxState()
    }
}
