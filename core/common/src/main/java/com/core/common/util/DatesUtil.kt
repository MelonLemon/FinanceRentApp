package com.core.common.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
fun listLongDates(startDate: LocalDate, endDate: LocalDate): MutableList<Long> {
    if(startDate.isBefore(endDate)){
        val listLongDates = mutableListOf<Long>()
        val stopDate = startDate.minusDays(1)
        while(stopDate!=endDate){
            stopDate.plusDays(1)
            listLongDates.add(stopDate.toEpochDay())
        }
        Log.d("New Guest", "List long:$listLongDates")
        return listLongDates
    } else {
        throw Exception("Start date should be before endDate!")
    }
}

