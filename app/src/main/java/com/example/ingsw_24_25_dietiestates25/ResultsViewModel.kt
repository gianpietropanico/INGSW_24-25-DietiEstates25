package com.example.ingsw_24_25_dietiestates25

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.request.PropertySearchRequest
import com.example.ingsw_24_25_dietiestates25.data.model.result.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultsState(
    val isLoading: Boolean = false,
    val properties: List<PropertyListing> = emptyList(),
    val errorMessage: String? = null
)



@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val propertyRepo: PropertyListingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultsState())
    val state: StateFlow<ResultsState> = _state.asStateFlow()

    fun searchProperties(type: String, location: String) {
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
                        errorMessage = "Errore sconosciuto"
                    )
                }
            }
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
                        properties = emptyList(), // ✅ lista vuota, non errore
                        errorMessage = null
                    )
                }
                is ApiResult.Unauthorized<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Non autorizzato"
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Errore durante il filtraggio"
                    )
                }
            }
        }
    }
}

