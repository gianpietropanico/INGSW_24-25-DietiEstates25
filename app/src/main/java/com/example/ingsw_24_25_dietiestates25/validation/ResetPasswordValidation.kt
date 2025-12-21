package com.example.ingsw_24_25_dietiestates25.validation

class ResetPasswordValidation {
    fun validateResetPassword(oldPassword: String?, newPassword: String?): Boolean {

        if (oldPassword == null || newPassword == null)
            throw IllegalArgumentException()

        if (oldPassword.isBlank() || newPassword.isBlank())
            return false

        if (newPassword.length < 8)
            return false

        if (!newPassword.any { it.isDigit() }) return false
        if (!newPassword.any { it.isUpperCase() }) return false
        if (!newPassword.any { "_.()$&@".contains(it) }) return false

        if (oldPassword == newPassword)
            return false

        return true
    }
}