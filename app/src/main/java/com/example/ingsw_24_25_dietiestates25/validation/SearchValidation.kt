package com.example.ingsw_24_25_dietiestates25.validation

class SearchValidation {
    private val allowedTypes = setOf("SELL", "RENT")

    fun validateSearch(type: String?, location: String?): Boolean {

        if (type == null || location == null)
            throw IllegalArgumentException()

        if (type.isBlank() || location.isBlank())
            return false

        if (!allowedTypes.contains(type))
            return false

        return true
    }
}