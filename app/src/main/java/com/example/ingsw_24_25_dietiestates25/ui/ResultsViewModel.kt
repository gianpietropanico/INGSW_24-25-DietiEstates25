package com.example.ingsw_24_25_dietiestates25.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.request.PropertySearchRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState
import com.example.ingsw_24_25_dietiestates25.validation.SearchValidation
import com.example.ingsw_24_25_dietiestates25.validation.SignUpValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ResultsState(
    val isLoading: Boolean = false,
    val properties: List<PropertyListing> = emptyList(),
    val errorMessage: String? = null,
    val selectedListing: PropertyListing? = null,
    val uiState: ListingState = ListingState.Idle,
)

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val propertyRepo: PropertyListingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultsState())
    val state: StateFlow<ResultsState> = _state.asStateFlow()


    private val validator = SearchValidation()
    fun searchProperties(type: String, location: String) {

        val isValid = validator.validateSearch(
            type = type,
            location = location
        )

        if (!isValid) {
            _state.update {
                it.copy(
                    errorMessage = "Dati di ricerca non validi",
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = propertyRepo.searchProperties(type, location)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        properties = result.data ?: emptyList()
                    )
                }
                is ApiResult.UnknownError -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is ApiResult.Unauthorized -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Unknown error"
                    )
                }
            }
        }
    }

    fun loadAllProperties() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = propertyRepo.getAllListings()) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        properties = result.data ?: emptyList()
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Error while loading all properties"
                    )
                }
            }
        }
    }

    fun setSelectedListing(listing: PropertyListing) {
        _state.update {
            it.copy(selectedListing = listing)
        }
    }

    fun applyFilters(request: PropertySearchRequest) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = propertyRepo.searchWithFilters(request)) {
                is ApiResult.Success<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        properties = result.data as? List<PropertyListing> ?: emptyList()
                    )
                }
                is ApiResult.UnknownError<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        properties = emptyList(),
                        errorMessage = null
                    )
                }
                is ApiResult.Unauthorized<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Unauthorized"
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Error while applying filters"
                    )
                }
            }
        }
    }
}