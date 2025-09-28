package com.example.ingsw_24_25_dietiestates25.ui.listingUI


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R.attr.rotation
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.theme.unselectedFacility
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils
import com.example.ingsw_24_25_dietiestates25.ui.utils.rememberImagePicker
import com.example.ingsw_24_25_dietiestates25.ui.utils.CircleButton
import com.example.ingsw_24_25_dietiestates25.ui.utils.CounterRow
import com.example.ingsw_24_25_dietiestates25.ui.utils.EnergyClassDropdown
import com.example.ingsw_24_25_dietiestates25.ui.utils.EnergyClassRow
import com.example.ingsw_24_25_dietiestates25.ui.utils.TypeToggle
import com.example.ingsw_24_25_dietiestates25.ui.utils.FacilityChip
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingState


import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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

    val state by listingVm.state.collectAsState()
    val formState = state.formState
    val uiState = state.uiState
    val context = LocalContext.current

    val currentUser by listingVm.currentUser.collectAsState()


    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }


    val pickImage = rememberImagePicker { uri ->
        uri?.let { imageUris = imageUris + it }
    }

    val scrollState = rememberScrollState()

    // Recupero valori dal ViewModel
//    val title = formState.title
//    val type = formState.type
//    val price = formState.price
//    val rooms = formState.numberOfRooms
//    val bathrooms = formState.numberOfBathrooms
//    val size = formState.size
//    val energyClass = formState.energyClass
//    val parking = formState.parking
//    val garden = formState.garden
//    val elevator = formState.elevator
//    val gatehouse = formState.gatehouse
//    val balcony = formState.balcony
//    val roof = formState.roof
//    val ac = formState.airConditioning
//    val heating = formState.heatingSystem
//    val description = formState.description
    val lat = formState.latitude
    val lng = formState.longitude
    val city = formState.city
    val province = formState.province
    val street = formState.street
    val civicNumber = formState.civicNumber
    val cap = formState.cap
    val country = formState.country

    // Stato della camera della mappa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(lat.toDoubleOrNull() ?: 0.0, lng.toDoubleOrNull() ?: 0.0),
            15f
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
            .padding(WindowInsets.statusBars.asPaddingValues())
            .verticalScroll(scrollState)
    ) {

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = primaryBlueWithOpacity.copy(alpha = 0.95f),
            modifier = Modifier
                .size(28.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(Modifier.height(12.dp))


        Text("Listing Type", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        Row {
            TypeToggle("Rent", selected = formState.type == Type.RENT) {
                listingVm.updateForm { it.copy(type = Type.RENT) }
            }
            Spacer(Modifier.width(8.dp))

            TypeToggle("Sell", selected = formState.type == Type.SELL) {
                listingVm.updateForm { it.copy(type = Type.SELL) }
            }
        }
        Spacer(Modifier.height(12.dp))

        Text("Listing Title", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.title,
            onValueChange = { newValue ->
                listingVm.updateForm { it.copy(title = newValue) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))


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


        val context = LocalContext.current


        val locationPermissions = rememberMultiplePermissionsState(
            listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
        )

        val uiSettings = MapUtils.defaultUiSettings
        val mapProperties = MapUtils.defaultMapProperties(context)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Stato iniziale della mappa
        val initialLat = lat.toDoubleOrNull() ?: 40.828886
        val initialLng = lng.toDoubleOrNull() ?: 14.190684

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(initialLat, initialLng), 12f)
        }

        val markerState = rememberMarkerState(position = LatLng(initialLat, initialLng))

        // LocationSource personalizzato per la mappa
        val myLocationSource = object : LocationSource {
            var listener: LocationSource.OnLocationChangedListener? = null
            override fun activate(l: LocationSource.OnLocationChangedListener) {
                listener = l
            }

            override fun deactivate() {
                listener = null
            }

            fun onLocation(userLocation: Location) {
                listener?.onLocationChanged(userLocation)
            }
        }

        // Callback per aggiornare posizione reale
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
            if (ActivityCompat.checkSelfPermission(
                    context,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.Builder(2000L).build(),
                locationCallback,
                Looper.getMainLooper()
            )
        }

        // Altezza variabile della mappa
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
                onMapClick = { latLng -> markerState.position = latLng } // sposta marker click
            ) {
                Marker(state = markerState, draggable = true)
            }

            val rotation by animateFloatAsState(if (mapHeight == 250.dp) 0f else 180f)


            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(56.dp) // dimensione FAB
                    .clip(CircleShape)
                    .background(AscientGradient) // qui il gradiente
                    .clickable {
                        mapHeight = if (mapHeight == 250.dp) 400.dp else 250.dp
                    },
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
                    if (locationPermissions.allPermissionsGranted) startListeningToLocations()
                    else locationPermissions.launchMultiplePermissionRequest()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.locator),
                    contentDescription = "My Location",
                    tint = if (locationPermissions.allPermissionsGranted) Color(0xFF1C73E8) else Color.Gray
                )
            }
        }


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
                        pos.latitude,
                        pos.longitude,
                        1
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

        Spacer(Modifier.height(16.dp))


        Text("Property photos", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            imageUris.forEach { uri ->
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
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
                Spacer(Modifier.width(8.dp))
            }


            if (imageUris.size < 2) {
                IconButton(
                    onClick = pickImage,
                    modifier = Modifier
                        .size(80.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add photo",
                        tint = primaryBlueWithOpacity
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        Text("Property features", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        CounterRow(
            label = "Rooms",
            count = formState.numberOfRooms.toIntOrNull() ?: 0,
            onValueChange = { newCount -> listingVm.updateForm { it.copy(numberOfRooms = newCount.toString()) } }
        )

        CounterRow(
            label = "Bathrooms",
            count = formState.numberOfBathrooms.toIntOrNull() ?: 0,
            onValueChange = { newCount -> listingVm.updateForm { it.copy(numberOfBathrooms = newCount.toString()) } }
        )

        Spacer(Modifier.height(12.dp))

        Text("Size", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.size,
            onValueChange = { newValue ->
                listingVm.updateForm { it.copy(size = newValue) }
            },
            label = { Text("Size (m²)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        EnergyClassDropdown(listingVm = listingVm)

        Spacer(Modifier.height(12.dp))

        Text("Facilities", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FacilityChip("Parking", formState.parking) {
                listingVm.updateForm { it.copy(parking = !it.parking) }
            }
            FacilityChip(
                "Garden",
                formState.garden
            ) { listingVm.updateForm { it.copy(garden = !it.garden) } }
            FacilityChip(
                "Elevator",
                formState.elevator
            ) { listingVm.updateForm { it.copy(elevator = !it.elevator) } }
            FacilityChip("Gatehouse", formState.gatehouse) {
                listingVm.updateForm {
                    it.copy(
                        gatehouse = !it.gatehouse
                    )
                }
            }
            FacilityChip(
                "Balcony",
                formState.balcony
            ) { listingVm.updateForm { it.copy(balcony = !it.balcony) } }
            FacilityChip(
                "Roof",
                formState.roof
            ) { listingVm.updateForm { it.copy(roof = !it.roof) } }
            FacilityChip("A/C", formState.airConditioning) {
                listingVm.updateForm {
                    it.copy(
                        airConditioning = !it.airConditioning
                    )
                }
            }
            FacilityChip(
                "Heating system",
                formState.heatingSystem
            ) { listingVm.updateForm { it.copy(heatingSystem = !it.heatingSystem) } }
        }

        Spacer(Modifier.height(16.dp))


        Text(if (formState.type == Type.SELL) "Sell price" else "Rent price")

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.price,
            onValueChange = { newValue -> listingVm.updateForm { it.copy(price = newValue) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))


        Text("Property description", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.description,
            onValueChange = { newValue -> listingVm.updateForm { it.copy(description = newValue) } },
            modifier = Modifier.height(120.dp)
        )

        Spacer(Modifier.height(20.dp))



        Button(
            onClick = {
                listingVm.addPropertyListing(
                    currentUser?.email ?: "",
                    imageUris,
                    context
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(AscientGradient),
            shape = RoundedCornerShape(25.dp)
        ) { Text("Publish") }

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



