package com.example.youtubelearning.view

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.youtubelearning.databinding.ActivityAddImageAtivityBinding
import com.example.youtubelearning.models.MyImages
import com.example.youtubelearning.room.MyImagesDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddImageActivity : AppCompatActivity() {

    private lateinit var addImageBinding: ActivityAddImageAtivityBinding
    private lateinit var activityResultLauncherForCamera: ActivityResultLauncher<Intent>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var imageUri: Uri? = null
    private val CAMERA_REQUEST_CODE = 1001
    private val LOCATION_PERMISSION_REQUEST_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addImageBinding = ActivityAddImageAtivityBinding.inflate(layoutInflater)
        setContentView(addImageBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Register the activity result launcher for camera capture
        registerActivityForCamera()

        // Set up click listener to capture a photo
        addImageBinding.imageViewAddImage.setOnClickListener {
            if (checkCameraPermission()) {
                openCameraForPhoto()
            } else {
                requestCameraPermission()
            }
        }

        // Set up click listener to get GPS location
        addImageBinding.buttonGetLocation.setOnClickListener {
            if (checkLocationPermission()) {
                getCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        // Add button click to handle saving the image (to save it to a database, etc.)
        addImageBinding.buttonAdd.setOnClickListener {
            var update = true
            val tittle = addImageBinding.editTextAddTitle.text.toString().trim()
            val date = addImageBinding.editTextAddDate.text.toString().trim()
            val des = addImageBinding.editTextAddDescription.text.toString().trim()
            val location = addImageBinding.textViewLocation.text.toString().trim()

            // Validation for empty fields
            if (imageUri == null) {

                update = false
                toastDisplay("Image is required")
            }
            if (tittle.isEmpty()) {
                update = false
                toastDisplay("Title is required")
            }
            if (des.isEmpty()) {
                update = false
                toastDisplay("Description is required")
            }
            if (location.isEmpty()) {
                update = false
                toastDisplay("Location is required")
            }
            if (date.isEmpty()) {
                update = false
                toastDisplay("Date is required")
            }

            // Proceed if everything is valid
            if (update) {
                val imagePath = imageUri?.let { it1 -> saveImageToStorage(it1) }
                val myImage = imagePath?.let { it1 -> MyImages(tittle, des, it1, location, date) }

                val db = MyImagesDatabase.getDatabase(this)
                val imageDao = db.myImageDao()
                CoroutineScope(Dispatchers.IO).launch {
                    if (myImage != null) {
                        imageDao.insert(myImage)
                    }

                    // Optionally, show a Toast on the main thread after inserting into the DB
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Image added to the database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Toolbar back button functionality
        addImageBinding.toolBarAddImage.setNavigationOnClickListener {
            finish()
        }

        // Set up date picker
        addImageBinding.editTextAddDate.setOnClickListener {
            showDatePickerDialog()
        }
    }



    fun saveImageToStorage(imageUri: Uri): String? {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val byteArray = inputStream?.readBytes()
            val fileName = "my_image_${System.currentTimeMillis()}.jpg"
            val file = File(applicationContext.filesDir, fileName)
            file.writeBytes(byteArray ?: byteArrayOf())
            return file.absolutePath // Return the file path
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null // Return null if failed
    }

    // Function to show Date Picker Dialog
    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Initialize the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Set the selected date in EditText (using a date format)
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Set the selected date into EditText
                addImageBinding.editTextAddDate.setText(formattedDate)
            },
            year, month, dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    // Check for camera permission
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request camera permission
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    // Check for location permission
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request location permission
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // Register the activity for camera capture
    private fun registerActivityForCamera() {
        activityResultLauncherForCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri?.let {
                    addImageBinding.imageViewAddImage.setImageURI(it)
                } ?: run {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Open camera to capture photo
    private fun openCameraForPhoto() {
        // Create a ContentValues object to define the new image
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Captured Image")  // Image title
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")   // MIME type
        }

        // Insert the image metadata into MediaStore and get a URI to the newly created image
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Check if the URI is null
        if (imageUri != null) {
            // Create the camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)  // Set the output URI for the camera to save the image
            }

            // Launch the camera with the intent
            activityResultLauncherForCamera.launch(intent)
        } else {
            Toast.makeText(this, "Failed to create image URI", Toast.LENGTH_SHORT).show()
        }
    }

    // Toast message display
    private fun toastDisplay(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Get current GPS location
    private fun getCurrentLocation() {
        if (checkLocationPermission()) {
            val locationTask: Task<Location> = fusedLocationClient.lastLocation
            locationTask.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Display the latitude and longitude in the TextView
                    addImageBinding.textViewLocation.text = "Lat: $latitude, Lon: $longitude"
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraForPhoto()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
