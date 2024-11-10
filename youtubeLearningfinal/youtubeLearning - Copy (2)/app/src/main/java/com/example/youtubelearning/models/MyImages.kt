package com.example.youtubelearning.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "my_images")
class MyImages(
    val imageTittle : String,
    val imageDescription : String,
    val image : String,
    val loction : String,
    val date : String


) {
    @PrimaryKey(autoGenerate = true)
    var imageId = 0

}