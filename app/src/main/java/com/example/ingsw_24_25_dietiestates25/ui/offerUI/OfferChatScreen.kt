package com.example.ingsw_24_25_dietiestates25.ui.offerUI

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.OfferMessage

@Composable
fun OfferChatScreen(
    offers: List<OfferMessage>
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {

            // 🔹 Top row: Nome venditore
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Adolfo".first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Adolfo",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Prodotto + prezzo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Qui puoi mettere immagine prodotto (ora placeholder)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Zaino",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "€${String.format("%.2f", 31.0)} (+ €${String.format("%.2f", 30.0)} spedizione)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Bottoni
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Fai un’offerta")
                }
                Button(
                    onClick = {},
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
            items(offers) { offer ->
                OfferCardMessage(
                    amount = offer.amount!!,
                    username = offer.senderId,      // chi ha fatto l’offerta
                    currentUser = "Anna",           // utente della chat in corso
                    onAccept = { println("Accettata ${offer.amount}") },
                    onDecline = { println("Rifiutata ${offer.amount}") }
                )
            }
        }
    }
}
@Composable
fun OfferCardMessage(
    amount: Double,
    username: String,
    currentUser: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val isMine = username == currentUser
    val bubbleColor = if (isMine) Color(0xFFD1F2EB) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .widthIn(max = 250.dp), // larghezza max stile chat
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isMine) "La tua offerta" else "Offerta ricevuta",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "€${String.format("%.2f", amount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF006666)
                )

                // Solo se l'offerta NON è mia → mostra bottoni
                if (!isMine) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Text("Accetta")
                        }
                        OutlinedButton(
                            onClick = onDecline,
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


@Preview(showBackground = true)
@Composable
fun OfferChatScreenPreview() {
    val dummyOffers = listOf(
        OfferMessage("1", "Anna", 0L, 100.0, null),
        OfferMessage("2", "Mario", 0L, 120.0, null),
        OfferMessage("3", "Anna", 0L, 150.0, true)
    )

    MaterialTheme {
        OfferChatScreen(
            offers = dummyOffers.map {
                OfferMessage(
                    id = it.id,
                    senderId = it.senderId,
                    timestamp = it.timestamp,
                    amount = it.amount,
                    accepted = it.accepted
                )
            }
        )
    }
}