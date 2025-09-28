package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingState

import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.theme.cardBackground
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.example.ingsw_24_25_dietiestates25.ui.utils.DietiNavBar
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.Screen
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap


@Composable
fun ListingScreen(
    listingVm: ListingViewModel,
    navController: NavController
) {
    val state by listingVm.state.collectAsState()
    val uiState = state.uiState
    val listings = state.myListings
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    LaunchedEffect(listingVm) {

            listingVm.loadMyListings()

    }

    Scaffold(
        bottomBar = {
            DietiNavBar(
                currentRoute = currentRoute ?: Screen.Home.route,
                onRouteSelected = { newRoute ->
                    navController.navigate(newRoute) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationItem.AddPropertyListings.route)
                },
                containerColor = primaryBlu,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(60.dp)
                    .shadow(elevation = 12.dp, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {

            if (uiState is ListingState.Loading && listings.isEmpty()) {
//                LoadingOverlay(isVisible = true)
                LazyColumn {
                    items(3) {
                        ListingCardShimmer()
                    }
                }
            }

            if (listings.isEmpty() && uiState !is ListingState.Loading) {
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
            } else if (listings.isNotEmpty()) {
                // Lista con annunci
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(listings) { propertyListing ->
                        ListingCard(propertyListing) {
                            navController.navigate("listingDetail/${propertyListing.id}")
                        }
                    }
                }
            }
        }
    }
}