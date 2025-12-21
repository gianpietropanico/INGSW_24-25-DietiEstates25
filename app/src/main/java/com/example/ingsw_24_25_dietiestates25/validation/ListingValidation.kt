package com.example.ingsw_24_25_dietiestates25.validation

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingFormState

class ListingValidation {

    fun validateListing(form: ListingFormState?): Boolean {

        if (form == null)
            throw IllegalArgumentException()

        if (form.title.isBlank())
            return false

        if (form.type != Type.SELL && form.type != Type.RENT)
            return false

        val price = form.price.toFloatOrNull()
        if (price == null || price <= 0)
            return false

        if (form.city.isBlank())
            return false

        return true
    }
}