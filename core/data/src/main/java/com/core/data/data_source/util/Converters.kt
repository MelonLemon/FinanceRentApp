package com.core.data.data_source.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter

class Converters {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromListLongToString(intList: List<Long>): String {
        return intList.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toListLongFromString(stringList: String): List<Long> {
        return stringList.replace("[", "").replace("]", "").replace(" ", "").split(",")
            .map { it.toLong() }
    }
}