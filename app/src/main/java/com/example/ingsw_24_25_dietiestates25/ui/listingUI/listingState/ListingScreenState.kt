package com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState

data class ListingScreenState(
    val uiState: ListingState = ListingState.Idle,
    val formState: ListingFormState = ListingFormState(),
    val myListings: List<PropertyListing> = emptyList(),
    val myListing: PropertyListing? = null
)