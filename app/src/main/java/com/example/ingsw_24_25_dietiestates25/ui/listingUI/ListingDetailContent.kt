package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.offerUI.InboxViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.AgentCard


import com.example.ingsw_24_25_dietiestates25.ui.utils.InfoHouse
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiColors
import com.example.ingsw_24_25_dietiestates25.ui.utils.MapUtils.poiIcons
import com.example.ingsw_24_25_dietiestates25.ui.utils.NearbyPlacesSection
import com.example.ingsw_24_25_dietiestates25.ui.utils.PropertyFacilitiesChips

import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.utils.getPoiBitmapDescriptor
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap

import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.String

@Composable
fun ListingDetailContent(
    navController: NavHostController,
    listingVm: ListingViewModel,
    inboxVm : InboxViewModel
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedPoi by remember { mutableStateOf<POI?>(null) }
    val mapHeight = 250.dp

    val state by listingVm.state.collectAsState()


    val selectedListing = state.selectedListing ?: return

    val mapProperties = MapUtils.defaultMapProperties(context)
    val uiSettings = MapUtils.defaultUiSettings

    val latLng = LatLng(selectedListing.property.latitude, selectedListing.property.longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(Modifier.width(90.dp))
                Text("Listing Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(76.dp))
                Icon(
                    imageVector = Icons.Default.IosShare,
                    contentDescription = "Back",
                    tint = bluPerchEcipiace,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {  }
                )
            }


            val imageList = selectedListing.property.images.ifEmpty {
                List(3) {
                    "drawable://default_house"
                }
            }

            val pagerState = rememberPagerState(pageCount = { imageList.size })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                //.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)) // leggermente arrotondato solo in basso
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                ) { page ->
                    val img = imageList[page]
                    if (img.startsWith("drawable://")) {
                        Image(
                            painter = painterResource(id = R.drawable.default_house),
                            contentDescription = "Default image ${page + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        AsyncImage(
                            model = img,
                            contentDescription = "Immagine ${page + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.default_house),
                            error = painterResource(id = R.drawable.default_house)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            activeColor = Color.White,
                            inactiveColor = Color.LightGray.copy(alpha = 0.8f),
                            pageCount = imageList.size
                        )
                    }
                }

            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(selectedListing.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                Text(
                    text = selectedListing.property.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(15.dp))
                InfoHouse(selectedListing.property)
            }


            AgentCard(selectedListing.agent, state.agency)


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
            ) {
                Text(
                    text = "Home facilities : ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(Modifier.height(12.dp))
                PropertyFacilitiesChips(selectedListing.property)

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Location :",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Address Icon",
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${selectedListing.property.street} ${selectedListing.property.civicNumber}, " +
                                "${selectedListing.property.city}, ${selectedListing.property.province}",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium
                    )

                }

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapHeight)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                ) {

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = uiSettings,
                        properties = mapProperties,
                        onMapClick = { selectedPoi = null }
                    ) {
                        Marker(
                            state = MarkerState(position = latLng),
                            title = state.selectedListing?.title
                        )
                        selectedListing.property.pois.forEach { poi ->
                            Marker(
                                state = MarkerState(LatLng(poi.lat, poi.lon)),
                                icon = context.getPoiBitmapDescriptor(
                                    poi.type,
                                    poiIcons,
                                    poiColors
                                ),
                                onClick = {
                                    selectedPoi = poi
                                    true
                                }
                            )
                        }
                    }

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

                Spacer(Modifier.height(30.dp))
                NearbyPlacesSection(selectedListing.property.pois)


            }


        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .align(Alignment.BottomCenter)
                .border(
                    width = 0.2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 6.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.width(1.dp))

            Text(
                text = "€ ${"%,.0f".format(selectedListing.price.toDouble())}",
                color = bluPerchEcipiace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )

            Button(
                onClick = {
                    inboxVm.setOfferScreen(selectedListing)
                    navController.navigate(NavigationItem.OfferChat.route)

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp,
                    hoveredElevation = 10.dp
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Agent Icon",
                    tint = bluPerchEcipiace,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contact Agent",
                    color = bluPerchEcipiace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }

    }

}

//
//@Composable
//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Listing Detail Preview (User)"
//)
//fun PreviewListingDetailContent() {
//    val navController = rememberNavController()
//
//    // --- Mock utente corrente ---
//    val fakeUser = User(
//        id = "1",
//        name = "Giuseppe",
//        email = "giuseppe@example.com",
//        role = Role.LOCAL_USER,
//        username = "PEPPONE",
//        surname = "reitano"
//    )
//
//    // --- Mock agente ---
//    val fakeAgent = User(
//        id = "99",
//        name = "Mario Rossi",
//        email = "mario.rossi@agenzia.it",
//        role = Role.AGENT_USER,
//        username = "TORCI",
//        surname = "DAJE"
//    )
//
//    // --- Mock POI ---
//    val fakePois = listOf(
//        POI(
//            name = "Politecnico di Torino",
//            type = "University",
//            lat = 45.062,
//            lon = 7.662,
//            distance = 250.0
//        ),
//        POI(
//            name = "Bar Centrale",
//            type = "Cafe",
//            lat = 45.063,
//            lon = 7.664,
//            distance = 120.0
//        )
//    )
//
//    // --- Mock immagini ---
//    val fakeImages = listOf(
//        "https://images.unsplash.com/photo-1507089947368-19c1da9775ae",
//        "https://images.unsplash.com/photo-1568605114967-8130f3a36994"
//    )
//
//    // --- Mock property ---
//    val fakeProperty = Property(
//        city = "Torino",
//        cap = "10129",
//        country = "Italia",
//        province = "TO",
//        street = "Corso Duca degli Abruzzi",
//        civicNumber = "24",
//        latitude = 45.062,
//        longitude = 7.662,
//        pois = fakePois,
//        images = fakeImages,
//        numberOfRooms = 3,
//        numberOfBathrooms = 1,
//        size = 85f,
//        energyClass = EnergyClass.B,
//        parking = true,
//        garden = false,
//        elevator = true,
//        gatehouse = false,
//        balcony = true,
//        roof = false,
//        airConditioning = true,
//        heatingSystem = true,
//        description = "Appartamento moderno e luminoso con cucina open-space e vista panoramica."
//    )
//
//    // --- Mock listing completo ---
//    val fakeListing = PropertyListing(
//        id = "1",
//        title = "Appartamento moderno vicino al Politecnico",
//        type = Type.SELL, // enum tuo
//        price = 3000000f,
//        property = fakeProperty,
//        agent = fakeAgent
//    )
//
//    // --- Mostra il composable ---
//    ListingDetailContent(
//        navController = navController,
//        currentUser = fakeUser,
//        propertyListing = fakeListing
//    )
//}