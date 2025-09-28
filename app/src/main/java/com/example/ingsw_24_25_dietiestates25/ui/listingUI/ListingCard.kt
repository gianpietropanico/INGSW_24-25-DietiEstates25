package com.example.ingsw_24_25_dietiestates25.ui.listingUI

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.cardBackground
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu
import com.example.ingsw_24_25_dietiestates25.ui.utils.bse64ToImageBitmap

@Composable
fun ListingCard(
    propertyListing: PropertyListing,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Column {

            Row(modifier = Modifier.height(120.dp)) {
                // Primo tentativo di immagine Base64 sicuro
                val firstImageBitmap = try {
                    propertyListing.property.images.firstOrNull()?.let { base64 ->
                        // Decoding sicuro con eventuale controllo lunghezza
                        if (base64.length > 1_000_000) { // esempio limite 1MB
                            Log.w("ListingCard", "Immagine troppo grande, uso default")
                            null
                        } else {
                            bse64ToImageBitmap(base64)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ListingCard", "Errore decoding immagine: ${e.message}")
                    null
                }

                if (firstImageBitmap != null) {
                    Image(
                        bitmap = firstImageBitmap,
                        contentDescription = propertyListing.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(150.dp)
                            .fillMaxHeight()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_house),
                        contentDescription = propertyListing.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(150.dp)
                            .fillMaxHeight()
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(propertyListing.title, fontWeight = FontWeight.Bold)
                    Text("${propertyListing.property.city}, ${propertyListing.property.province}")
                    Text("${propertyListing.price} €", color = MaterialTheme.colorScheme.primary)
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Delete",
                        color = primaryBlu,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* futura implementazione */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(brush = AscientGradient, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Edit Property",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}