package com.example.ingsw_24_25_dietiestates25.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(
            "Search",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF3F7FA),
            unfocusedContainerColor = Color(0xFFF3F7FA),
            disabledContainerColor = Color(0xFFF3F7FA),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    )
}

@Composable
fun GenericListItem(
    visibleIcons : Boolean = true,
    icon: Painter,
    title: String,
    subtitle: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEFF9FA))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color(0xFF3A7CA5),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A7CA5),
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if( visibleIcons ){
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Accept",
                tint = Color(0xFF3A7CA5),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onAccept() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Reject",
                tint = Color(0xFF3A7CA5),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onReject() }
            )
        }else{
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF3A7CA5),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { /*TODO*/ }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFF3A7CA5),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { /*TODO*/ }
            )
        }


    }
}

@Composable
fun <T> SearchableList(
    modifier: Modifier,
    items: List<T>,
    query: String,
    onQueryChange: (String) -> Unit,
    extraFilter: (T) -> Boolean = { true },
    searchFilter: (T, String) -> Boolean,
    itemContent: @Composable (T) -> Unit
) {

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        modifier = modifier
    )

    LazyColumn {
        items(
            items.filter { extraFilter(it) }
                .filter { searchFilter(it, query) }
        ) { item ->
            itemContent(item)
        }
    }

}