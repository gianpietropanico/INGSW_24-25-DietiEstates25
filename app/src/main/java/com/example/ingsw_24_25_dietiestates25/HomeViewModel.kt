package com.example.ingsw_24_25_dietiestates25

import androidx.lifecycle.ViewModel
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class HomeState(
    val propertyType: String = "Rent",
    val location: String = "",
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onPropertyTypeSelected(type: String) {
        _state.value = _state.value.copy(propertyType = type)
    }

    fun onLocationChanged(newLocation: String) {
        _state.value = _state.value.copy(location = newLocation, errorMessage = null)
    }

    fun onSearchClicked(): Pair<String, String>? {
        val current = _state.value
        return if (current.location.isBlank()) {
            // aggiorna lo stato con errore
            _state.value = current.copy(errorMessage = "Inserisci una località")
            null
        } else {
            // nessun errore, ritorna parametri
            Pair(current.propertyType, current.location)
        }
    }
}