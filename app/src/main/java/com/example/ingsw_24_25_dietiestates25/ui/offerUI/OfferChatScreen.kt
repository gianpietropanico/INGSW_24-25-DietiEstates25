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
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Appointment
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentStatus
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.AppointmentSummary
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
import java.time.LocalDate

@Composable
fun OfferChatScreen(
    inboxVm :InboxViewModel,
    navController: NavController
) {

    val state by inboxVm.state.collectAsState()
    val currentUser = inboxVm.getCurrentUser()

    LaunchedEffect(Unit) {
        inboxVm.loadAppointmentsForUser( currentUser!!.id)
    }



    when {
        (state.selectedProperty == null && state.selectedOffer == null) -> {
            Log.d("OFFERCHATSCREEN","Offer is null")
            LoadingOverlay(true)
        }
        else -> {

            // questo vuol dire che è l'utente a cliccare sul contatta agente
            // poiche sto cercando di creare uno stato in cui solo l'utnete che vuole comprare
            // puo entrarci , l'agente entra solo se gia esiste questa offerta
            var chatUser: User
            var listingOffer: PropertyListing
            var isDisabled: Boolean
            var messages: List<OfferMessage> = emptyList()
            var lastMessage: OfferMessage

            if( state.selectedOffer != null ){


                messages = state.selectedOffer!!.messages

                lastMessage = messages.lastOrNull()!!

                isDisabled = lastMessage.status == OfferStatus.ACCEPTED

                listingOffer = state.selectedOffer!!.listing


                if(state.selectedOffer!!.buyerUser.username == currentUser!!.username)
                    chatUser = state.selectedOffer!!.agentUser
                else
                    chatUser = state.selectedOffer!!.buyerUser


            }else {
                isDisabled = false
                chatUser = state.selectedProperty!!.agent!!
                listingOffer = state.selectedProperty!!
            }



            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .clickable { navController.popBackStack() }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.Center)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {

                            val picture = chatUser!!.profilePicture
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
                            text = chatUser!!.username,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {}),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF006666)),
                            contentAlignment = Alignment.Center
                        ) {

                            //DIPENDENZA → listing.property.images viene da Offer
                            if (!state.selectedProperty!!.property.images.isEmpty()) {
                                AsyncImage(
                                    model = state.selectedProperty!!.property.images.first(),
                                    contentDescription = "Property Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.default_house),
                                    error = painterResource(id = R.drawable.default_house)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.default_house),
                                    contentDescription = "Default House",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = listingOffer.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                text = listingOffer.property.street,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                text = "${listingOffer.price.toInt()} €",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.Gray
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(17.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            navController.navigate(NavigationItem.BookAppointment.route)
                        },
                        enabled = !isDisabled,
                        modifier = Modifier
                            .width(172.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = if (!isDisabled) BorderStroke(1.dp, AscientGradient)
                        else BorderStroke(1.dp, Color.LightGray),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Arrange a meeting",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                brush = if (!isDisabled) AscientGradient else Brush.linearGradient(
                                    listOf(
                                        Color.Gray,
                                        Color.Gray
                                    )
                                )
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(172.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (!isDisabled) AscientGradient else Brush.linearGradient(
                                    listOf(
                                        Color.LightGray,
                                        Color.LightGray
                                    )
                                )
                            )
                            .clickable(enabled = !isDisabled) {
                                inboxVm.setOfferScreen(listingOffer)
                                navController.navigate(NavigationItem.MakeOffer.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Make your offer",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            color = if (!isDisabled) Color.White else Color.Gray
                        )
                    }
                }

                if (messages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.MailOutline,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "No messages yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Start by sending an offer or arranging a meeting.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {


                        items(state.selectedOffer!!.messages) { message ->
                            OfferCardMessage(
                                message = message,
                                currentUser = currentUser!!,
                                offerUser = chatUser,
                                offer = state.selectedOffer!!,
                                navController = navController,
                                inboxVm = inboxVm
                            )
                        }

                        items(
                            state.userAppointments.filter { appointment ->
                                val matchesListing = appointment.listing.id == listingOffer.id
                                val matchedBoth = if (currentUser!!.role == Role.AGENT_USER || currentUser!!.role == Role.AGENT_ADMIN) {
                                    chatUser.id == appointment.user.id && currentUser!!.id == appointment.agent.id
                                } else {
                                    chatUser.id == appointment.agent.id && currentUser!!.id == appointment.user.id
                                }

                                matchesListing && matchedBoth
                            }
                        ) { appointment ->

                            val isAgent = appointment.agent.id == currentUser!!.id

                            AppointmentSummaryCard(
                                summary = AppointmentSummary(
                                    date = appointment.date,
                                    status = when (appointment.status) {
                                        AppointmentStatus.ACCEPTED -> true
                                        AppointmentStatus.REJECTED -> false
                                        AppointmentStatus.PENDING -> null
                                    }
                                ),
                                isAgent = isAgent,
                                onAccept = {
                                    inboxVm.acceptAppointment(appointment.id)
                                },
                                onReject = {
                                    inboxVm.declineAppointment(appointment.id)
                                }
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
                                            .border(
                                                1.dp,
                                                Color.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
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

                                        val text =
                                            if (state.selectedOffer!!.messages.last().sender.username == currentUser!!.username)
                                                "You accepted the offer"
                                            else
                                                "The ${chatUser.username} accepted your offer"


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
                                            .border(
                                                1.dp,
                                                Color.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
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
                                                .clickable {
                                                    inboxVm.acceptOffer(true)
                                                },
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
}


@Composable
fun AppointmentSummaryCard(
    summary: AppointmentSummary,
    isAgent: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {

    val statusText = when (summary.status) {
        true -> "Approved"
        false -> "Rejected"
        null -> "Pending"
    }

    val statusColor = when (summary.status) {
        true -> Color(0xFF2E7D32)
        false -> Color(0xFFC62828)
        null -> Color(0xFFEF6C00)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Appointment Summary",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Date
            Text(
                text = "Date: ${summary.date}",
                style = MaterialTheme.typography.bodyLarge
            )

            // Status
            Text(
                text = "Status: $statusText",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            )

            // Show buttons only if:
            // - user is AGENT
            // - appointment.status == Pending
            if (isAgent && summary.status == null) {

                val shape = RoundedCornerShape(6.dp)

                // ACCEPT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(shape)
                        .background(AscientGradient)
                        .clickable { onAccept() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Accept appointment",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // DECLINE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(shape)
                        .border(1.dp, AscientGradient, shape)
                        .clickable { onReject() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Decline appointment",
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



@Composable
fun OfferCardMessage(
    message : OfferMessage,   //OfferMessage = modello OFFER
    currentUser : User,
    offerUser : User,         //deriva da OFFER
    offer: Offer,             //dipendenza diretta
    inboxVm: InboxViewModel,
    navController : NavController
) {

    val isMine = message.sender.username == currentUser.username
    val bubbleColor = if (isMine) Color(0xFFF2F2F2) else Color.White

    //DIPENDENZA: status dell'offerta
    val textColor = when (message.status) {
        OfferStatus.ACCEPTED ->  Color(0xFF2E7D32)
        OfferStatus.REJECTED -> DarkRed
        else -> Color.Gray
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

                //DIPENDENZA → offerUser viene da OFFER
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

                Row {

                    Text(
                        text = if (message?.amount != null) {
                            "${message.amount.toInt()} €"
                        } else {
                            "OFFER NULL"
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Black
                        )
                    )

                    Spacer(Modifier.width(10.dp))

                    //DIPENDENZA → prende la penultima offerta
                    val secondToLast = offer.messages.getOrNull(offer.messages.size - 2)

                    Text(
                        text = if (secondToLast == null)
                            "${offer.listing.price.toInt()} €" //prezzo OFFER
                        else
                            "${secondToLast.amount!!.toInt()} €",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            Color.Gray,
                            fontWeight = FontWeight.Black,
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }

                Text(
                    text = returnStatus(message.status!!.name), //OFFER STATUS
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                if (!isMine && message.status == OfferStatus.PENDING) { //DIPENDENZA
                    val buttonShape = RoundedCornerShape(4.dp)
                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .clip(buttonShape)
                                .background(AscientGradient)
                                .clickable { inboxVm.acceptOffer(true) }, //logica OFFER
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
                                    .clickable { inboxVm.acceptOffer(false) }, //logica OFFER
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
                                    .clickable {
                                        navController.navigate(NavigationItem.MakeOffer.route)
                                   },

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


@Preview(showBackground = true)
@Composable
fun AppointmentSummaryCardBuyerPreview() {

    val sample = AppointmentSummary(
        date = LocalDate.of(2025, 1, 15),
        status = true
    )

    AppointmentSummaryCard(
        summary = sample,
        isAgent = true,
        onAccept = {},
        onReject = {}
    )
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
