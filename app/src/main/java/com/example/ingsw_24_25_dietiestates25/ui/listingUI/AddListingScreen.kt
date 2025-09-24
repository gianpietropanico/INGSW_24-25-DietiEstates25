package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
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
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient

import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlueWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.theme.unselectedFacility
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import com.example.ingsw_24_25_dietiestates25.ui.utils.rememberImagePicker
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
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddPropertyListingScreen(
    listingVm: ListingViewModel,
    navController: NavController
) {

    val currentUser by listingVm.user.collectAsState()
    val uiState by listingVm.uiState.collectAsState()

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImage = rememberImagePicker { uri -> uri?.let { imageUris = imageUris + it } }

    val scrollState = rememberScrollState()

    val title by listingVm.title.collectAsState()
    val type by listingVm.type.collectAsState()
    val price by listingVm.price.collectAsState()
    val rooms by listingVm.numberOfRooms.collectAsState()
    val bathrooms by listingVm.numberOfBathrooms.collectAsState()
    val size by listingVm.size.collectAsState()
    val energyClass by listingVm.energyClass.collectAsState()
    val parking by listingVm.parking.collectAsState()
    val garden by listingVm.garden.collectAsState()
    val elevator by listingVm.elevator.collectAsState()
    val gatehouse by listingVm.gatehouse.collectAsState()
    val balcony by listingVm.balcony.collectAsState()
    val roof by listingVm.roof.collectAsState()
    val ac by listingVm.airConditioning.collectAsState()
    val heating by listingVm.heatingSystem.collectAsState()
    val description by listingVm.description.collectAsState()
    val lat by listingVm.latitude.collectAsState()
    val lng by listingVm.longitude.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(lat.toDoubleOrNull() ?: 0.0, lng.toDoubleOrNull() ?: 0.0), 15f
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Back button finto
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = primaryBlueWithOpacity.copy(alpha = 0.95f),
            modifier = Modifier.size(28.dp).clickable {
                navController.popBackStack()
            }
        )

        Spacer(Modifier.height(12.dp))

        // Listing Type
        Text("Listing Type", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row {
            TypeToggle("Rent", selected = type == Type.A) { listingVm.type.value = Type.A }
            Spacer(Modifier.width(8.dp))
            TypeToggle("Sell", selected = type == Type.B) { listingVm.type.value = Type.B }
        }


        Spacer(Modifier.height(12.dp))

        // Title
        Text("Listing Title", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { listingVm.title.value = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))


        // Location
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
                "Jl. Gerungsari, Bulusan, Kec. Tembalang, Kota Semarang, Jawa Tengah 50277",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // MAPPA CON MARKER E FAB

        Text("Location", fontWeight = FontWeight.Bold)

        val context = LocalContext.current
        val locationPermissions = rememberMultiplePermissionsState(
            listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
        )
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Stato iniziale mappa
        val initialLat = lat.toDoubleOrNull() ?: 40.828886
        val initialLng = lng.toDoubleOrNull() ?: 14.190684
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(initialLat, initialLng), 12f)
        }
        val markerState = rememberMarkerState(position = LatLng(initialLat, initialLng))

        // LocationSource personalizzato
        val myLocationSource = object : LocationSource {
            var listener: LocationSource.OnLocationChangedListener? = null
            override fun activate(l: LocationSource.OnLocationChangedListener) { listener = l }
            override fun deactivate() { listener = null }
            fun onLocation(userLocation: Location) {
                listener?.onLocationChanged(userLocation)
            }
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
            if (ActivityCompat.checkSelfPermission(
                    context, ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, ACCESS_COARSE_LOCATION
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


        var mapHeight by remember { mutableStateOf(250.dp) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(mapHeight)
        ) {
            val mapProperties = MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
                isTrafficEnabled = true,
                isBuildingEnabled = true,
            )
            val uiSettings = MapUiSettings(compassEnabled = true, zoomGesturesEnabled = true)

            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                locationSource = myLocationSource,
                onMapClick = { latLng ->
                    // Sposta il marker al click sulla mappa
                    markerState.position = latLng
                }

            ) {
                Marker(
                    state = markerState,
                    draggable = true
                )
            }
            // Bottone per allargare/ristretta la mappa.
            FloatingActionButton(
                onClick = {
                    mapHeight = if (mapHeight == 250.dp) 400.dp else 250.dp
                },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Toggle map size")
            }

            FloatingActionButton(
                onClick = {
                    if (locationPermissions.allPermissionsGranted) {
                        startListeningToLocations()
                    } else {
                        locationPermissions.launchMultiplePermissionRequest()
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.locator),
                    contentDescription = "My Location",
                    tint = if (locationPermissions.allPermissionsGranted) Color(0xFF1C73E8) else Color.Gray
                )
            }
        }

        // Aggiorna campi indirizzo quando il marker cambia posizione
        LaunchedEffect(markerState.position) {
            val position = markerState.position
            listingVm.latitude.value = position.latitude.toString()
            listingVm.longitude.value = position.longitude.toString()

            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                listingVm.city.value = address.locality ?: ""
                listingVm.province.value = address.subAdminArea ?: ""
                listingVm.street.value = address.thoroughfare ?: ""
                listingVm.civicNumber.value = address.subThoroughfare ?: ""
                listingVm.cap.value = address.postalCode ?: ""
                listingVm.country.value = address.countryName ?: ""
            }
        }

        Spacer(Modifier.height(16.dp))

        // Photos
        Text("Property photos", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            imageUris.forEach { uri ->
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.default_house),
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
            IconButton(
                onClick = pickImage,
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add photo", tint = primaryBlueWithOpacity)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Property features
        Text("Property features", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        CounterRow(
            label = "Rooms",
            count = rooms.toIntOrNull() ?: 0,
            onValueChange = { listingVm.numberOfRooms.value = it.toString() }
        )

        Spacer(Modifier.height(8.dp))

        CounterRow(
            label = "Bathrooms",
            count = bathrooms.toIntOrNull() ?: 0,
            onValueChange = { listingVm.numberOfBathrooms.value = it.toString() }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = size,
            onValueChange = { listingVm.size.value = it },
            label = { Text("Size (m²)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        EnergyClassDropdown(
            listingVm = listingVm
        )

        Spacer(Modifier.height(12.dp))

        Text("Facilities", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Facilities", fontWeight = FontWeight.Bold)
            FacilityChip("Parking", parking) { listingVm.parking.value = !parking }
            FacilityChip("Garden", garden) { listingVm.garden.value = !garden }
            FacilityChip("Elevator", elevator) { listingVm.elevator.value = !elevator }
            FacilityChip("Gatehouse", gatehouse) { listingVm.gatehouse.value = !gatehouse }
            FacilityChip("Balcony", balcony) { listingVm.balcony.value = !balcony }
            FacilityChip("Roof", roof) { listingVm.roof.value = !roof }
            FacilityChip("A/C", ac) { listingVm.airConditioning.value = !ac }
            FacilityChip("Heating system", heating) { listingVm.heatingSystem.value = !heating }
        }

        Spacer(Modifier.height(16.dp))

        // Price
        Text(if (type == Type.B) "Sell price" else "Rent price")
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = {listingVm.price.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Description
        Text("Property description", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { listingVm.description.value = it },
            modifier = Modifier.height(120.dp)
        )

        Spacer(Modifier.height(20.dp))


        // Publish button
        Button(
            onClick = { listingVm.addPropertyListing(currentUser?.email ?: "", imageUris, context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp).background(AscientGradient),
            shape = RoundedCornerShape(25.dp),

            ) {
            Text("Publish")
        }
    }


// UI State
    when (uiState) {
        is ListingViewModel.ListingState.Loading -> LoadingOverlay(isVisible = true)
        is ListingViewModel.ListingState.Success -> Text(
            "Immobile aggiunto con successo!",
            color = MaterialTheme.colorScheme.primary
        )
        is ListingViewModel.ListingState.Error -> Text(
            text = (uiState as ListingViewModel.ListingState.Error).message,
            color = MaterialTheme.colorScheme.error
        )
        else->{}
    }

}

@Composable
fun TypeToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (selected) AscientGradient else Brush.linearGradient(listOf(Color.LightGray, Color.LightGray))
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun SizeRow(size: Int, onSizeChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Size", fontWeight = FontWeight.Medium)

        OutlinedTextField(
            value = size.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { onSizeChange(it) }
            },
            singleLine = true,
            modifier = Modifier.width(60.dp).height(50.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
@Composable
fun CounterRow(
    label: String,
    count: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleButton(
                text = "−",
                onClick = { if (count > 0) onValueChange(count - 1) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            CircleButton(
                text = "+",
                onClick = { onValueChange(count + 1) }
            )
        }
    }
}

@Composable
fun CircleButton(
    text: String,
    backgroundColor: Color = primaryBlueWithOpacity.copy(alpha = 0.95f),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(brush = AscientGradient, shape = CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun FacilityChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .background(
                if (selected) AscientGradient else Brush.linearGradient(listOf(unselectedFacility, unselectedFacility)),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EnergyClassDropdown(listingVm: ListingViewModel) {
    val energyClass by listingVm.energyClass.collectAsState() // StateFlow<EnergyClass>

    EnergyClassRow(
        selectedClass = energyClass,
        onClassSelected = { selected ->
            listingVm.energyClass.value = selected
        }
    )
}

@Composable
fun EnergyClassRow(
    selectedClass: EnergyClass,
    onClassSelected: (EnergyClass) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Energy Class", fontWeight = FontWeight.Medium)

        Box {
            OutlinedTextField(
                value = selectedClass.label,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.width(80.dp).height(50.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                EnergyClass.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onClassSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}