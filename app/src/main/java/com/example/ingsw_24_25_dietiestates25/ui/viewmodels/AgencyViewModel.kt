package com.example.ingsw_24_25_dietiestates25.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ingsw_24_25_dietiestates25.data.auth.AgencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AgencyViewModel(private val repository: AgencyRepository) : ViewModel() {

    private val _agencyName = MutableStateFlow("")
    val agencyName: StateFlow<String> = _agencyName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isRegistrationSuccessful = MutableStateFlow(false)
    val isRegistrationSuccessful: StateFlow<Boolean> = _isRegistrationSuccessful

    // Update agency name
    fun updateAgencyName(name: String) {
        _agencyName.value = name
    }

    // Update email
    fun updateEmail(email: String) {
        _email.value = email
    }

    // Function to register an agency
    fun registerAgency() {
        val name = agencyName.value
        val emailValue = email.value

        // Validation
        if (name.isBlank() || emailValue.isBlank()) {
            _errorMessage.value = "All fields must be filled"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            _errorMessage.value = "Invalid email address"
            return
        }

        _errorMessage.value = null
        _isLoading.value = true

        viewModelScope.launch {
            // Simulate API call or registration logic
            kotlinx.coroutines.delay(2000)
            _isLoading.value = false
            _isRegistrationSuccessful.value = true // Change based on the result
        }
    }
}
