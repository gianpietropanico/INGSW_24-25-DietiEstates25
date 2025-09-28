package com.example.ingsw_24_25_dietiestates25.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.ResultsViewModel
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.theme.Rubik
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.example.ingsw_24_25_dietiestates25.ui.theme.testColor
import kotlinx.coroutines.launch
import java.util.Base64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    type: String,
    location: String,
    rm: ResultsViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()


    val state by rm.state.collectAsState()

    //  Avvio della chiamata quando apro la schermata
    LaunchedEffect(type, location) {
        rm.searchProperties(type, location)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        containerColor = Color.White,
        sheetContainerColor = Color.White,
        sheetContent = {
            FiltersSheet(
                onApply = {
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
                onDismiss = {
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Search results",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
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
                            onClick = { /* TODO: Mappa */ },
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
                        text = "❌ ${state.errorMessage}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append("Found ")
                                }


                                withStyle(
                                    style = SpanStyle(
                                        color = primaryBlu,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append("${state.properties.size}")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = Rubik,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append(" estates")
                                }
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )


                        if (state.properties.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
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
                                                    //color = Color(0xFF252B5C),
                                                    fontFamily = Rubik,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 24.sp
                                                )
                                            ) {
                                                append("Search ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    color = primaryBlu,
                                                    //color = Color(0xFF252B5C),
                                                    fontFamily = Rubik,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 24.sp
                                                )
                                            ) {
                                                append("not found")
                                            }
                                        },
                                        style = MaterialTheme.typography.titleMedium, // base style
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

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
                        }else {
                            // risultati della lista
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.properties) { property ->
                                    PropertyItem(property)
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
fun PropertyItem(propertyListing: PropertyListing) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            // 📷 Immagine (usa propertyPicture se c'è, placeholder altrimenti)
            val image = propertyListing.property.images
                // se hai immagini in base64
                val imageBytes = Base64.getDecoder().decode(image.toString())
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = propertyListing.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

            Column(modifier = Modifier.padding(16.dp)) {
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

                //  icone + dati
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bed),
                            contentDescription = "Bedrooms",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${propertyListing.property.numberOfRooms} bedroom")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bathroom),
                            contentDescription = "Bathrooms",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${propertyListing.property.numberOfBathrooms} bathroom")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Size",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${propertyListing.property.size.toInt()} mq")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // pulsante e prezzo

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* TODO: Azione prenotazione */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1688CF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book an appointment")
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
fun FiltersSheet(
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("• Prezzo minimo/massimo", color = Color.Black)
        Text("• Numero stanze", color = Color.Black)
        Text("• Tipo di proprietà", color = Color.Black)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) { Text("Close") }

            Button(
                onClick = onApply,
                modifier = Modifier.weight(1f)
            ) { Text("Apply") }
        }
    }
}
