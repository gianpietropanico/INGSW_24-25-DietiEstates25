package com.example.ingsw_24_25_dietiestates25.validation

class AgencyRequestValidation {

    fun validateAgencyRequest(
        agencyName: String?,
        email: String?,
        password: String?,
        confirmPassword: String?
    ): Boolean {

        if (agencyName == null || email == null || password == null || confirmPassword == null)
            throw IllegalArgumentException()

        if (agencyName.isBlank())
            return false

        // Email
        val emailRegex =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
        if (!Regex(emailRegex).matches(email))
            return false

        // Password rules
        if (password.length < 8) return false
        if (!password.any { it.isDigit() }) return false
        if (!password.any { it.isUpperCase() }) return false
        if (!password.any { "_.()$&@".contains(it) }) return false

        if (password != confirmPassword)
            return false

        return true
    }
}