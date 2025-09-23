package com.example.ingsw_24_25_dietiestates25.ui.authUI

data class AuthState(
    val email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    val state: String? = null,
    val jwtToken: String? = null,

    val isAuthenticated: Boolean? = false,
    val isLoading: Boolean = false,
    var resultMessage: String? = null,
    var localError: Boolean = false,
    val success: Boolean = false

)