package com.example.youtubelearning.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.youtubelearning.util.Converters
import com.example.youtubelearning.models.MyImages

@Database(entities = [MyImages::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyImagesDatabase : RoomDatabase() {

    abstract fun myImageDao(): MyImageDao

    companion object {
        @Volatile
        private var INSTANCE: MyImagesDatabase? = null

        fun getDatabase(context: Context): MyImagesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyImagesDatabase::class.java,
                    "my_images_database"
                )
                    .fallbackToDestructiveMigration() // Handle schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
