package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Offer
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferStatus
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import java.util.Locale
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.DarkRed
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay

@Composable
fun OfferChatScreen(
    inboxVm :InboxViewModel,
    navController: NavController
) {

    val state by inboxVm.state.collectAsState()
    val currentUser = inboxVm.getCurrentUser()

    when {
        (state.selectedOffer == null) -> {
            Log.d("OFFERCHATSCREEN","Offer is null")
            LoadingOverlay(true)
        }
        else -> {
            val isDisabled = state.selectedOffer!!.messages.last().status == OfferStatus.ACCEPTED
            val offerUser = if(state.selectedOffer!!.buyerUser.username == currentUser!!.username) state.selectedOffer!!.agent else state.selectedOffer!!.buyerUser

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(62.dp)
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.popBackStack() }
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable( onClick = {})//TODO
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            val picture = offerUser.profilePicture
                            if (picture != null) {
                                Image(
                                    bitmap = bse64ToImageBitmap(picture),
                                    contentDescription = "Property Picture",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.defaultprofilepic),
                                    contentDescription = "Default Property Picture",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Text(
                            text = offerUser.username,
                            style = MaterialTheme.typography.titleLarge .copy(fontWeight = FontWeight.Bold)
                        )
                    }



                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth().clickable(onClick = {}),// TODO
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(73.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF006666)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!state.selectedOffer!!.listing.property.images.isEmpty()) {
                                AsyncImage(
                                    model = state.selectedOffer!!.listing.property.images.first(),
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
//                            if (!state.selectedOffer!!.listing.property.images.isEmpty()) {
//                                Image(
//                                    bitmap = bse64ToImageBitmap(state.selectedOffer!!.listing.property.images.first()),
//                                    contentDescription = "Profile Picture",
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .clip(RectangleShape),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }else{
//                                Image(
//                                    painter = painterResource(id = R.drawable.default_house),
//                                    contentDescription = "Profile Picture",
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .clip(RectangleShape),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }


                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.selectedOffer!!.listing.title,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                text = state.selectedOffer!!.listing.property.street ,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                text = "${state.selectedOffer!!.listing.price.toInt()} €",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                                color = Color.Gray
                            )
                        }
                    }


