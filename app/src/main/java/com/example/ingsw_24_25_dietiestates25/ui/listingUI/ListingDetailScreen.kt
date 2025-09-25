package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ListingDetailScreen(
    listingId: String,
    listingVm: ListingViewModel,
    navController: NavHostController
) {
    val listing = listingVm.getListingById(listingId)
}