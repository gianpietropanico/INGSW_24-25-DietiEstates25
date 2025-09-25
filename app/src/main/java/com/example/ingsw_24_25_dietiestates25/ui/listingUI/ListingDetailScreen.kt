package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@Composable
fun ListingDetailScreen(
    listingId: String,
    listingVm: ListingViewModel,
    navController: NavHostController
) {
    val listing by listingVm.myListing.collectAsState()
    val uiState by listingVm.uiState.collectAsState()


    LaunchedEffect(listingId) {
        listingVm.getListingById(listingId)
    }

    when (uiState) {
        is ListingViewModel.ListingState.Loading -> {

            LoadingOverlay(isVisible = true)
        }

        is ListingViewModel.ListingState.Success -> {
            listing?.let { propertyListing ->
                ListingDetailContent(propertyListing = propertyListing)
            }
        }

        is ListingViewModel.ListingState.Error -> {
            Text(
                text = (uiState as ListingViewModel.ListingState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        is ListingViewModel.ListingState.Idle -> {

        }
    }
}

@Composable
fun ListingDetailContent(propertyListing: PropertyListing) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = propertyListing.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))


        Text("Price: ${propertyListing.price}")
        Text("Rooms: ${propertyListing.property.numberOfRooms}")
        Text("Bathrooms: ${propertyListing.property.numberOfBathrooms}")
        Text("Size: ${propertyListing.property.size} m²")
        Text("Address: ${propertyListing.property.street} ${propertyListing.property.civicNumber}, ${propertyListing.property.city}, ${propertyListing.property.province}, ${propertyListing.property.cap}, ${propertyListing.property.country}")

        Spacer(Modifier.height(12.dp))

        Text("Description:", fontWeight = FontWeight.Bold)
        Text(propertyListing.property.description)

        Spacer(Modifier.height(12.dp))

        Text("Images:", fontWeight = FontWeight.Bold)
        Row {
            propertyListing.property.images.forEach { imageUri ->
                Image(
                    bitmap = bse64ToImageBitmap(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
            }
        }
    }
}