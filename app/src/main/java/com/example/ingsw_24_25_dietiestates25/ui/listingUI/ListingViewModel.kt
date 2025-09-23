package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentState
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ListingViewModel  @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val imageRepo : ImageRepository,
    private val listingsRepo: PropertyListingRepository,
): ViewModel()  {

    private val _uiState = MutableStateFlow<ListingState>(ListingState.Idle)
    val uiState: StateFlow<ListingState> = _uiState

    var user = userSessionManager.currentUser
    private val _myListings = MutableStateFlow<List<PropertyListing>>(emptyList())
    val myListings: StateFlow<List<PropertyListing>> = _myListings

    // Stati per i campi del form
    val title = MutableStateFlow("")

    val type = MutableStateFlow<Type>(Type.A)  // default Rent
    val price = MutableStateFlow("")
    val city = MutableStateFlow("")
    val cap = MutableStateFlow("")
    val country = MutableStateFlow("")
    val province = MutableStateFlow("")
    val street = MutableStateFlow("")
    val civicNumber = MutableStateFlow("")
    val latitude = MutableStateFlow("")
    val longitude = MutableStateFlow("")
    val numberOfRooms = MutableStateFlow("")
    val numberOfBathrooms = MutableStateFlow("")

    val size = MutableStateFlow("")
    val energyClass = MutableStateFlow<EnergyClass>(EnergyClass.A)
    val parking = MutableStateFlow(false)
    val garden = MutableStateFlow(false)
    val elevator = MutableStateFlow(false)
    val gatehouse = MutableStateFlow(false)
    val balcony = MutableStateFlow(false)
    val roof = MutableStateFlow(false)
    val airConditioning = MutableStateFlow(false)
    val heatingSystem = MutableStateFlow(false)
    val description = MutableStateFlow("")


    fun loadMyListings() {
        viewModelScope.launch {
            user.value?.email?.let { email ->
                val listings = listingsRepo.getPropertiesListingByAgent(email)
                _myListings.value = listings
            }
        }
    }

    fun addPropertyListing(agentEmail: String, imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {

            _uiState.value = ListingState.Loading

            try {
                val property = Property(
                    city = city.value,
                    cap = cap.value,
                    country = country.value,
                    province = province.value,
                    street = street.value,
                    civicNumber = civicNumber.value,
                    latitude = latitude.value.toDoubleOrNull() ?: 0.0,
                    longitude = longitude.value.toDoubleOrNull() ?: 0.0,
                    indicators = emptyList(),
                    propertyPicture = null,
                    numberOfRooms = numberOfRooms.value.toIntOrNull() ?: 0,
                    numberOfBathrooms = numberOfBathrooms.value.toIntOrNull() ?: 0,
                    size = size.value.toFloatOrNull() ?: 0f,
                    energyClass = energyClass.value,
                    parking = parking.value,
                    garden = garden.value,
                    elevator = elevator.value,
                    gatehouse = gatehouse.value,
                    balcony = balcony.value,
                    roof = roof.value,
                    airConditioning = airConditioning.value,
                    heatingSystem = heatingSystem.value,
                    description = description.value
                )

                val listing = PropertyListing(
                    id = "",
                    title = title.value,
                    type = type.value,
                    price = price.value.toFloatOrNull() ?: 0f,
                    property = property,
                    agentEmail = agentEmail
                )

                // Conversione immagini
                val base64Images = imageUris.mapNotNull { uri -> uriToBase64(context, uri) }

                // Salvataggio immagini
                base64Images.forEach { base64 ->
                    imageRepo.insertHouseImages(agentEmail, base64)
                }
                val succes = listingsRepo.addPropertyListing(listing)
                _uiState.value =
                    if (succes) ListingState.Success else ListingState.Error(
                        "Error in the process of saving the listing"
                    )
            } catch (e: Exception) {

                _uiState.value = ListingState.Error("Unknown Error")
            }
        }
    }
    sealed class ListingState{
        object Idle : ListingState()
        object Loading : ListingState()
        object Success : ListingState()
        data class Error(val message: String) : ListingState()
    }
}

