package com.example.ingsw_24_25_dietiestates25.ui.offerUI
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary

import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Euro
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferStatus
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing

import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient

@Composable
fun MakeOfferScreen(
    inboxVm : InboxViewModel,
    navController : NavController,
    listingVm : ListingViewModel

) {
    var amount by remember { mutableStateOf( "")}
    var customPrice by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableStateOf(2) }
    val state by inboxVm.state.collectAsState()
    val previousRoute = navController.previousBackStackEntry?.destination?.route
    var showHistoryOffers by remember { mutableStateOf(false) }
    var isPermittedAmount by remember { mutableStateOf(if (amount.isNotEmpty()) inboxVm.checkPrice(amount.toDouble()) else true) }

    val listingOffer =  if( state.selectedOffer == null ){
        state.selectedProperty
    }else{
        state.selectedOffer!!.listing
    }

    LaunchedEffect(state.created) {
        if (state.created  ) {
            navController.navigate(NavigationItem.InboxScreen.route)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            inboxVm.clearState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(87.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel",
                tint = bluPerchEcipiace,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        if (previousRoute == NavigationItem.OfferChat.route) {
                            navController.navigate(NavigationItem.InboxScreen.route)
                        } else {
                            navController.popBackStack()
                        }
                    }
            )

            Text(
                text = "Make an offer",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable( onClick = {
                        listingVm.setSelectedListing(state.selectedProperty!!)
                        navController.navigate(NavigationItem.ListingDetail.route)
                    }),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF006666)),
                    contentAlignment = Alignment.Center
                ) {
                    if ( listingOffer!!.property.images.isEmpty()) {
                        AsyncImage(
                            model = listingOffer.property.images.first(),
                            contentDescription = "Property Image",
                            modifier = Modifier.fillMaxSize().clip(RectangleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.default_house),
                            error = painterResource(id = R.drawable.default_house)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.default_house),
                            contentDescription = "Default House",
                            modifier = Modifier.fillMaxSize().clip(RectangleShape),
                            contentScale = ContentScale.Crop
                        )
                    }



                }

                Spacer(modifier = Modifier.width(15.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.selectedProperty!!.title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = state.selectedProperty!!.property.street ,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = "Listed by ${state.selectedProperty!!.agent!!.username}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = "Price of the listing : ${state.selectedProperty!!.price.toInt()} €",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }


            }
            Spacer(modifier = Modifier.height(12.dp))

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val clickBorderColor = bluPerchEcipiace
            val clickContainerColor = Color(0xFFC9EAFF)


            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        if (selectedIndex == 0) clickBorderColor else Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (selectedIndex == 0) clickContainerColor else Color.White
                    )
                    .clickable {
                        selectedIndex = 0
                        amount = inboxVm.calculateDiscount(state.selectedProperty!!.price.toDouble(), 4).toInt()
                            .toString()
                    }
                    .padding(8.dp)
                    .height(65.dp)
            ) {
                Text(
                    text = inboxVm.calculateDiscount(state.selectedProperty!!.price.toDouble(), 4)
                        .toInt()
                        .toString() + " €",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "4% discount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = bluPerchEcipiace
                    )
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        if (selectedIndex == 1) clickBorderColor else Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (selectedIndex == 1) clickContainerColor else Color.White
                    )
                    .clickable {
                        selectedIndex = 1
                        amount = inboxVm.calculateDiscount(state.selectedProperty!!.price.toDouble(), 7).toString()
                    }
                    .padding(8.dp)
                    .height(65.dp)
            ) {
                Text(
                    text = inboxVm.calculateDiscount(state.selectedProperty!!.price.toDouble(), 7)
                        .toInt()
                        .toString() + " €",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "7% discount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = bluPerchEcipiace
                    )
                )
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        if (selectedIndex == 2) clickBorderColor else Color.LightGray,
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        if (selectedIndex == 2) clickContainerColor else Color.White
                    )
                    .clickable {
                        selectedIndex = 2
                        customPrice = true
                        amount = state.selectedProperty!!.price.toInt().toString()
                    }
                    .padding(8.dp)
                    .height(65.dp)
            ) {
                Text(
                    text = "Custom",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Choose your price",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = bluPerchEcipiace
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        if( customPrice ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Column() {
                    Text(
                        text = "Your offer",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Euro,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))



                        Box(modifier = Modifier.fillMaxWidth()) {

                            if(amount.isEmpty()) {

                                Text(
                                    text = "Insert your price",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                )

                            }

                            BasicTextField(
                                value = amount,
                                onValueChange = { newValue ->
                                    amount = newValue
                                    isPermittedAmount = newValue.toDoubleOrNull()?.let {
                                        inboxVm.checkPrice(it)
                                    } ?: false
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = bluPerchEcipiace,
                        modifier = Modifier.fillMaxWidth(1f)
                    )

                    if(!isPermittedAmount && amount.isNotEmpty())
                    Text(
                        text =  "You have to insert a mininum amount of ${inboxVm.calculateDiscount(state.selectedProperty!!.price.toDouble(), 10)}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            color = DarkRed
                        )
                    )
                }



                Spacer(modifier = Modifier.width(8.dp))

            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        val isEnabled = amount.isNotBlank() && amount.toDoubleOrNull() != null
        val displayAmount = amount.toDoubleOrNull()?.toInt()?.toString()?.plus(" €") ?: ""

        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(48.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isEnabled || isPermittedAmount ) AscientGradient
                    else Brush.linearGradient(listOf(Color.LightGray, Color.LightGray))
                )
                .clickable(enabled = isEnabled) {
                    if ( inboxVm.checkPrice(amount.toDouble()) ){

                        if( state.createOffer ){
                            inboxVm.createOffer(amount.toDouble())

                        }else{
                            inboxVm.makeOffer(amount.toDouble())
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (displayAmount.isNotEmpty()) "Offer $displayAmount" else "Offer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isEnabled ) Color.White else Color.DarkGray
            )
        }

        Spacer(Modifier.height(10.dp))


        Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {


                    Text(
                        "This listing has received ${state.historyOffers.size} offers. ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    if(state.historyOffers.isNotEmpty()){
                        Text(
                            text = if (showHistoryOffers) "Hide offer history" else "View offer history",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = bluPerchEcipiace,
                            modifier = Modifier.clickable {
                                showHistoryOffers = !showHistoryOffers
                            }
                        )
                    }


                }

                Spacer(Modifier.height(22.dp))

                if(showHistoryOffers) {


                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(6.dp))
                            //.background(Color(0xFF68C5FF))
                            .background(Color(0xFFC9EAFF))
                            .border(
                                1.dp,
                                Color(0xFFAFE0FF),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(8.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                modifier = Modifier.weight(1.3f)
                            )

                            Text(
                                text = "Offer",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                modifier = Modifier.weight(0.8f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Status",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }

                        //HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(state.historyOffers) { index, offer ->
                                OfferSummaryItem(offer = offer, index = index)
                            }
                        }
                    }
                }
            }
        }
    LoadingOverlay(isVisible = state.isLoading)

    }



