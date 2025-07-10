package com.example.dieti_estate.ui.authenticate

data class AuthState(
    val signInEmail: String = "",
    val signInPassword: String = "",
    val signUpConfirmPassword: String = "",
    val state: String? = null,
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val jwtToken: String? = null,
    val isAuthenticated: Boolean? = false

)