package com.core.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
fun listLongDates(startDate: LocalDate, endDate: LocalDate): MutableList<Long> {
    val listLongDates = mutableListOf<Long>()
    val stopDate = startDate
    while(!stopDate.isEqual(endDate.plusDays(1))){
        listLongDates.add(stopDate.toEpochDay())
        stopDate.plusDays(1)
    }
    return listLongDates
}

