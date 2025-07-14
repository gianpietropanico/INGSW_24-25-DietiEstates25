package com.example.dieti_estate.ui.authenticate

data class AuthState(
    val email: String = "",
    val password: String = "",
    var confirmPassword: String = "",
    val state: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val jwtToken: String? = null,
    val isAuthenticated: Boolean? = false

)