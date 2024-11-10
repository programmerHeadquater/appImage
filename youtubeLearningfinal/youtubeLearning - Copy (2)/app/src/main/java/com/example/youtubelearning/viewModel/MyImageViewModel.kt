package com.example.youtubelearning.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.youtubelearning.models.MyImages
import com.example.youtubelearning.repository.MyImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyImageViewModel(application: Application): AndroidViewModel(application) {
    var repository : MyImageRepository
    var imagesList: LiveData<List<MyImages>>

    init{
        repository = MyImageRepository(application)
        imagesList = repository.getAllImages()
    }

    fun insert(myImages : MyImages) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(myImages)
    }

    fun update(myImages : MyImages) = viewModelScope.launch(Dispatchers.IO){
        repository.update(myImages)
    }

    fun delete(myImages : MyImages) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(myImages)
    }

    fun getAllImages() : LiveData<List<MyImages>>{
        return imagesList
    }
}