package com.example.youtubelearning.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import java.io.IOException
import java.io.InputStream
import android.net.Uri

class ConvertImage {

    companion object {
        // Function to convert image URI to Base64
        fun convertImageToBase64(context: Context, imageUri: Uri?): String? {
            return try {
                // Open InputStream for the image URI
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri!!)
                val byteArray = inputStream?.readBytes()

                // If the byte array is not null, encode it to Base64 string
                byteArray?.let {
                    Base64.encodeToString(it, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()  // Log the error
                null
            }
        }
    }
}
