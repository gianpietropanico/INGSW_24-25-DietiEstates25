package com.example.ingsw_24_25_dietiestates25

data class AuthState(
    val signInEmail: String = "",
    val signInPassword: String = "",
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateTo: String? = null
)