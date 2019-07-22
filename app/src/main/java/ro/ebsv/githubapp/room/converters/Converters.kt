package ro.ebsv.githubapp.room.converters

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}