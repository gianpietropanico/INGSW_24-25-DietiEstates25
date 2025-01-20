package com.example.ingsw_24_25_dietiestates25.data.auth

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