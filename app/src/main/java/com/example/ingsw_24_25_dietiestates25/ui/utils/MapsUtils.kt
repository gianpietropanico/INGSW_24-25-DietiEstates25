package com.example.ingsw_24_25_dietiestates25.ui.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.ingsw_24_25_dietiestates25.R
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.POI
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

object MapUtils {

    fun defaultMapProperties(context: Context): MapProperties {
        return MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
            isTrafficEnabled = true,
            isBuildingEnabled = true
        )
    }

    val defaultUiSettings: MapUiSettings
        get() = MapUiSettings(
            compassEnabled = true,
            zoomGesturesEnabled = true
        )


    val poiIcons = mapOf(
        "School" to R.drawable.ic_school,
        "University" to R.drawable.ic_school,
        "Park" to R.drawable.ic_park,
        "Bus Stop" to R.drawable.ic_bus,
        "Restaurant" to R.drawable.ic_restaurant,
        "Hospital" to R.drawable.ic_hospital,
        "Stadium" to R.drawable.ic_stadium,
        "Train Station" to R.drawable.ic_train,
        "Metro" to R.drawable.ic_metro
    )

    val poiColors = Color.LightGray


}

