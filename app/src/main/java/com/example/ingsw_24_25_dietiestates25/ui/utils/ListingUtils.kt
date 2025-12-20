package com.example.ingsw_24_25_dietiestates25.ui.utils

import android.R.attr.rating
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Balcony
import androidx.compose.material.icons.outlined.Bathtub
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Elevator
import androidx.compose.material.icons.outlined.Grass
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.HouseSiding
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Subway
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Agency
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.PropertyListing
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.ListingViewModel
import com.example.ingsw_24_25_dietiestates25.ui.theme.AscientGradient
import com.example.ingsw_24_25_dietiestates25.ui.theme.unselectedFacility

@Composable
fun TypeToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) AscientGradient else Brush.linearGradient(
                    listOf(
                        Color(0xFFBFC4D6).copy(alpha = 0.45f),
                        Color(0xFFBFC4D6).copy(alpha = 0.45f)
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
fun ListingTypeSelector(
    type: Type,
    onTypeChange: (Type) -> Unit
) {
    Column {
        Text(
            "Listing Type",
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        Row {
            TypeToggle(
                label = "Rent",
                selected = type == Type.RENT,
                onClick = { onTypeChange(Type.RENT) }
            )

            Spacer(Modifier.width(8.dp))

            TypeToggle(
                label = "Sell",
                selected = type == Type.SELL,
                onClick = { onTypeChange(Type.SELL) }
            )
        }
    }
}


@Composable
fun CounterRow(
    label: String,
    count: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.width(90.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            CounterButton("-") {
                if (count > 0) onValueChange(count - 1)
            }

            Text(
                text = count.toString(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            CounterButton("+") {
                onValueChange(count + 1)
            }
        }
    }

}
@Composable
fun CounterButton(
    symbol: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)                              // ← più largo
            .clip(RoundedCornerShape(12.dp))          // ← leggermente più morbido
            .background(Color(0xFFBFC4D6).copy(alpha = 0.45f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 22.sp,                         // ← leggermente più grande
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C2F4A)
        )
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

fun getFacilityIcon(label: String) = when {
    label.contains("A/C", true) -> Icons.Outlined.AcUnit
    label.contains("Garden", true) -> Icons.Outlined.LocalFlorist
    label.contains("Parking", true) -> Icons.Outlined.LocalParking
    label.contains("Gatehouse", true) -> Icons.Outlined.AccountCircle
    label.contains("Roof", true) -> Icons.Outlined.Wifi
    label.contains("Heating", true) -> Icons.Outlined.Whatshot
    label.contains("Elevator", true) -> Icons.Outlined.Elevator
    label.contains("Balcony", true) -> Icons.Outlined.Balcony
    label.contains("Energy", true) -> Icons.Outlined.Bolt
    else -> Icons.Outlined.Home
}


@Composable
fun FacilityChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    chipModifier: Modifier = Modifier
) {
    val icon = getFacilityIcon(label)

    Box(
        modifier = chipModifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) AscientGradient else Brush.linearGradient(
                    listOf(
                        Color(0xFFBFC4D6).copy(alpha = 0.45f),
                        Color(0xFFBFC4D6).copy(alpha = 0.45f)
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) Color.White else Color.Black,
                modifier = Modifier.size(22.dp)                // ★ più grande, più chiaro
            )
            Text(
                text = label,
                color = if (selected) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }


}





@Composable
fun PropertyFacilitiesChips(property: Property) {
    val facilities = buildList {
        if (property.airConditioning) add("A/C")
        if (property.garden) add("Garden")
        if (property.parking) add("Parking")
        if (property.gatehouse) add("Gatehouse")
        if (property.roof) add("Roof")
        if (property.heatingSystem) add("Heating")
        if (property.elevator) add("Elevator")
        if (property.balcony) add("Balcony")
        if (property.energyClass != null) add("Energy class ${property.energyClass.label}")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        facilities.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FacilityItemFixed(rowItems[0])
                if (rowItems.size > 1)
                    FacilityItemFixed(rowItems[1])
                else
                    Spacer(modifier = Modifier.width(140.dp))
            }
        }
    }
}

@Composable
fun FacilityItemFixed(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(32.dp)
                .widthIn(min = 120.dp)
    ) {
        val icon = when {
            label.contains("A/C", true) -> Icons.Outlined.AcUnit
            label.contains("Garden", true) -> Icons.Outlined.LocalFlorist
            label.contains("Parking", true) -> Icons.Outlined.LocalParking
            label.contains("Gatehouse", true) -> Icons.Outlined.AccountCircle
            label.contains("Roof", true) -> Icons.Outlined.Wifi
            label.contains("Heating", true) -> Icons.Outlined.Whatshot
            label.contains("Elevator", true) -> Icons.Outlined.Elevator
            label.contains("Balcony", true) -> Icons.Outlined.Balcony
            label.contains("Energy", true) -> Icons.Outlined.Bolt
            else -> Icons.Outlined.Home
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .alignByBaseline()
        )
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier.alignByBaseline()
        )
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
fun AgentCard(
    agent: User?,
    agency : Agency?
) {
    Log.d("AGENT CARD","${agency}    ${agent}")
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.6f),
            thickness = 1.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3E1FA)),
                    contentAlignment = Alignment.Center
                ) {
                    if (agent?.profilePicture != null) {
                        Image(
                            bitmap = bse64ToImageBitmap(agent.profilePicture),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.defaultprofilepic),
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {



                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = agent?.username ?: "ERRORE",
                            color = if(agent?.username == null ) Color.Red else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )


                        Text(
                            text = agency?.name ?: "ERRORE",
                            color = if(agency == null )Color.Red else Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer( modifier = Modifier.width(60.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- Valutazione numerica ---
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "5",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black,
                                modifier = Modifier.alignByBaseline()
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "out of 5",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.alignByBaseline()
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // --- Stelle ---
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // --- Numero recensioni ---
                        Text(
                            text = "73 ratings",
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }


        }

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.6f),
            thickness = 1.dp
        )
    }
}

