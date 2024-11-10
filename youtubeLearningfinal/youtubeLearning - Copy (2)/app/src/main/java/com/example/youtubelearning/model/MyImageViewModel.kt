package com.example.youtubelearning.Model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.youtubelearning.models.MyImages
import com.example.youtubelearning.room.MyImageDao
import com.example.youtubelearning.room.MyImagesDatabase
import kotlinx.coroutines.launch

class MyImageViewModel(application: Application) : AndroidViewModel(application) {

    private val myImageDao: MyImageDao = MyImagesDatabase.getDatabase(application).myImageDao()  // Correct call to getDatabase
    val allImages: LiveData<List<MyImages>> = myImageDao.getAllImages()

    // Insert new image into the database
    fun insertImage(image: MyImages) {
        viewModelScope.launch {
            myImageDao.insert(image)
        }
    }
}
