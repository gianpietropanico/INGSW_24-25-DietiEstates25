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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.primaryBlu

@Composable
fun ListingCard(
    propertyListing: PropertyListing,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(8.dp)
            ) {
                // Se c'è un'immagine, la mostro; altrimenti default
                Log.d("ListingCard", "URL immagine: ${propertyListing.property.images.firstOrNull()}")

                if (propertyListing.property.images.isNotEmpty()) {
                    AsyncImage(
                        model = propertyListing.property.images.first(),
                        contentDescription = "Property Image",
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.default_house),
                        error = painterResource(id = R.drawable.default_house)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_house),
                        contentDescription = "Default House",
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = propertyListing.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "${propertyListing.property.city}, ${propertyListing.property.province}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = "${propertyListing.price} €",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clickable { /* implementa delete */ },
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
                            .height(42.dp),
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