@Composable
fun InfoHouse(property: Property) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem(icon = Icons.Outlined.Home, label = "${property.size.toInt()} m²")
            InfoItem(
                icon = Icons.Outlined.Bed,
                label = "${property.numberOfRooms} room${if (property.numberOfRooms != 1) "s" else ""}"
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem(
                icon = Icons.Outlined.Bathtub,
                label = "${property.numberOfBathrooms} bathroom${if (property.numberOfBathrooms != 1) "s" else ""}"
            )
            InfoItem(
                icon = Icons.Outlined.LocationOn,
                label = "${property.city}, ${property.province}"
            )
        }
    }
}


@Composable
fun InfoItem(icon: ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)
            .widthIn(min = 120.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = label,
            color = Color(0xFF3C4043),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}

@Composable
fun NearbyPlacesSection(pois: List<POI>) {

    if (pois.isEmpty()) return
    Text(
        text = "Nearest public facilities :",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
    Spacer(Modifier.height(12.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        pois.forEach { poi ->
            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(min = 140.dp)
            ) {
                PoiItemDefault(poi)
            }
        }
    }

}



@Composable
fun PoiItemDefault(poi: POI) {
    val icon = when (poi.type.lowercase()) {
        "school", "university" -> Icons.Outlined.School
        "park" -> Icons.Outlined.Park
        "bus stop" -> Icons.Outlined.DirectionsBus
        "restaurant" -> Icons.Outlined.Restaurant
        "hospital" -> Icons.Outlined.LocalHospital
        "stadium" -> Icons.Outlined.SportsSoccer
        "train station" -> Icons.Outlined.Train
        "metro" -> Icons.Outlined.Subway
        "bar", "pub", "café", "cafe" -> Icons.Outlined.LocalBar
        else -> Icons.Outlined.Place
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = poi.name,
            tint = Color(0xFF5F6368),
            modifier = Modifier
                .size(22.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = poi.name,
                color = Color(0xFF3C4043),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            Row{
//                Text(
//                    text = poi.type,
//                    color = Color.Gray,
//                    fontSize = 13.sp
//                )
//                Spacer(Modifier.width(5.dp))
                Text(
                    text = poi.distance.toInt().toString() + " m",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

        }
    }
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
            .padding(vertical = 6.dp)
    ) {
        Text("Energy Class:", fontWeight = FontWeight.Medium)

        Spacer(Modifier.width(70.dp))

        Box {

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFBFC4D6).copy(alpha = 0.45f))
                    .clickable { expanded = true }
                    .height(48.dp)
                    .width(170.dp)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    selectedClass.label,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C2F4A)
                )

                Spacer(Modifier.width(110.dp))

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF2C2F4A),
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                EnergyClass.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onClassSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AgentCardPreview() {
//    AgentCard(
//        agent = User(
//            id = "99",
//            name = "Mario Rossi",
//            email = "mario.rossi@agenzia.it",
//            role = Role.AGENT_USER,
//            username = "TORCI",
//            surname = "DAJE"
//        )
//    )
//}

@Preview(showBackground = true)
@Composable
fun InfoHousePreview() {
    // --- Mock POI ---
    val fakePois = listOf(
        POI(
            name = "Politecnico di Torino",
            type = "University",
            lat = 45.062,
            lon = 7.662,
            distance = 250.0
        ),
        POI(
            name = "Bar Centrale",
            type = "Cafe",
            lat = 45.063,
            lon = 7.664,
            distance = 120.0
        )
    )

    // --- Mock immagini ---
    val fakeImages = listOf(
        "https://images.unsplash.com/photo-1507089947368-19c1da9775ae",
        "https://images.unsplash.com/photo-1568605114967-8130f3a36994"
    )

    val fakeProperty = Property(
        city = "Torino",
        cap = "10129",
        country = "Italia",
        province = "TO",
        street = "Corso Duca degli Abruzzi",
        civicNumber = "24",
        latitude = 45.062,
        longitude = 7.662,
        pois = fakePois,
        images = fakeImages,
        numberOfRooms = 3,
        numberOfBathrooms = 1,
        size = 85f,
        energyClass = EnergyClass.B,
        parking = true,
        garden = false,
        elevator = true,
        gatehouse = false,
        balcony = true,
        roof = false,
        airConditioning = true,
        heatingSystem = true,
        description = "Appartamento moderno e luminoso con cucina open-space e vista panoramica."
    )

    val fakeAgent = User(
        id = "99",
        name = "Mario Rossi",
        email = "mario.rossi@agenzia.it",
        role = Role.AGENT_USER,
        username = "TORCI",
        surname = "DAJE"
    )

    //AgentCard( fakeAgent )
}