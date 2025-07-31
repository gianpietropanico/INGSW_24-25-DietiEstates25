package com.example.ingsw_24_25_dietiestates25.ui.propertyUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.model.dataclass.Property
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun AddPropertyScreen(viewModel: PropertyViewModel = hiltViewModel()) {
    val titleState = remember { mutableStateOf("") }
    val agentEmailState = remember { mutableStateOf("") }
    val selectedPosition = remember { mutableStateOf<LatLng?>(null) }

    val addResult by viewModel.addResult.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Aggiungi nuova proprietà", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = { Text("Titolo") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        OutlinedTextField(
            value = agentEmailState.value,
            onValueChange = { agentEmailState.value = it },
            label = { Text("Email agente") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mappa con selezione posizione
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            val defaultLocation = LatLng(45.4642, 9.1900) // Milano di Deafault
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedPosition.value = latLng
                }
            ) {
                selectedPosition.value?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Posizione selezionata"
                    )
                }
            }
        }

        Button(
            onClick = {
                val pos = selectedPosition.value
                if (pos != null && titleState.value.isNotBlank() && agentEmailState.value.isNotBlank()) {
                    val newProperty = Property(
                        id = "",
                        title = titleState.value,
                        latitude = pos.latitude,
                        longitude = pos.longitude,
                        indicators = emptyList(),
                        agentEmail = agentEmailState.value
                    )
                    viewModel.addProperty(newProperty)
                } else {
                    // gestisci errore (ad es. mostra toast "Seleziona posizione e completa i campi")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Aggiungi proprietà")
        }

        addResult?.let {
            Text(
                "Proprietà aggiunta: ${it.title}",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}