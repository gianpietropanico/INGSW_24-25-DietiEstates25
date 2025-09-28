package com.example.ingsw_24_25_dietiestates25.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.example.ingsw_24_25_dietiestates25.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory

// Funzione che crea bitmap circolare
fun Context.drawableToCircleBitmap(
    @DrawableRes drawableRes: Int,
    sizeDp: Int = 48,
    circleColor: Int = 0xFF03DAC5.toInt()
): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableRes)
        ?: throw IllegalArgumentException("Drawable not found")

    val sizePx = (sizeDp * resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = android.graphics.Paint()
    paint.isAntiAlias = true
    paint.color = circleColor

    // Disegna cerchio
    canvas.drawCircle(sizePx / 2f, sizePx / 2f, sizePx / 2f, paint)

    // Disegna icona sopra
    drawable.setBounds(0, 0, sizePx, sizePx)
    drawable.draw(canvas)

    return bitmap
}

// Funzione per ottenere BitmapDescriptor con caching
@Composable
fun Context.getPoiBitmapDescriptor(
    poiType: String,
    poiIcons: Map<String, Int>,
    poiColors: Map<String, Int>,
    sizeDp: Int = 24
): com.google.android.gms.maps.model.BitmapDescriptor {
    val cache = remember { mutableMapOf<String, Bitmap>() }

    val bitmap = cache.getOrPut(poiType) {
        drawableToCircleBitmap(
            drawableRes = poiIcons[poiType] ?: R.drawable.ic_default,
            sizeDp = sizeDp,
            circleColor = poiColors[poiType] ?: 0xFF03DAC5.toInt()
        )
    }
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}