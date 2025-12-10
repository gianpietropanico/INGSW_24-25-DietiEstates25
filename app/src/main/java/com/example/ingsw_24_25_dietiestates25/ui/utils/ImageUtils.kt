package com.example.ingsw_24_25_dietiestates25.ui.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun drawableToBase64(context: Context, @DrawableRes drawableId: Int): String? {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    if (bitmap == null) {
        Log.e("drawableToBase64", "Bitmap is null: drawableId=$drawableId")
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

suspend fun downloadImageAsBase64(imageUrl: String): String? = withContext(Dispatchers.IO) {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun safeDecodeBase64(base64: String?): ImageBitmap? {
    if (base64.isNullOrEmpty()) return null
    return try {
        if (base64.length > 1_000_000) {
            Log.w("ImageDecode", "Image too large, using default")
            null
        } else {
            bse64ToImageBitmap(base64)
        }
    } catch (e: Exception) {
        Log.e("ImageDecode", "Error decoding image: ${e.message}")
        null
    }
}

fun uriToBase64WithSizeLimit(context: Context, uri: Uri, maxKb: Int = 500): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        var bitmap = BitmapFactory.decodeStream(inputStream) ?: return null

        // Ridimensiona bitmap se troppo grande
        var outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        var byteArray = outputStream.toByteArray()

        // Riduci qualità fino a rientrare nel limite
        var quality = 90
        while (byteArray.size / 1024 > maxKb && quality > 10) {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
            byteArray = outputStream.toByteArray()
            quality -= 10
        }

        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}