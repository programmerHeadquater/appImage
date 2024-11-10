package com.example.youtubelearning.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.youtubelearning.databinding.ActivityDetailActivityBinding
import java.io.File

class DetailActivity : AppCompatActivity() {
    //this is test purpose
    private lateinit var detailActivityBinding: ActivityDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailActivityBinding = ActivityDetailActivityBinding.inflate(layoutInflater)


        val imageId: String? = intent.getStringExtra("imageId")

        val image: String? = intent.getStringExtra("image")
        val imageTitle: String? = intent.getStringExtra("imageTitle")
        val description: String? = intent.getStringExtra("description")
        val date: String? = intent.getStringExtra("date")
        val location: String? = intent.getStringExtra("location")
        val imageUri = image?.let { convertToUri(it) }



        setContentView(detailActivityBinding.root)
        detailActivityBinding.detailTitle.text = "Title : " + imageTitle
        detailActivityBinding.detailDate.text = "Date : " + date
        detailActivityBinding.detailDescription.text = description
        detailActivityBinding.detailLocation.text = "Location: " + location

        detailActivityBinding.imageView.setImageURI(imageUri)



        detailActivityBinding.detailBack.setOnClickListener{
            finish()
        }







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