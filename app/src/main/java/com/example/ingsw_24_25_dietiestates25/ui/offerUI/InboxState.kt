package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User

data class InboxState (
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val resultMessage: String? = null,
    val localError: Boolean = false,
    val offers : List<Offer> = emptyList(),
    val offerMessages : List<OfferMessage> = emptyList(),
    val historyOffers : List<OfferSummary> = emptyList(),
    val selectedOffer : Offer? = null,
    val selectedProperty : PropertyListing? = null
)