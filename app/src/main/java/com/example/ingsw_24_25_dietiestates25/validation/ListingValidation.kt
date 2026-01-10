package com.example.ingsw_24_25_dietiestates25.validation

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.ValidationResult

class ListingValidator {

    fun validateListing(
        title: String?,
        type: Type?,
        price: Float?,
        property: Property?,
        agent: User?
    ): ValidationResult {

        if (title == null || type == null || price == null)
            return ValidationResult(false, "Missing listing data")

        if (title.isBlank())
            return ValidationResult(false, "Title is required")

        if (type != Type.SELL && type != Type.RENT)
            return ValidationResult(false, "Invalid listing type")

        if (price.isNaN() || price <= 0)
            return ValidationResult(false,"Price must be a valid positive number greater than zero")

        if (property == null)
            return ValidationResult(false, "Property data is required")

        if (agent == null)
            return ValidationResult(false, "Agent is required")

        return ValidationResult(true)
    }
}





















