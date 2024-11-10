package com.example.youtubelearning.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.youtubelearning.models.MyImages
import com.example.youtubelearning.room.MyImageDao
import com.example.youtubelearning.room.MyImagesDatabase

class MyImageRepository(application: Application) {
    var myImagesDao : MyImageDao
    var imagesList : LiveData<List<MyImages>>

    init{
        val database = MyImagesDatabase.getDatabase(application)

        myImagesDao = database.myImageDao()

        imagesList = myImagesDao.getAllImages()
    }

    suspend fun insert(myImage: MyImages) {
        myImagesDao.insert(myImage)
    }

    suspend fun delete(myImage: MyImages) {
        myImagesDao.delete(myImage)
    }

    suspend fun update(myImage: MyImages) {
        myImagesDao.update(myImage)
    }

    fun getAllImages() : LiveData<List<MyImages>>{
        return imagesList
    }


}