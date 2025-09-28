package com.example.ingsw_24_25_dietiestates25.ui.offerUI
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferSummary
import com.example.ingsw_24_25_dietiestates25.ui.utils.MinimalTextField
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.theme.bluPerchEcipiace
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState


@Composable
fun MakeOfferScreen(
    inboxVm : InboxViewModel,
    navController : NavController
) {
    var amount by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val state by inboxVm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material.Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel",
                tint = bluPerchEcipiace,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {

            val picture = state.selectedProperty?.property?.images?.first()
            if ( picture != null){

                Image(
                    bitmap = bse64ToImageBitmap(picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }else{

                Image(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = state.selectedProperty!!.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = state.selectedProperty!!.property.street,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = state.selectedProperty!!.price.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1976D2),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        if(!showDialog) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MinimalTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Price",
                    placeholder = "Insert your price",
                    modifier = Modifier.weight(1f),
                    onError = false,
                    leadingIcon = Icons.Default.Euro
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF673AB7)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF673AB7)
                    )
                ) {
                    Text("History offers")
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Freccia giù"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    inboxVm.makeOffer(
                        propertyId = state.selectedProperty!!.id,
                        agentEmail = state.selectedProperty!!.agentEmail,
                        amount = amount
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Offer", fontSize = 18.sp)
            }
        }else{

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { showDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF673AB7)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF673AB7)
                        )
                    ) {
                        Text("History offers")
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = "Freccia su"
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.historyOffers) { offer ->
                        OfferSummaryItem(offer)
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun OfferSummaryItem(offer: OfferSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "€ ${offer.amount ?: "-"}",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )

        val statusText: String = when (offer.accepted) {
            true -> "Accettata"
            false -> "Rifiutata"
            null -> "In attesa"
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = when (offer.accepted) {
                true -> Color(0xFF2E7D32)
                false -> Color(0xFFC62828)
                null -> Color(0xFFF9A825)
            }
        )
    }
}
