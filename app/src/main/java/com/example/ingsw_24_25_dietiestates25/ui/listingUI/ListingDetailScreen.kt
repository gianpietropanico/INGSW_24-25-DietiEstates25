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
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay


@Composable
fun ListingDetailScreen(
    listingVm: ListingViewModel,
    navController: NavHostController,
    id: String? = null
) {

    val state by listingVm.state.collectAsState()
    val uiState = state.uiState

    val listing by listingVm.selectedListing.collectAsState()


    LaunchedEffect(Unit) {
        if (listing == null && id != null) {
            listingVm.getListingById(id)
        }
    }

//    when (uiState) {
//        is ListingState.Loading -> {
//            LoadingOverlay(isVisible = true)
//        }
//
//        is ListingState.Success -> {
//            listing?.let { propertyListing ->
//                ListingDetailContent(propertyListing = propertyListing, navController = navController, listingVm = listingVm)
//            }
//        }
//
//        is ListingState.Error -> {
//            Text(
//                text = (uiState as ListingState.Error).message,
//                color = MaterialTheme.colorScheme.error,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        is ListingState.Idle -> {
//            // Stato iniziale
//        }
//    }


    when {
        uiState is ListingState.Loading -> LoadingOverlay(true)

        listing != null -> ListingDetailContent(
            propertyListing = listing!!,
            navController = navController,
            listingVm = listingVm
        )

        uiState is ListingState.Error -> Text(uiState.message)

        else -> Text("Nessun annuncio selezionato")
    }

}