//                    HorizontalDivider(
//                        color = Color.Gray,
//                        thickness = 1.dp,
//                        modifier = Modifier.padding(vertical = 16.dp)
//                    )

                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(17.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {

                    OutlinedButton(
                        onClick = { /* TODO */ },
                        enabled = !isDisabled,
                        modifier = Modifier
                            .width(172.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(2.dp),
                        border = if (!isDisabled) BorderStroke(1.dp, AscientGradient)
                        else BorderStroke(1.dp, Color.LightGray),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Arrange a meeting",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                brush = if (!isDisabled) AscientGradient
                                else Brush.linearGradient(listOf(Color.Gray, Color.Gray))
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(172.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (!isDisabled) AscientGradient
                                else Brush.linearGradient(listOf(Color.LightGray, Color.LightGray))
                            )
                            .clickable(enabled = !isDisabled) {
                                navController.navigate(NavigationItem.MakeOffer.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Make your offer",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ) ,
                            color = if (!isDisabled) Color.White
                            else Color.Gray
                        )
                    }
                }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.selectedOffer!!.messages) { message ->
                        OfferCardMessage(
                            message = message,
                            currentUser = currentUser,
                            offerUser = offerUser,
                            offer = state.selectedOffer!!
                        )
                    }


                    if (isDisabled) {
                        item {
                            Spacer(Modifier.height(24.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF2F2F2))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Congratulations !!",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    val text = if(state.selectedOffer!!.messages.last().sender.username == currentUser.username) "You accepted the offer"
                                    else "The ${offerUser.username} accepted your offer"
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Gray
                                        )
                                    )
                                }


                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF2F2F2))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Complete your journey",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = "Thank you for using DietiEstates. Now arrange a meeting to see your future home.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Gray
                                        )
                                    )

                                    Spacer(Modifier.height(16.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(AscientGradient)
                                            .clickable { /* inboxVm.acceptOffer(true) TODO */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Arrange a meeting",
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }


            }


        }
    }
}

    @Composable
    fun OfferCardMessage(
        message : OfferMessage,
        currentUser : User,
        offerUser : User,
        offer: Offer
        //inboxVm: InboxViewModel
    ) {
        val isMine = message.sender.username == currentUser.username
        val bubbleColor = if (isMine) Color(0xFFF2F2F2) else Color.White
        val textColor = when (message.status) {
            OfferStatus.ACCEPTED ->  Color(0xFF2E7D32) // verde accettata
            OfferStatus.REJECTED -> DarkRed // rosso per rifiutata
            else -> Color.Gray// default
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {

            if (!isMine) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 4.dp)
                ) {
                    val picture = offerUser.profilePicture
                    if (picture != null) {
                        Image(
                            bitmap = bse64ToImageBitmap(picture),
                            contentDescription = "User picture",
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomStart)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.defaultprofilepic),
                            contentDescription = "Default picture",
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomStart)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.widthIn(max = 250.dp),
                colors = CardDefaults.cardColors(containerColor = bubbleColor),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    if(isMine){
//                        Text(
//                            text = "You made an offer to ${offer.agent.username} :",
//                            style = MaterialTheme.typography.bodyLarge.copy(
//                                fontWeight = FontWeight.SemiBold,
//                                textDecoration = textDecoration
//                            )
//                        )
//                    }


                    Row {

                        Text(
                            text = "${message.amount!!.toInt()} €",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Black
                            )
                        )

                        Spacer(Modifier.width(10.dp))
                        val secondToLast = offer.messages.getOrNull(offer.messages.size - 2)

                        Text(
                            text = if (secondToLast == null) "${offer.listing.price.toInt()} €" else "${secondToLast.amount!!.toInt()} €",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                Color.Gray,
                                fontWeight = FontWeight.Black,
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }

                    Text(
                        text = returnStatus(message.status!!.name),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = textColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )


                    // Bottoni solo se l'offerta NON è mia e non è già accettata o rifiutata
                    if (!isMine && message.status == OfferStatus.PENDING) {
                        val buttonShape = RoundedCornerShape(4.dp)
                        Column {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .clip(buttonShape)
                                    .background(AscientGradient)
                                    .clickable { /* inboxVm.acceptOffer(true) TODO */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Accept",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(30.dp)
                                        .clip(buttonShape)
                                        .border(1.dp, AscientGradient, buttonShape)
                                        .clickable { /* inboxVm.declineOffer(true) TODO */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Decline",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            brush = AscientGradient
                                        )
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(30.dp)
                                        .clip(buttonShape)
                                        .border(1.dp, AscientGradient, buttonShape)
                                        .clickable { /* inboxVm.makeOffer(false) TODO */ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Offer your price",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            brush = AscientGradient
                                        )
                                    )
                                }
                            }
                        }


                    }
                }

            }
        }
    }

fun returnStatus(status: String): String {
    return when (status) {
        "PENDING" -> "In progress"
        "ACCEPTED" -> "Accepted"
        "REJECTED" -> "Declined"
        else -> "UNKNOW STATE"
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun OfferChatScreenPreview() {
//
//    val dummyCurrentUser = User(
//        id = "U1",
//        username = "Luca Pelle",
//        name = "Luca",
//        surname = "Pelle",
//        email = "luca@gmail.com",
//        role = Role.LOCAL_USER
//    )
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
//    // --- Dummy messages ---
//    val dummyMessages = mutableListOf(
//        OfferMessage(
//            id = "MSG1",
//            sender = dummyCurrentUser,
//            timestamp = System.currentTimeMillis() - (86400000L * 1),
//            amount = 120000.0,
//            status = OfferStatus.PENDING
//        ),
//        OfferMessage(
//            id = "MSG2",
//            sender = dummyAgent,
//            timestamp = System.currentTimeMillis() - (86400000L * 2),
//            amount = 125000.0,
//            status = OfferStatus.ACCEPTED
//        ),
//        OfferMessage(
//            id = "MSG3",
//            sender = dummyAgent,
//            timestamp = System.currentTimeMillis() - (86400000L * 3),
//            amount = 130000.0,
//            status = OfferStatus.REJECTED
//        )
//    )

//    // --- Dummy property ---
//    val dummyProperty = PropertyListing(
//        id = "P1",
//        title = "Trilocale in centro",
//        type = null,
//        price = 180000f,
//        property = com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property(
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
//            energyClass = com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass.B,
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
//    // --- Dummy offer ---
//    val dummyOffer = Offer(
//        id = "O1",
//        listing = dummyProperty,
//        buyerUser = dummyCurrentUser,
//        agent = dummyAgent,
//        messages = dummyMessages
//    )
//
//    // --- Fake navController for preview ---
//    val navController = androidx.navigation.compose.rememberNavController()
//
//    // --- Render screen ---
//    MaterialTheme {
//        OfferChatScreen(
//            navController = navController,
//            offer = dummyOffer,
//            currentUser = dummyCurrentUser
//        )
//    }
//}
