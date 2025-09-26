package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import android.graphics.Canvas
import androidx.annotation.DrawableRes

import androidx.compose.ui.zIndex


import androidx.core.content.ContextCompat

// Funzione che crea bitmap circolare
fun Context.drawableToCircleBitmap(
    @DrawableRes drawableRes: Int,
    sizeDp: Int = 48,
    circleColor: Int = 0xFF03DAC5.toInt()
): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableRes)
        ?: throw IllegalArgumentException("Drawable not found")

    val sizePx = (sizeDp * resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = android.graphics.Paint()
    paint.isAntiAlias = true
    paint.color = circleColor

    // Disegna cerchio
    canvas.drawCircle(sizePx / 2f, sizePx / 2f, sizePx / 2f, paint)

    // Disegna icona sopra
    drawable.setBounds(0, 0, sizePx, sizePx)
    drawable.draw(canvas)

    return bitmap
}

// Funzione per ottenere BitmapDescriptor con caching
@Composable
fun Context.getPoiBitmapDescriptor(
    poiType: String,
    poiIcons: Map<String, Int>,
    poiColors: Map<String, Int>,
    sizeDp: Int = 24
): com.google.android.gms.maps.model.BitmapDescriptor {
    val cache = remember { mutableMapOf<String, Bitmap>() }

    val bitmap = cache.getOrPut(poiType) {
        drawableToCircleBitmap(
            drawableRes = poiIcons[poiType] ?: R.drawable.ic_default,
            sizeDp = sizeDp,
            circleColor = poiColors[poiType] ?: 0xFF03DAC5.toInt()
        )
    }
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

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

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedPoi by remember { mutableStateOf<POI?>(null) }
    val mapHeight = 250.dp

    // LatLng della proprietà
    val latLng = LatLng(
        propertyListing.property.latitude,
        propertyListing.property.longitude
    )

    // Stato della camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    val poiIcons = mapOf(
        "School" to R.drawable.ic_school,
        "University" to R.drawable.ic_school,
        "Park" to R.drawable.ic_park,
        "Bus Stop" to R.drawable.ic_bus,
        "Restaurant" to R.drawable.ic_restaurant,
        "Hospital" to R.drawable.ic_hospital,
        "Stadium" to R.drawable.ic_stadium,
        "Train Station" to R.drawable.ic_train,
        "Metro" to R.drawable.ic_metro
    )

    val poiColors = mapOf(
        "School" to 0xFF6200EE.toInt(),
        "University" to 0xFF3700B3.toInt(),
        "Park" to 0xFF03DAC5.toInt(),
        "Bus Stop" to 0xFFFF5722.toInt(),
        "Restaurant" to 0xFFFFC107.toInt(),
        "Hospital" to 0xFFE53935.toInt(),
        "Stadium" to 0xFF009688.toInt(),
        "Train Station" to 0xFF795548.toInt(),
        "Metro" to 0xFF607D8B.toInt()
    )

    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
        isTrafficEnabled = true,
        isBuildingEnabled = true
    )

    val uiSettings = MapUiSettings(compassEnabled = true, zoomGesturesEnabled = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
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
                            Text("X", color = Color.Red, modifier = Modifier.clickable { selectedPoi = null })
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
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            propertyListing.property.parking.takeIf { it }?.let { FacilityChip("Parking") }
            propertyListing.property.garden.takeIf { it }?.let { FacilityChip("Garden") }
            propertyListing.property.elevator.takeIf { it }?.let { FacilityChip("Elevator") }
            propertyListing.property.gatehouse.takeIf { it }?.let { FacilityChip("Gatehouse") }
            propertyListing.property.balcony.takeIf { it }?.let { FacilityChip("Balcony") }
            propertyListing.property.roof.takeIf { it }?.let { FacilityChip("Roof") }
            propertyListing.property.airConditioning.takeIf { it }?.let { FacilityChip("A/C") }
            propertyListing.property.heatingSystem.takeIf { it }?.let { FacilityChip("Heating") }
        }

        Spacer(Modifier.height(12.dp))

        // --- NEARBY PLACES ---
        Text("Nearby Places:", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            propertyListing.property.pois.forEach { poi ->
                POIChip("${poi.name} (${poi.type}, ${poi.distance.toInt()} m)")
            }
        }
    }
}



@Composable
fun FacilityChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(brush = AscientGradient)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun POIChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(primaryBlu)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    }
}

