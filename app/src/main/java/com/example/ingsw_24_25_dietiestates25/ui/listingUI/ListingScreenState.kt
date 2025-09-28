package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing

data class ListingScreenState(
    val uiState: ListingState = ListingState.Idle,
    val formState: ListingFormState = ListingFormState(),
    val myListings: List<PropertyListing> = emptyList(),
    val myListing: PropertyListing? = null
)
