package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.ui.agentUI.AgentState
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val imageRepo: ImageRepository,
    private val listingsRepo: PropertyListingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingState>(ListingState.Idle)
    val uiState: StateFlow<ListingState> = _uiState

    var user = userSessionManager.currentUser
    private val _myListings = MutableStateFlow<List<PropertyListing>>(emptyList())
    val myListings: StateFlow<List<PropertyListing>> = _myListings

    val _myListing = MutableStateFlow<PropertyListing?>(null)

    val myListing: StateFlow<PropertyListing?> = _myListing

    // Stati per i campi del form
    val title = MutableStateFlow("")

    val type = MutableStateFlow<Type>(Type.RENT)  // default Rent
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

            _uiState.value = ListingState.Loading
            val result = listingsRepo.getPropertiesListingByAgent(user.value!!.email)
            when (result) {
                is ApiResult.Success -> {
                    _myListings.value = result.data ?: emptyList()
                    _uiState.value = ListingState.Idle
                }

                is ApiResult.Unauthorized -> {

                    ListingState.Error(result.message ?: "Accesso non autorizzato")
                }

                is ApiResult.UnknownError -> {

                    ListingState.Error(result.message ?: "Errore sconosciuto")
                }

                else -> {
                    ListingState.Error(result.message ?: "Errore inatteso")
                }
            }

        }
    }


    fun addPropertyListing(agentEmail: String, imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {


            _uiState.value = ListingState.Idle

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
                    pois = emptyList(),
                    images = emptyList(),
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

                listing
                // Conversione immagini
                val base64Images = imageUris.mapNotNull { uri -> uriToBase64(context, uri) }


                val result = listingsRepo.addPropertyListing(listing)


//                val images: List<String> = listOf(
//                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
//                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
//                )
                // Salvataggio immagini
                base64Images.forEach { base64 ->
                    imageRepo.insertHouseImages(result.data!!, base64Images)
                }

                _uiState.value = when (result) {
                    is ApiResult.Success -> {
                        resetForm()
                        ListingState.Success
                    }

                    is ApiResult.Unauthorized -> ListingState.Error(
                        result.message ?: "Non autorizzato"
                    )

                    is ApiResult.UnknownError -> ListingState.Error(
                        result.message ?: "Errore sconosciuto"
                    )

                    else -> ListingState.Error("Errore inatteso")
                }
            } catch (e: Exception) {

                _uiState.value = ListingState.Error("Unknown Error")
            }
        }
    }

    fun updateAddressFromCoordinates(context: Context, lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]

                withContext(Dispatchers.Main) {
                    city.value = address.locality ?: ""
                    province.value = address.subAdminArea ?: ""
                    street.value = address.thoroughfare ?: ""
                    civicNumber.value = address.subThoroughfare ?: ""
                    cap.value = address.postalCode ?: ""
                    country.value = address.countryName ?: ""
                }
            }
        }
    }

    fun setLoading() {
        _uiState.value = ListingState.Loading
    }

    fun setIdle() {
        _uiState.value = ListingState.Idle
    }

    fun resetForm() {
        title.value = ""
        type.value = Type.RENT
        price.value = ""
        city.value = ""
        cap.value = ""
        country.value = ""
        province.value = ""
        street.value = ""
        civicNumber.value = ""
        latitude.value = ""
        longitude.value = ""
        numberOfRooms.value = ""
        numberOfBathrooms.value = ""
        size.value = ""
        energyClass.value = EnergyClass.A
        parking.value = false
        garden.value = false
        elevator.value = false
        gatehouse.value = false
        balcony.value = false
        roof.value = false
        airConditioning.value = false
        heatingSystem.value = false
        description.value = ""
    }

    fun getListingById(id: String) {
        viewModelScope.launch {
            _uiState.value = ListingState.Loading
            val result = listingsRepo.getListingById(id)
            when (result) {
                is ApiResult.Success -> {
                    _myListing.value = result.data
                    _uiState.value = ListingState.Success
                }

                is ApiResult.Unauthorized -> {
                    _uiState.value = ListingState.Error(result.message ?: "Accesso non autorizzato")
                }

                is ApiResult.UnknownError -> {
                    _uiState.value = ListingState.Error(result.message ?: "Errore sconosciuto")
                }

                else -> {
                    _uiState.value = ListingState.Error(result.message ?: "Errore inatteso")
                }
            }
        }
    }


    sealed class ListingState {
        object Idle : ListingState()
        object Loading : ListingState()
        object Success : ListingState()
        data class Error(val message: String) : ListingState()
    }
}


