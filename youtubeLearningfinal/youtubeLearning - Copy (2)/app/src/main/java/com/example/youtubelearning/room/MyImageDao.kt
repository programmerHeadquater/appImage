package com.example.youtubelearning.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.youtubelearning.models.MyImages

@Dao
interface MyImageDao {
    @Insert
    suspend fun insert(myImages: MyImages)

    @Update
    suspend fun update(myImages: MyImages)


    @Delete
    suspend fun delete(myImages: MyImages)

    @Query("DELETE FROM my_images WHERE imageId = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE MY_IMAGES SET imageTittle = :title, imageDescription = :description, image = :path, loction = :location, date = :date WHERE imageId = :id")
    suspend fun updateImageById(id: Int, title: String, description: String, path: String, location: String, date: String)

    @Query("SELECT * FROM my_images ORDER BY imageId ASC")
    fun getAllImages() : LiveData<List<MyImages>>
}