package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.utils.Chip
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiColors
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiIcons
import com.example.ingsw_24_25_dietiestates25.ui.utils.PropertyFacilitiesChips

import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.getPoiBitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap

import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ListingDetailContent(
    propertyListing: PropertyListing,
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedPoi by remember { mutableStateOf<POI?>(null) }
    val mapHeight = 250.dp

    val mapProperties = MapUtils.defaultMapProperties(context)
    val uiSettings = MapUtils.defaultUiSettings
    // LatLng della proprietà
    val latLng = LatLng(
        propertyListing.property.latitude,
        propertyListing.property.longitude
    )

    // Stato della camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(16.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {

            // --- INFO BASE DELLA PROPRIETÀ ---
            Text(propertyListing.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Type: ${propertyListing.type}")
            Text("Price: ${propertyListing.price}")
            Text("Rooms: ${propertyListing.property.numberOfRooms}")
            Text("Bathrooms: ${propertyListing.property.numberOfBathrooms}")
            Text("Size: ${propertyListing.property.size} m²")
            Text("Energy Class: ${propertyListing.property.energyClass}")
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

            Spacer(Modifier.height(12.dp))

            // --- MAPPA ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    properties = mapProperties,
                    onMapClick = { selectedPoi = null }
                ) {
                    Marker(state = MarkerState(position = latLng), title = propertyListing.title)
                    propertyListing.property.pois.forEach { poi ->
                        Marker(
                            state = MarkerState(LatLng(poi.lat, poi.lon)),
                            icon = context.getPoiBitmapDescriptor(poi.type, poiIcons, poiColors),
                            onClick = {
                                selectedPoi = poi
                                true
                            }
                        )
                    }
                }

                // InfoBox POI
                selectedPoi?.let { poi ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp)
                            .zIndex(1f)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(poi.name, fontWeight = FontWeight.Bold)
                                Text(
                                    "X",
                                    color = Color.Red,
                                    modifier = Modifier.clickable { selectedPoi = null })
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(poi.type)
                            Text("${poi.distance.toInt()} m away")
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- FACILITIES ---
            Text("Facilities:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            PropertyFacilitiesChips(propertyListing.property)

            Spacer(Modifier.height(12.dp))

            // --- NEARBY PLACES ---
            Text("Nearby Places:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                propertyListing.property.pois.forEach { poi ->
                    Chip("${poi.name} (${poi.type}, ${poi.distance.toInt()} m)")
                }
            }
            Spacer(Modifier.height(50.dp))
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // altezza fissa tipo bottom bar
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
                    .background(Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pulsante VENDI
                Button(
                    onClick = {
                        navController.navigate(NavigationItem.MakeOffer.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("OFFRI", color = Color.Black)
                }

                // Divider centrale
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                // Pulsante ACQUISTA
                Button(
                    onClick = { /* TODO: azione acquisto */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ACQUISTA", color = Color.Black)
                }
            }
        }
    }

}