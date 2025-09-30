package com.example.ingsw_24_25_dietiestates25.ui.listingUI


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay


@Composable
fun ListingDetailScreen(
    listingVm: ListingViewModel,
    navController: NavHostController,
    inboxVm: InboxViewModel
) {

    val state by listingVm.state.collectAsState()
    val uiState = state.uiState




//    LaunchedEffect(listing) {
//        listing?.id?.let { listingVm.getListingById(it) }
//    }

    when (uiState) {
        is ListingState.Loading -> {
            LoadingOverlay(isVisible = true)
        }

        is ListingState.Success -> {
                ListingDetailContent( navController = navController, listingVm = listingVm, inboxVm = inboxVm )

        }

        is ListingState.Error -> {
            Text(
                text = (uiState as ListingState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        is ListingState.Idle -> {
            // Stato iniziale
        }
    }
}







