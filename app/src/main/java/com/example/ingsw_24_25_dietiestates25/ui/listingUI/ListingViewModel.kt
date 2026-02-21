package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.imageRepo.ImageRepository
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.session.UserSessionManager
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingFormState
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingScreenState
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState
import com.example.ingsw_24_25_dietiestates25.ui.utils.uriToBase64WithSizeLimit
import com.example.ingsw_24_25_dietiestates25.validation.ListingValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val imageRepo: ImageRepository,
    private val listingsRepo: PropertyListingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ListingScreenState())
    val state: StateFlow<ListingScreenState> = _state

    private val _myListing = MutableStateFlow<PropertyListing?>(null)
    val myListing: StateFlow<PropertyListing?> = _myListing


    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()


    val energyClass = MutableStateFlow(EnergyClass.A)


    private val listingValidator = ListingValidator()

    init {
        viewModelScope.launch {
            userSessionManager.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }


    fun loadMyListings() = viewModelScope.launch {
        _state.update { it.copy(uiState = ListingState.Loading) }
        val result = listingsRepo.getPropertiesListingByAgent(currentUser.value!!.email)
        handleApiResult(result) { data ->
            _state.update { it.copy(myListings = data ?: emptyList(), uiState = ListingState.Idle) }
        }
    }

    fun addPropertyListing(agent: User?, imageUris: List<Uri>, context: Context) =
        viewModelScope.launch {

            val form = _state.value.formState
            val property = form.toProperty()

            val listingPrice = form.price.toFloatOrNull()

            val validation = listingValidator.validateListing(
                title = form.title,
                type = form.type,
                price = listingPrice,
                property = property,
                agent = agent
            )


            if (!validation.isValid) {
                _state.update {
                    it.copy(
                        uiState = ListingState.Error(
                            validation.errorMessage ?: "Not valid data"
                        )
                    )
                }
                return@launch
            }
            _state.update { it.copy(uiState = ListingState.Loading) }

            try {
//                val property = _state.value.formState.toProperty()
//                val listing = _state.value.formState.toPropertyListing(agent, property)
//
//                val base64Images =
//                    imageUris.mapNotNull { uri -> uriToBase64WithSizeLimit(context, uri) }

                val uploadedUrls = uploadImagesSequentially(imageUris, context)

                val property = _state.value.formState.toProperty().copy(images = uploadedUrls)

                val listing = _state.value.formState.toPropertyListing(agent, property)

                val result = listingsRepo.addPropertyListing(listing)

                if (result is ApiResult.Success && result.data != null) {
//                    base64Images.forEach { base64 ->
//                        imageRepo.insertHouseImages(result.data, base64Images)
//                    }
                    _state.update {
                        it.copy(
                            formState = ListingFormState(),
                            uiState = ListingState.Success
                        )
                    }
                } else if (result is ApiResult.Unauthorized) {
                    _state.update {
                        it.copy(
                            uiState = ListingState.Error(
                                result.message ?: "Unauthorized"
                            )
                        )
                    }
                } else if (result is ApiResult.UnknownError) {
                    _state.update {
                        it.copy(
                            uiState = ListingState.Error(
                                result.message ?: "Unknown Error"
                            )
                        )
                    }
                } else {
                    _state.update { it.copy(uiState = ListingState.Error("Unexpected error")) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(uiState = ListingState.Error("Unknown Error: ${e.message}")) }
            }
        }

//    suspend fun uploadImagesSequentially(imageUris: List<Uri>, context: Context): List<String> {
//        val uploadedUrls = mutableListOf<String>()
//
//        for (uri in imageUris) {
//            return imageRepo.uploadImages(imageUris, context)
//        }
//
//        return uploadedUrls
//    }

    suspend fun uploadImagesSequentially(imageUris: List<Uri>, context: Context): List<String> {
        val uploadedUrls = mutableListOf<String>()

        for (uri in imageUris) {
            val url = imageRepo.uploadImage(uri, context)
            url?.let { uploadedUrls.add(it) } // aggiunge solo se non null
        }

        return uploadedUrls
    }

    fun updateAddressFromCoordinates(context: Context, lat: Double, lng: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            addresses?.firstOrNull()?.let { address ->
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            formState = it.formState.copy(
                                city = address.locality ?: "",
                                province = address.subAdminArea ?: "",
                                street = address.thoroughfare ?: "",
                                civicNumber = address.subThoroughfare ?: "",
                                cap = address.postalCode ?: "",
                                country = address.countryName ?: ""
                            )
                        )
                    }
                }
            }
        }


    fun setSelectedListing(listing: PropertyListing) {

        //QUI DEVO INSERIRE una chiamata alla repository che mi prende che tipo di agenzia è
        viewModelScope.launch(Dispatchers.IO) {
            val agency = listingsRepo.getListingAgency(listing.id)
            Log.d("LISTINGVM GET AGENCY","${agency.data?.name}")

            _state.update {
                it.copy(selectedListing = listing, agency = agency.data)
            }

            Log.d("SETLISTING", "${_state.value.selectedListing}")
        }
    }

    fun resetForm() {
        _state.update { it.copy(formState = ListingFormState()) }
    }

    fun updateForm(update: (ListingFormState) -> ListingFormState) {
        _state.update { it.copy(formState = update(it.formState)) }
    }

    private fun <T> handleApiResult(result: ApiResult<T>, onSuccess: (T?) -> Unit) {
        when (result) {
            is ApiResult.Success -> onSuccess(result.data)
            is ApiResult.Unauthorized -> _state.update {
                it.copy(
                    uiState = ListingState.Error(
                        result.message ?: "Unauthorized access"
                    )
                )
            }

            is ApiResult.UnknownError -> _state.update {
                it.copy(
                    uiState = ListingState.Error(
                        result.message ?: "Unknown Error"
                    )
                )
            }

            else -> _state.update { it.copy(uiState = ListingState.Error("Unexpected error")) }
        }
    }
}


private fun ListingFormState.toProperty() =
    Property(
        city = city,
        cap = cap,
        country = country,
        province = province,
        street = street,
        civicNumber = civicNumber,
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0,
        numberOfRooms = numberOfRooms.toIntOrNull() ?: 0,
        numberOfBathrooms = numberOfBathrooms.toIntOrNull() ?: 0,
        size = size.toFloatOrNull() ?: 0f,
        energyClass = energyClass,
        parking = parking,
        garden = garden,
        elevator = elevator,
        gatehouse = gatehouse,
        balcony = balcony,
        roof = roof,
        airConditioning = airConditioning,
        heatingSystem = heatingSystem,
        pois = emptyList(),
        images = emptyList(),
        description = description
    )

private fun ListingFormState.toPropertyListing(agent: User?, property: Property) =
    PropertyListing(
        id = "",
        title = title,
        type = type,
        price = price.toFloatOrNull() ?: 0f,
        property = property,
        agent = agent
    )