package com.example.ingsw_24_25_dietiestates25.ui

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ResultsViewModel
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.request.PropertySearchRequest
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.Rubik
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.example.ingsw_24_25_dietiestates25.ui.theme.testColor
import com.example.ingsw_24_25_dietiestates25.ui.theme.unselectedFacility
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiColors
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiIcons
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.getPoiBitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.example.ingsw_24_25_dietiestates25.ui.appointmentUI.AppointmentViewModel
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.grayWithOpacity
import com.example.ingsw_24_25_dietiestates25.ui.theme.inActiveTextColor
import com.example.ingsw_24_25_dietiestates25.ui.utils.safeDecodeBase64
import com.google.maps.android.compose.MarkerInfoWindowContent
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    type: String,
    location: String,
    rm: ResultsViewModel,
    listingVm : ListingViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var activeSheet by remember { mutableStateOf<String?>(null) }

    val state by rm.state.collectAsState()


    // Avvio della chiamata quando apro la schermata
    LaunchedEffect(type, location) {
        if (location == "ALL") {
            rm.loadAllProperties()
        } else {
            rm.searchProperties(type, location)
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
       // containerColor = Color.White,
        sheetContainerColor = Color(0xFFF2F2F2),
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            when (activeSheet) {
                "filters" -> FiltersSheet(
                    rm = rm,
                    type = type,
                    location = location,
                    onApply = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }
                )
                "map" -> MapSheet(
                    state.properties,
                    rm = rm,
                    navController = navController
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Search results",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = testColor,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                            .size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.frecciaback),
                            contentDescription = "Back",
                            tint = testColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                activeSheet = "filters"
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filters",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                activeSheet = "map"
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_map),
                                contentDescription = "Map",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            when {
                state.isLoading -> {
                    Text(
                        text = "Loading...",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                state.errorMessage != null -> {
                    Text(
                        text = " ${state.errorMessage}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // titolo Found X estates
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) { append("Found ") }

                                withStyle(
                                    style = SpanStyle(
                                        color = primaryBlu,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) { append("${state.properties.size}") }

                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) { append(" estates") }
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (state.properties.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.attention),
                                        contentDescription = "No results",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(160.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    color = primaryBlu,
                                                    fontFamily = Rubik,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 24.sp
                                                )
                                            ) { append("Search ") }
                                            withStyle(
                                                style = SpanStyle(
                                                    color = primaryBlu,
                                                    fontFamily = Rubik,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 24.sp
                                                )
                                            ) { append("not found") }
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "Sorry, we can’t find the real estates you are looking for. Maybe, a little spelling mistake?",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = Rubik,
                                            fontSize = 18.sp
                                        ),
                                        color = Color(0xFF53587A),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(20.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.properties) { property ->
                                    PropertyItem(property) {
                                        listingVm.setSelectedListing(property)
                                        navController.navigate(NavigationItem.ListingDetail.route)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapSheet(properties: List<PropertyListing>,
             rm: ResultsViewModel,
             navController: NavController
             ) {

    val context = LocalContext.current
    val mapHeight = 250.dp
    val mapProperties = MapUtils.defaultMapProperties(context)
    val uiSettings = MapUtils.defaultUiSettings

    var selectedPoi by remember { mutableStateOf<POI?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Maps",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Rubik
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))


        val cameraPositionState = rememberCameraPositionState {
            // Centro iniziale su Napoli
            position = CameraPosition.fromLatLngZoom(LatLng(40.8522, 14.2681), 12f)
        }

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
                properties.forEach { property ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(property.property.latitude, property.property.longitude)
                        ),
                        title = property.title,
                        snippet = "${property.price} €"
                    )
                }
            }
        }
    }
}









