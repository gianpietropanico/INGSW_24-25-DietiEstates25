package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import androidx.compose.foundation.lazy.items


@Composable
fun ListingScreen(
    listingVm: ListingViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        listingVm.loadMyListings()
    }

    val listings by listingVm.myListings.collectAsState(initial = emptyList())


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("PROPERTYLISTING")
            }) {
                Icon(Icons.Default.Add, contentDescription = "PROPERTYLISTING")
            }
        }
    ) { padding ->
        if (listings.isEmpty()) {
            // Schermata vuota
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Nessun annuncio ancora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aggiungi il tuo primo immobile con il + in basso",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Lista con annunci
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(listings) { propertyListing ->
                    PropertyCard(propertyListing) {
                        navController.navigate("propertyDetail/${propertyListing.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyCard(
    propertyListing: PropertyListing,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.height(120.dp)) {
            Image(
                painter = rememberAsyncImagePainter(propertyListing.property.propertyPicture),
                contentDescription = propertyListing.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(150.dp).fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(propertyListing.title, fontWeight = FontWeight.Bold)
                Text("${propertyListing.property.city}, ${propertyListing.property.province}")
                Text("${propertyListing.price} €", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}