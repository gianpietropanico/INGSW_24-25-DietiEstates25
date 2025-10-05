package com.example.ingsw_24_25_dietiestates25.ui.offerUI

import android.util.Log
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferStatus
import java.util.Locale
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import com.example.ingsw_24_25_dietiestates25.ui.navigation.NavigationItem
import com.example.ingsw_24_25_dietiestates25.ui.utils.LoadingOverlay

@Composable
fun OfferChatScreen(
    inboxVm :InboxViewModel,
    navController: NavController
) {

    val state by inboxVm.state.collectAsState()


    when {
        (state.selectedProperty == null || state.selectedOffer == null) -> {
            Log.d("OFFERCHATSCREEN","${state.selectedProperty} AYAYAYAYAY ${state.selectedOffer}")
            LoadingOverlay(true)
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = bluPerchEcipiace,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                inboxVm.clearResultMessage()
                                navController.popBackStack()
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if(state.selectedProperty!!.agent!!.username != inboxVm.getUsername() ){
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.Gray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.selectedProperty!!.agent!!.username.first().toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.selectedProperty!!.agent!!.username,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }else{
                            Text(
                                text = "Your Listing",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp))
                        ) {
                            val picture = state.selectedProperty?.property?.images?.first()
                            if (picture != null) {

                                Image(
                                    bitmap = bse64ToImageBitmap(picture),
                                    contentDescription = "Property Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {

                                Image(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Property Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.selectedProperty!!.title,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = state.selectedProperty!!.property.street,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "€${String.format(Locale.US, "%.2f", state.selectedProperty!!.price)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(NavigationItem.MakeOffer.route)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fai un’offerta")
                        }


                        Button(
                            onClick = {},//TODO CLICK SU MAKE APPOINTMENT
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00ACC1))
                        ) {
                            Text("Acquista", color = Color.White)
                        }
                    }
                }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                ) {
                    items(state.offerMessages) { offer ->
                        OfferCardMessage(
                            amount = offer.amount!!,
                            username = offer.senderName,
                            currentUser = inboxVm.user.collectAsState().value!!.username,
                            status = offer.status!!,
                            inboxVm
                        )
                    }
                }
            }
        }
    }
}

    @Composable
    fun OfferCardMessage(
        amount: Double,
        username: String,
        currentUser: String,
        status : OfferStatus,
        inboxVm: InboxViewModel
    ) {
        val isMine = username == currentUser
        val bubbleColor = if (isMine) Color(0xFFD1F2EB) else Color.White
        val textColor = when (status) {
            OfferStatus.ACCEPTED ->  Color(0xFF2E7D32) // verde accettata
            OfferStatus.REJECTED ->  Color.Red.copy(alpha = 0.6f) // rosso per rifiutata
            else -> Color(0xFF006666) // default
        }

        val textDecoration = if (status == OfferStatus.REJECTED) TextDecoration.LineThrough else TextDecoration.None

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
        ) {
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
                    Text(
                        text = if (isMine) "Your Offer" else "His Offer",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "€${String.format("%.2f", amount)}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = textColor,
                            textDecoration = textDecoration
                        )
                    )


                    // Bottoni solo se l'offerta NON è mia e non è già accettata o rifiutata
                    if (!isMine && status == OfferStatus.PENDING) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { inboxVm.acceptOffer(true)},
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("Accetta")
                            }
                            OutlinedButton(
                                onClick = { inboxVm.acceptOffer(false) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828))
                            ) {
                                Text("Rifiuta")
                            }
                        }
                    }
                }
            }
        }
    }


//@Preview(showBackground = true)
//@Composable
//fun OfferChatScreenPreview() {
//    val dummyOffers = listOf(
//        OfferMessage("1", "Anna", 0L, 100.0, null),
//        OfferMessage("2", "Mario", 0L, 120.0, null),
//        OfferMessage("3", "Anna", 0L, 150.0, true)
//    )
//
//    MaterialTheme {
//        OfferChatScreen(
//            offers = dummyOffers.map {
//                OfferMessage(
//                    id = it.id,
//                    senderId = it.senderId,
//                    timestamp = it.timestamp,
//                    amount = it.amount,
//                    accepted = it.accepted
//                )
//            }
//        )
//    }
//}