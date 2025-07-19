package com.example.ingsw_24_25_dietiestates25.ui.profileUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R



@Composable
fun ProfileScreen(

) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Sfondo bianco per l'intero schermo
    ) {
        // Immagine di sfondo
        Image(
            painter = painterResource(id = R.drawable.rectangleblu),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Altezza personalizzata dell'immagine di sfondo
                .align(Alignment.TopCenter), // Posizionata in alto al centro
            contentScale = ContentScale.Crop // Ritaglia l'immagine per adattarla alla larghezza
        )

        // Contenuto principale
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp)) // Spazio per lasciare l'immagine visibile sopra

            // Top Section
            ProfileTopSection(
                name = "Gianpietro Panico",
                email = "gianpietro.panico@gmail.com"
            )

            // Divider sotto la top section
            Divider(
                color = Color.Gray,
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Divider con larghezza personalizzata
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campi statici senza navigazione
            ProfileOptionItem(
                icon = R.drawable.icon_person,
                title = "Edit personal details",
                onClick = { /* Implementa l'azione */ }
            )

            ProfileOptionItem(
                icon = R.drawable.calendar,
                title = "Show calendar",
                onClick = { /* Implementa l'azione */ }
            )

            ProfileOptionItem(
                icon = R.drawable.changepass,
                title = "Change your password",
                onClick = { /* Implementa l'azione */ }
            )

            ProfileOptionItem(
                icon = R.drawable.activities,
                title = "Your activities",
                onClick = { /* Implementa l'azione */ }
            )

            ProfileOptionItem(
                icon = R.drawable.logout,
                title = "Logout",
                onClick = { /* Implementa l'azione */ }
            )
        }
    }
}


@Composable
fun ProfileTopSection(name: String, email: String) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.test),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(130.dp) // Garantisce che l'immagine abbia larghezza e altezza uguali
                .clip(CircleShape) // Ritaglia l'immagine in una forma circolare
                .background(Color.Transparent), // Sfondo trasparente dietro l'immagine
            contentScale = ContentScale.Crop // Ritaglia l'immagine per riempire il cerchio
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Nome
        Text(
            text = name,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Email
        Text(
            text = email,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun ProfileOptionItem(icon: Int, title: String, onClick: () -> Unit) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp) // Dimensione dell'immagine
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) // Ombra con angoli arrotondati
                     .clip(RoundedCornerShape(8.dp)) // Arrotonda gli angoli dell'immagine
               .background(Color.White) // Sfondo dietro l'immagine
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp
                )


            )

             //freccia
            Image(
                painter = painterResource(id = R.drawable.frecciaprofilo),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
