package com.example.ingsw_24_25_dietiestates25.ui.sendEmailFormUI

data class MailSenderState (
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null,
    val localError: Boolean = false,
)