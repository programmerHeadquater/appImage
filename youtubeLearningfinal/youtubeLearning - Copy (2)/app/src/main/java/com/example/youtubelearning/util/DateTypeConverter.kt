package com.example.youtubelearning.util

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    // Convert Date to Long (milliseconds)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    // Convert Long back to Date
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