@Composable
fun PropertyItem(
    propertyListing: PropertyListing,
    onClick: () -> Unit

) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column  {

            //val firstImageBitmap = safeDecodeBase64(propertyListing.property.images.firstOrNull())

            if (propertyListing.property.images.isNotEmpty()) {
                AsyncImage(
                    model = propertyListing.property.images.first(),
                    contentDescription = "Property Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.default_house),
                    error = painterResource(id = R.drawable.default_house)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_house),
                    contentDescription = "Default House",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
//            if (firstImageBitmap != null) {
//                Image(
//                    bitmap = firstImageBitmap,
//                    contentDescription = propertyListing.title,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )
//            } else {
//                Image(
//                    painter = painterResource(id = R.drawable.default_house),
//                    contentDescription = "No image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )
//            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x73D9D9D9))
                    .padding(16.dp)
            ) {
                Text(
                    text = propertyListing.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = Rubik
                    ),
                    color = Color.Black
                )

                Text(
                    text = propertyListing.property.city,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = Rubik
                    ),
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                //  icone

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PropertyInfoItem(R.drawable.ic_bed, "${propertyListing.property.numberOfRooms} bedroom")
                    PropertyInfoItem(R.drawable.ic_bathroom, "${propertyListing.property.numberOfBathrooms} bathroom")
                    PropertyInfoItem(R.drawable.ic_home, "${propertyListing.property.size.toInt()} mq")
                }

                Spacer(modifier = Modifier.height(16.dp))

                //  Pulsante gradiente + Prezzo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(brush = AscientGradient)
                            .clickable {
                                // TODO: Azione prenotazione
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Book an appointment",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = Rubik
                            ),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${propertyListing.price.toInt()} $",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = Rubik
                        ),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyInfoItem(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = Rubik),
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersSheet(
    onApply: () -> Unit,
    rm: ResultsViewModel,
    type: String,
    location: String
) {
    var minPrice by remember { mutableStateOf<Float?>(null) }
    var maxPrice by remember { mutableStateOf<Float?>(null) }

    var rooms by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedEnergyClass by remember { mutableStateOf<EnergyClass?>(null) }

    var elevator by remember { mutableStateOf(false) }
    var gatehouse by remember { mutableStateOf(false) }
    var balcony by remember { mutableStateOf(false) }
    var roof by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = Rubik
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SLIDER Prezzo ---
        Text("Price range", fontWeight = FontWeight.Bold)
        RangeSlider(
            value = (minPrice ?: 0f)..(maxPrice ?: 100000f),
            onValueChange = { range ->
                minPrice = if (range.start > 0f) range.start else null
                maxPrice = if (range.endInclusive < 100000f) range.endInclusive else null
            },
            valueRange = 0f..100000f,
            steps = 20
        )

        Text(
            "€${minPrice?.toInt() ?: 0} - €${maxPrice?.toInt() ?: 100000}"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Numero di stanze ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rooms", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { if (rooms > 0) rooms-- }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text("$rooms", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(
                    onClick = { rooms++ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Classe energetica ---
        Text("Energy Class", fontWeight = FontWeight.Bold)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedEnergyClass?.label ?: "Select class",
                onValueChange = {},
                readOnly = true,
                label = { Text("Energy class") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                EnergyClass.values().forEach { energy ->
                    DropdownMenuItem(
                        text = { Text(energy.label) },
                        onClick = {
                            selectedEnergyClass = energy
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Facilities",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Prima riga
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FilterChip(
                label = "Elevator",
                selected = elevator,
                onClick = { elevator = !elevator }
            )
            FilterChip(
                label = "Gatehouse",
                selected = gatehouse,
                onClick = { gatehouse = !gatehouse }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Seconda riga
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FilterChip(
                label = "Balcony",
                selected = balcony,
                onClick = { balcony = !balcony }
            )
            FilterChip(
                label = "Roof",
                selected = roof,
                onClick = { roof = !roof }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(brush = AscientGradient)
                .clickable {
                    val request = PropertySearchRequest(
                        type = type,
                        city = location.takeIf { it.isNotBlank() },
                        minPrice = minPrice,   // slider → Int
                        maxPrice = maxPrice,
                        minRooms = if (rooms > 0) rooms else null,
                        elevator = elevator,
                        gatehouse = gatehouse,
                        balcony = balcony,
                        roof = roof,
                        energyClass = selectedEnergyClass?.label
                    )


                    rm.applyFilters(request)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Apply",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = Rubik
                ),
                color = Color.White
            )
        }
    }
}

// cliccabile
@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .background(
                if (selected) AscientGradient else Brush.linearGradient(
                    listOf(
                        unselectedFacility,
                        unselectedFacility
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}