package com.example.ingsw_24_25_dietiestates25.ui.propertyUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.repository.PropertyRepository
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    private val _addResult = MutableStateFlow<Property?>(null)
    val addResult = _addResult.asStateFlow()

    fun addProperty(property: Property) {
        viewModelScope.launch {
            try {
                val added = repository.addProperty(property)
                _addResult.value = added
            } catch (e: Exception) {
                // gestisci errore (es. log o notifiche)
            }
        }
    }
}