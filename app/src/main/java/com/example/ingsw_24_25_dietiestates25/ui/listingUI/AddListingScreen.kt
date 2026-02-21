package com.example.ingsw_24_25_dietiestates25.ui.listingUI


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingState
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.utils.*

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddPropertyListingScreen(
    listingVm: ListingViewModel,
    navController: NavController
) {

    val context: Context = LocalContext.current
    val state by listingVm.state.collectAsState()
    val formState = state.formState
    val uiState = state.uiState

    var titleTouched by remember { mutableStateOf(false) }
    var descTouched by remember { mutableStateOf(false) }
    var priceTouched by remember { mutableStateOf(false) }
    var sizeTouched by remember { mutableStateOf(false) }

    val trimmedTitle = formState.title.trim()
    val trimmedDesc = formState.description.trim()
    val priceText = formState.price.trim()
    val sizeText = formState.size.trim()

    val titleValid = trimmedTitle.isNotBlank() && trimmedTitle.length >= 4
    val descValid = trimmedDesc.isNotBlank() && trimmedDesc.length >= 10
    val priceValid =
        priceText.isNotEmpty() &&
                priceText.all { it.isDigit() } &&
                priceText.toLongOrNull()?.let { it > 0 } == true
    val sizeValid =
        sizeText.isNotEmpty() &&
                sizeText.all { it.isDigit() } &&
                sizeText.toLongOrNull()?.let { it >= 20 } == true

    val canPublish = titleValid && descValid && priceValid && sizeValid

    val titleError: String? = when {
        trimmedTitle.isBlank() -> "Title is required."
        trimmedTitle.length < 4 -> "Title is too short (min 4 characters)."
        else -> null
    }

    val descError: String? = when {
        trimmedDesc.isBlank() -> "Description is required."
        trimmedDesc.length < 10 -> "Description is too short (min 10 characters)."
        else -> null
    }

    val priceError: String? = when {
        priceText.isEmpty() -> "Price is required."
        priceText.any { !it.isDigit() } -> "Please enter a valid number."
        priceText.toLongOrNull() == null -> "Price must be greater than 0."
        priceText.toLongOrNull() != null && priceText.toLongOrNull()!! <= 0 -> "Price must be greater than 0."
        else -> null
    }

    val sizeError: String? = when {
        sizeText.isEmpty() -> "Size is required."
        sizeText.any { !it.isDigit() } -> "Please enter a valid number."
        sizeText.toLongOrNull() == null -> "Size must be greater than 20."
        sizeText.toLongOrNull() != null && sizeText.toLongOrNull()!! <= 20 -> "Size must be greater than 20"
        else -> null
    }

    // ---- da Document 4: isInPreview = true ----
    val isInPreview = true
    val currentUser by listingVm.currentUser.collectAsState()

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val pickImage = rememberImagePicker { uri ->
        uri?.let { imageUris = imageUris + it }
    }

    val scrollState = rememberScrollState()

    val lat = formState.latitude
    val lng = formState.longitude
    val city = formState.city
    val province = formState.province
    val street = formState.street
    val civicNumber = formState.civicNumber
    val cap = formState.cap
    val country = formState.country
    val maxImages = 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .verticalScroll(scrollState)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = bluPerchEcipiace,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(Modifier.width(85.dp))
            Text("Create Listing", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(30.dp))

        Text("Listing Title", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = formState.title,
            onValueChange = { newValue ->
                if (!titleTouched) titleTouched = true
                listingVm.updateForm { it.copy(title = newValue) }
            },
            modifier = Modifier.width(370.dp),
            shape = RoundedCornerShape(16.dp),
            isError = titleError != null,
            supportingText = {
                if (titleError != null) Text(titleError, color = MaterialTheme.colorScheme.error)
            },
            placeholder = {
                Text(
                    text = "e.g. Modern Loft in Turin",
                    color = if (titleError != null) DarkRed else LocalContentColor.current.copy(alpha = 0.5f)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = DarkRed,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(Modifier.height(23.dp))

        Text("Property description", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = formState.description,
            onValueChange = { newValue ->
                if (!descTouched) descTouched = true
                listingVm.updateForm { it.copy(description = newValue) }
            },
            modifier = Modifier
                .height(120.dp)
                .width(370.dp),
            shape = RoundedCornerShape(16.dp),
            isError = descError != null,
            supportingText = {
                if (descError != null) Text(descError, color = MaterialTheme.colorScheme.error)
            },
            placeholder = {
                Text(
                    text = "Write a short description...",
                    color = if (descError != null) DarkRed else LocalContentColor.current.copy(alpha = 0.5f)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = DarkRed,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(Modifier.height(24.dp))

        Text("Photos of the Listing", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            imageUris.forEach { uri ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(end = 8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { imageUris = imageUris - uri },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Red, CircleShape)
                            .size(20.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                    }
                }
            }

            if (imageUris.size < maxImages) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFBFC4D6).copy(alpha = 0.45f))
                        .border(1.dp, Color(0xFFBFC4D6).copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        .clickable { pickImage() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add photo",
                        tint = Color(0xFF2C2F4A),
                        modifier = Modifier.size(27.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.height(24.dp))

        Text("Location", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_icon),
                contentDescription = null,
                tint = primaryBlueWithOpacity.copy(alpha = 0.95f)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = listOfNotNull(
                    street.takeIf { it.isNotBlank() }?.plus(" $civicNumber"),
                    city.takeIf { it.isNotBlank() },
                    province.takeIf { it.isNotBlank() },
                    cap.takeIf { it.isNotBlank() },
                    country.takeIf { it.isNotBlank() }
                ).joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
        }

        // ---- BLOCCO MAPPA DAL DOCUMENTO 4 ----
        val locationPermissions = if (!isInPreview) {
            rememberMultiplePermissionsState(
                listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
            )
        } else null

        val uiSettings = MapUtils.defaultUiSettings
        val mapProperties = MapUtils.defaultMapProperties(context)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val initialLat = lat.toDoubleOrNull() ?: 40.828886
        val initialLng = lng.toDoubleOrNull() ?: 14.190684

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(initialLat, initialLng), 12f)
        }

        val markerState = rememberMarkerState(position = LatLng(initialLat, initialLng))

        val myLocationSource = object : LocationSource {
            var listener: LocationSource.OnLocationChangedListener? = null
            override fun activate(l: LocationSource.OnLocationChangedListener) { listener = l }
            override fun deactivate() { listener = null }
            fun onLocation(userLocation: Location) { listener?.onLocationChanged(userLocation) }
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    myLocationSource.onLocation(location)
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    markerState.position = userLatLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 15f)
                }
            }
        }

        fun startListeningToLocations() {
            if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) return

            fusedLocationClient.requestLocationUpdates(
                LocationRequest.Builder(2000L).build(),
                locationCallback,
                Looper.getMainLooper()
            )
        }

        var mapHeight by remember { mutableStateOf(250.dp) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(mapHeight)
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                locationSource = myLocationSource,
                onMapClick = { latLng -> markerState.position = latLng }
            ) {
                Marker(state = markerState, draggable = true)
            }

            val rotation by animateFloatAsState(if (mapHeight == 250.dp) 0f else 180f)

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AscientGradient)
                    .clickable { mapHeight = if (mapHeight == 250.dp) 400.dp else 250.dp },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    modifier = Modifier.rotate(rotation),
                    contentDescription = "Toggle map size",
                    tint = Color.White
                )
            }

            FloatingActionButton(
                onClick = {
                    val lp = locationPermissions
                    if (lp == null) return@FloatingActionButton
                    if (lp.allPermissionsGranted) startListeningToLocations()
                    else lp.launchMultiplePermissionRequest()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.locator),
                    contentDescription = "My Location",
                    tint = if (locationPermissions?.allPermissionsGranted == true) Color(0xFF1C73E8) else Color.Gray
                )
            }
        }

        // Geocoder semplice dal Document 4
        LaunchedEffect(markerState.position) {
            val pos = markerState.position
            if (pos.latitude.toString() != formState.latitude || pos.longitude.toString() != formState.longitude) {
                listingVm.updateForm {
                    it.copy(
                        latitude = pos.latitude.toString(),
                        longitude = pos.longitude.toString()
                    )
                }
                val addresses = withContext(Dispatchers.IO) {
                    Geocoder(context, Locale.getDefault()).getFromLocation(
                        pos.latitude, pos.longitude, 1
                    )
                }
                addresses?.firstOrNull()?.let { addr ->
                    listingVm.updateForm {
                        it.copy(
                            city = addr.locality ?: "",
                            province = addr.subAdminArea ?: "",
                            street = addr.thoroughfare ?: "",
                            civicNumber = addr.subThoroughfare ?: "",
                            cap = addr.postalCode ?: "",
                            country = addr.countryName ?: ""
                        )
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        // ---- FINE BLOCCO MAPPA ----

        Spacer(Modifier.height(16.dp))
        Spacer(Modifier.height(16.dp))

        Text("Property features", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        CounterRow(
            label = "Rooms :       ",
            count = formState.numberOfRooms.toIntOrNull() ?: 0,
            onValueChange = { newCount ->
                listingVm.updateForm { it.copy(numberOfRooms = newCount.toString()) }
            }
        )

        Spacer(Modifier.height(12.dp))

        CounterRow(
            label = "Bathrooms : ",
            count = formState.numberOfBathrooms.toIntOrNull() ?: 0,
            onValueChange = { newCount ->
                listingVm.updateForm { it.copy(numberOfBathrooms = newCount.toString()) }
            }
        )

        Spacer(Modifier.height(24.dp))

        EnergyClassRow(
            selectedClass = formState.energyClass,
            onClassSelected = { newClass ->
                listingVm.updateForm { it.copy(energyClass = newClass) }
            }
        )

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Listing Type : ", fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(70.dp))
            TypeToggle(
                label = "Rent",
                selected = formState.type == Type.RENT,
                onClick = { listingVm.updateForm { it.copy(type = Type.RENT) } }
            )
            Spacer(Modifier.width(8.dp))
            TypeToggle(
                label = "Sell",
                selected = formState.type == Type.SELL,
                onClick = { listingVm.updateForm { it.copy(type = Type.SELL) } }
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Property size (m²):", fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(26.dp))
            OutlinedTextField(
                value = formState.size,
                onValueChange = { newValue ->
                    if (!sizeTouched) sizeTouched = true
                    listingVm.updateForm { it.copy(size = newValue) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(370.dp),
                isError = sizeError != null,
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                supportingText = {
                    if (sizeError != null) Text(sizeError, color = MaterialTheme.colorScheme.error)
                },
                placeholder = {
                    Text(
                        text = "e.g. 20",
                        color = if (sizeError != null) DarkRed else LocalContentColor.current.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = DarkRed,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.height(12.dp))

        Text("Facilities", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val chipModifier = Modifier.width(150.dp).height(48.dp)

            FacilityChip(label = "Parking", selected = formState.parking, onClick = { listingVm.updateForm { it.copy(parking = !it.parking) } }, chipModifier = chipModifier)
            Spacer(Modifier.width(20.dp))
            FacilityChip(label = "Garden", selected = formState.garden, onClick = { listingVm.updateForm { it.copy(garden = !it.garden) } }, chipModifier = chipModifier)
            FacilityChip(label = "Elevator", selected = formState.elevator, onClick = { listingVm.updateForm { it.copy(elevator = !it.elevator) } }, chipModifier = chipModifier)
            Spacer(Modifier.width(20.dp))
            FacilityChip(label = "Gatehouse", selected = formState.gatehouse, onClick = { listingVm.updateForm { it.copy(gatehouse = !it.gatehouse) } }, chipModifier = chipModifier)
            FacilityChip(label = "Balcony", selected = formState.balcony, onClick = { listingVm.updateForm { it.copy(balcony = !it.balcony) } }, chipModifier = chipModifier)
            Spacer(Modifier.width(20.dp))
            FacilityChip(label = "Roof", selected = formState.roof, onClick = { listingVm.updateForm { it.copy(roof = !it.roof) } }, chipModifier = chipModifier)
            FacilityChip(label = "A/C", selected = formState.airConditioning, onClick = { listingVm.updateForm { it.copy(airConditioning = !it.airConditioning) } }, chipModifier = chipModifier)
            Spacer(Modifier.width(20.dp))
            FacilityChip(label = "Heating", selected = formState.heatingSystem, onClick = { listingVm.updateForm { it.copy(heatingSystem = !it.heatingSystem) } }, chipModifier = chipModifier)
        }

        Spacer(Modifier.height(24.dp))

        Text(if (formState.type == Type.SELL) "Sell price" else "Rent price", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.price,
            onValueChange = { newValue ->
                if (!priceTouched) priceTouched = true
                listingVm.updateForm { it.copy(price = newValue) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Text("€", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            isError = priceError != null,
            supportingText = {
                if (priceError != null) Text(priceError, color = MaterialTheme.colorScheme.error)
            },
            placeholder = {
                Text(
                    text = "e.g. 750",
                    color = if (priceError != null) DarkRed else LocalContentColor.current.copy(alpha = 0.5f)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = DarkRed,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = if (canPublish) AscientGradient
                    else Brush.linearGradient(colors = listOf(Color.LightGray, Color.LightGray))
                )
                .clickable(enabled = canPublish) {
                    listingVm.addPropertyListing(currentUser, imageUris, context)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Publish",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.rubik_semibold)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    letterSpacing = 0.sp
                ),
                color = if (canPublish) Color.White else Color.Gray
            )
        }

        Spacer(Modifier.height(16.dp))
    }

    when (uiState) {
        is ListingState.Loading -> LoadingOverlay(isVisible = true)
        is ListingState.Success -> {
            Text("Listing added!", color = MaterialTheme.colorScheme.primary)
            LaunchedEffect(uiState) {
                listingVm.resetForm()
                navController.popBackStack()
            }
        }
        is ListingState.Error -> {
            Text(uiState.message, color = MaterialTheme.colorScheme.error)
        }
        else -> {}
    }
}