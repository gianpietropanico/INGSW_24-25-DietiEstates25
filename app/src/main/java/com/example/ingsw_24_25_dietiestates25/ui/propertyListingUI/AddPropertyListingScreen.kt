package com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI

import com.example.ingsw_24_25_dietiestates25.R

import android.location.Geocoder
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import com.example.ingsw_24_25_dietiestates25.ui.propertyListingUI.PropertyListingViewModel.AddPropertyListingUiState
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ingsw_24_25_dietiestates25.ui.utils.rememberImagePicker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.ingsw_24_25_dietiestates25.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Type


@Composable
fun AddPropertyListingScreen(
    plm: PropertyListingViewModel,
    navController: NavController,
    onBack: () -> Unit
) {

    val currentUser by plm.user.collectAsState()
    val uiState by plm.uiState.collectAsState()

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val pickImage = rememberImagePicker { uri -> uri?.let { imageUris = imageUris + it } }

    val scrollState = rememberScrollState()

    // Stati dei campi
    val title by plm.title.collectAsState()
    val type by plm.type.collectAsState()
    val price by plm.price.collectAsState()
    val rooms by plm.numberOfRooms.collectAsState()
    val bathrooms by plm.numberOfBathrooms.collectAsState()
    val size by plm.size.collectAsState()
    val energyClass by plm.energyClass.collectAsState()
    val parking by plm.parking.collectAsState()
    val garden by plm.garden.collectAsState()
    val elevator by plm.elevator.collectAsState()
    val gatehouse by plm.gatehouse.collectAsState()
    val balcony by plm.balcony.collectAsState()
    val roof by plm.roof.collectAsState()
    val ac by plm.airConditioning.collectAsState()
    val heating by plm.heatingSystem.collectAsState()
    val description by plm.description.collectAsState()
    val lat by plm.latitude.collectAsState()
    val lng by plm.longitude.collectAsState()

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
        // Back button
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(Modifier.height(12.dp))

        // Listing Type
        Text("Listing Type", fontWeight = FontWeight.Bold)
        Row {
            TypeToggle("Rent", type == Type.A) { plm.type.value = Type.A }
            Spacer(Modifier.width(8.dp))
            TypeToggle("Sell", type == Type.B) { plm.type.value = Type.B }
        }

        Spacer(Modifier.height(12.dp))

        // Title
        OutlinedTextField(
            value = title,
            onValueChange = { plm.title.value = it },
            label = { Text("Property Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Location Map
        Text("Location", fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState
            ) {
                val context = LocalContext.current
                val markerState = rememberMarkerState(
                    position = LatLng(lat.toDoubleOrNull() ?: 0.0, lng.toDoubleOrNull() ?: 0.0)
                )

                LaunchedEffect(markerState.position) {
                    val position = markerState.position
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1)

                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        plm.latitude.value = position.latitude.toString()
                        plm.longitude.value = position.longitude.toString()
                        plm.city.value = address.locality ?: ""
                        plm.province.value = address.subAdminArea ?: ""
                        plm.street.value = address.thoroughfare ?: ""
                        plm.civicNumber.value = address.subThoroughfare ?: ""
                        plm.cap.value = address.postalCode ?: ""
                        plm.country.value = address.countryName ?: ""
                    }
                }

                Marker(
                    state = markerState,
                    draggable = true
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Photos
        Text("Property photos", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            imageUris.forEach { uri ->
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.default_house) ,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { imageUris = imageUris - uri },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
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
                Icon(Icons.Default.Add, contentDescription = "Add photo")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Property Features
        Text("Property features", fontWeight = FontWeight.Bold)
        CounterRow("Rooms", rooms) { plm.numberOfRooms.value = it.toString() }
        CounterRow("Bathroom", bathrooms) { plm.numberOfBathrooms.value = it.toString() }
        OutlinedTextField(
            value = size,
            onValueChange = { plm.size.value = it },
            label = { Text("Size (m²)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        EnergyClassDropdown(
            plm = plm
        )

        Spacer(Modifier.height(12.dp))

        // Facilities
        Text("Facilities", fontWeight = FontWeight.Bold)
        FacilityChip("Parking", parking) { plm.parking.value = !parking }
        FacilityChip("Garden", garden) { plm.garden.value = !garden }
        FacilityChip("Elevator", elevator) { plm.elevator.value = !elevator }
        FacilityChip("Gatehouse", gatehouse) { plm.gatehouse.value = !gatehouse }
        FacilityChip("Balcony", balcony) { plm.balcony.value = !balcony }
        FacilityChip("Roof", roof) { plm.roof.value = !roof }
        FacilityChip("A/C", ac) { plm.airConditioning.value = !ac }
        FacilityChip("Heating system", heating) { plm.heatingSystem.value = !heating }

        Spacer(Modifier.height(16.dp))

        // Price
        OutlinedTextField(
            value = price,
            onValueChange = { plm.price.value = it },
            label = { Text(if (type == Type.B) "Sell price" else "Rent price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { plm.description.value = it },
            label = { Text("Property description") },
            modifier = Modifier.height(120.dp)
        )

        Spacer(Modifier.height(20.dp))

        val context = LocalContext.current

        // Publish button
        Button(
            onClick = { plm.addPropertyListing(currentUser?.email ?: "", imageUris, context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publish", color = Color.White)
        }
    }

    // UI State
    when (uiState) {
        is PropertyListingViewModel.AddPropertyListingUiState.Loading -> CircularProgressIndicator()
        is PropertyListingViewModel.AddPropertyListingUiState.Success -> Text(
            "Immobile aggiunto con successo!",
            color = MaterialTheme.colorScheme.primary
        )
        is PropertyListingViewModel.AddPropertyListingUiState.Error -> Text(
            text = (uiState as PropertyListingViewModel.AddPropertyListingUiState.Error).message,
            color = MaterialTheme.colorScheme.error
        )
        else -> {}
    }
}

@Composable
fun TypeToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF4FC3F7) else Color.LightGray
        )
    ) {
        Text(label)
    }
}
@Composable
fun CounterRow(label: String, value: String, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(label)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onValueChange((value.toIntOrNull() ?: 0).coerceAtLeast(1) - 1) }) {
                Text("-")
            }
            Text(value, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
            IconButton(onClick = { onValueChange((value.toIntOrNull() ?: 0) + 1) }) {
                Text("+")
            }
        }
    }
}

@Composable
fun FacilityChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
fun EnergyClassDropdown(plm: PropertyListingViewModel) {
    val energyState by plm.energyClass.collectAsState()  // ora EnergyClass
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = energyState.label,  // usa label direttamente
            onValueChange = {},
            readOnly = true,
            label = { Text("Energy Class") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            EnergyClass.values().forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        plm.energyClass.value = option
                        expanded = false
                    }
                )
            }
        }
    }
}