@Composable
fun OfferSummaryItem(offer: OfferSummary, index: Int) {
    val backgroundColor =
        if (index % 2 == 0) Color(0xFFAFE0FF)  else Color(0xFFC9EAFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Data offerta
        Text(
            text = offer.timestamp.toDaysAgo(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            ),
            modifier = Modifier.weight(1.3f)
        )

        // Importo
        Text(
            text = "${offer.amount?.toInt() ?: 0} €",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            ),
            modifier = Modifier.weight(0.8f),
            textAlign = TextAlign.Center
        )

        val icon = when (offer.status) {
            OfferStatus.ACCEPTED -> Icons.Default.Check
            OfferStatus.REJECTED -> Icons.Default.Close
            else -> Icons.Default.AccessTime
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = icon,
                contentDescription = offer.status.name,
                tint = Color.DarkGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MakeOfferScreenPreview() {
//
//    val navController = rememberNavController()
//
//    val dummyAgent = User(
//        id = "U2",
//        username = "Agenzia Alfa",
//        name = "Agente",
//        surname = "007",
//        email = "agente007@gmail.com",
//        role = Role.AGENT_USER
//    )
//
//    val dummyProperty = PropertyListing(
//        id = "P1",
//        title = "Trilocale in centro",
//        type = null,
//        price = 180000f,
//        property = Property(
//            city = "Napoli",
//            cap = "80100",
//            country = "Italia",
//            province = "NA",
//            street = "Via Roma",
//            civicNumber = "12",
//            latitude = 40.8518,
//            longitude = 14.2681,
//            numberOfRooms = 3,
//            numberOfBathrooms = 2,
//            size = 90f,
//            energyClass = EnergyClass.B,
//            parking = true,
//            garden = false,
//            elevator = true,
//            gatehouse = false,
//            balcony = true,
//            roof = false,
//            airConditioning = true,
//            heatingSystem = true,
//            description = "Appartamento luminoso con vista panoramica.",
//            images = emptyList()
//        ),
//        agent = dummyAgent
//    )
//
//
//    val now = System.currentTimeMillis()
//
//    val dummyOffers = listOf(
//        OfferSummary(amount = 120000.0, status = OfferStatus.REJECTED, timestamp = now - 3600_000L * 2),   // 2 ore fa
//        OfferSummary(amount = 122500.0, status = OfferStatus.ACCEPTED, timestamp = now - 3600_000L * 24),  // 1 giorno fa
//        OfferSummary(amount = 123000.0, status = OfferStatus.PENDING,  timestamp = now - 3600_000L * 48)   // 2 giorni fa
//    )
//
//
//    MakeOfferScreen(
//        navController = navController,
//        selectedProperty = dummyProperty,
//        historyOffers = dummyOffers
//    )
//}
