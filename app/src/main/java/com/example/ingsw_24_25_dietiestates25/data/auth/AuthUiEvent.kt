package com.example.ingsw_24_25_dietiestates25.data.auth

sealed class AuthUiEvent {
    data class SignUpEmailChanged(val value: String) : AuthUiEvent()
    data class SignUpPasswordChanged(val value: String) : AuthUiEvent()
    object SignUp : AuthUiEvent()

    data class SignInEmailChanged(val value: String) : AuthUiEvent()
    data class SignInPasswordChanged(val value: String) : AuthUiEvent()
    data class SignUpConfirmPasswordChanged(val value: String) : AuthUiEvent()
    object SignIn : AuthUiEvent()



}
