package com.example.youtubelearning.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.youtubelearning.R
import com.example.youtubelearning.databinding.ActivityUpdateImageBinding
import java.io.File

class updateImageActivity2 : AppCompatActivity() {

    lateinit var updateImageBinding: ActivityUpdateImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateImageBinding = ActivityUpdateImageBinding.inflate(layoutInflater)
        setContentView(updateImageBinding.root)


        val imageId: String? = intent.getStringExtra("imageId")

        val image: String? = intent.getStringExtra("image")
        val imageTitle: String? = intent.getStringExtra("imageTitle")
        val description: String? = intent.getStringExtra("description")
        val date: String? = intent.getStringExtra("date")
        val location: String? = intent.getStringExtra("location")
        val imageUri = image?.let { convertToUri(it) }

        updateImageBinding.updateBack.setOnClickListener{
            finish()
        }

        updateImageBinding.imageViewUpdateImage.setImageURI(imageUri)
        updateImageBinding.editTextUpdateDate.setText(date)
        updateImageBinding.editTextUpdateTitle.setText(imageTitle)
        updateImageBinding.editTextUpdateDescription.setText(description)
        updateImageBinding.textViewLocation.setText(location)




    }
    fun convertToUri(imageString: String): Uri {
        return try {
            // Check if the imagePath is a valid URI string or a file path
            if (imageString.startsWith("content://") || imageString.startsWith("file://")) {
                // It's already a URI
                Uri.parse(imageString)
            } else {
                // It's a file path, so convert it to a URI
                Uri.fromFile(File(imageString))
            }
        } finally {
            //nothing

        }
    }

}