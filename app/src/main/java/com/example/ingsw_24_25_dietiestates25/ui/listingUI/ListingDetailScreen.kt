package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController

@Composable
fun ListingDetailScreen(
    listingId: String,
    listingVm: ListingViewModel,
    navController: NavHostController
) {
    val listing by listingVm.myListing.collectAsState()



    LaunchedEffect(listingId) {
        listingVm.getListingById(listingId)
    }

}