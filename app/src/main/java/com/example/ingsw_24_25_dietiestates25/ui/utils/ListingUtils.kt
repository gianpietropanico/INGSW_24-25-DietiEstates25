package com.example.ingsw_24_25_dietiestates25.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.unselectedFacility

@Composable
fun TypeToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (selected) AscientGradient else Brush.linearGradient(
                    listOf(
                        Color.LightGray,
                        Color.LightGray
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CounterRow(label: String, count: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleButton("−") { if (count > 0) onValueChange(count - 1) }
            Spacer(Modifier.width(8.dp))
            Text("$count", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            CircleButton("+") { onValueChange(count + 1) }
        }
    }
}

@Composable
fun CircleButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(brush = AscientGradient, shape = CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

// cliccabile
@Composable
fun FacilityChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .background(
                if (selected) AscientGradient else Brush.linearGradient(
                    listOf(
                        unselectedFacility,
                        unselectedFacility
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

// non cliccabile
@Composable
fun Chip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(brush = AscientGradient)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PropertyFacilitiesChips(property: Property) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        property.parking.takeIf { it }?.let { Chip("Parking") }
        property.garden.takeIf { it }?.let { Chip("Garden") }
        property.elevator.takeIf { it }?.let { Chip("Elevator") }
        property.gatehouse.takeIf { it }?.let { Chip("Gatehouse") }
        property.balcony.takeIf { it }?.let { Chip("Balcony") }
        property.roof.takeIf { it }?.let { Chip("Roof") }
        property.airConditioning.takeIf { it }?.let { Chip("A/C") }
        property.heatingSystem.takeIf { it }?.let { Chip("Heating") }
    }
}
@Composable
fun EnergyClassDropdown(listingVm: ListingViewModel) {
    val state by listingVm.state.collectAsState()
    val selectedClass = state.formState.energyClass

    EnergyClassRow(
        selectedClass = selectedClass,
        onClassSelected = { newClass ->
            listingVm.updateForm { it.copy(energyClass = newClass) }
        }
    )
}

@Composable
fun EnergyClassRow(
    selectedClass: EnergyClass,
    onClassSelected: (EnergyClass) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Energy Class", fontWeight = FontWeight.Medium)
        Box {
            OutlinedTextField(
                value = selectedClass.label,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .width(80.dp)
                    .height(50.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                EnergyClass.values().forEach { energyOption ->
                    DropdownMenuItem(
                        text = { Text(energyOption.label) },
                        onClick = {
                            onClassSelected(energyOption) // aggiorna il formState
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

