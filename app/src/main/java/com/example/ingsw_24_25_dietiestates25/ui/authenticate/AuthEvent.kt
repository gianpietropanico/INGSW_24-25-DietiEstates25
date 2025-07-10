package com.example.dieti_estate.ui.authenticate

sealed class AuthEvent {
    data class SignUpEmailChanged(val value: String) : AuthEvent()
    data class SignUpPasswordChanged(val value: String) : AuthEvent()
    object SignUp : AuthEvent()

    data class SignInEmailChanged(val value: String) : AuthEvent()
    data class SignInPasswordChanged(val value: String) : AuthEvent()
    data class SignUpConfirmPasswordChanged(val value: String) : AuthEvent()
    object SignIn : AuthEvent()

}
