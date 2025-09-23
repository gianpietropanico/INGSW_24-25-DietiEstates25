package com.example.ingsw_24_25_dietiestates25

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.propertyListingRepo.PropertyListingRepository
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
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
            try {
               // val results = propertyRepo.searchProperties(type, location) //metodo da creare
                _state.value = _state.value.copy(
                    isLoading = false,
                   // properties = results
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Errore durante la ricerca"
                )
            }
        }
    }
}