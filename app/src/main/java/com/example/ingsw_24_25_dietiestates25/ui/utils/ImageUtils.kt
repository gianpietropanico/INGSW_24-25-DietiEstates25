package com.example.ingsw_24_25_dietiestates25.ui.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

fun drawableToBase64(context: Context, @DrawableRes drawableId: Int): String? {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    if (bitmap == null) {
        Log.e("drawableToBase64", "Bitmap null: drawableId=$drawableId")
        return null
    }
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
}

fun bse64ToImageBitmap(base64: String): ImageBitmap {
    val pureBase64 = base64.substringAfter("base64,", base64) // rimuove eventuale header tipo "data:image/png;base64,"
    val imageBytes = Base64.decode(pureBase64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return bitmap.asImageBitmap()
}