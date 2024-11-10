package com.example.youtubelearning.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

class ControlPermission {

    companion object {

        // Check permission to read images or files
        fun checkReadPermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13 (API 33) and above
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                // Below Android 13 (API < 33)
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        // Check permission to write to storage
        fun checkWritePermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped storage is used for Android 10 (API 29) and above.
                // No need for WRITE_EXTERNAL_STORAGE permission, use MediaStore API
                true
            } else {
                // Below Android 10
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        // Request Read Permission (if needed)
        fun requestReadPermission(activity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_READ_PERMISSION
            )
        }

        // Request Write Permission (if needed)
        fun requestWritePermission(activity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERMISSION
            )
        }

        // Request READ_MEDIA_IMAGES permission for Android 13 and above
        fun requestReadMediaPermission(activity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_READ_PERMISSION
            )
        }

        private const val REQUEST_CODE_READ_PERMISSION = 1001
        private const val REQUEST_CODE_WRITE_PERMISSION = 1002
    }
}
