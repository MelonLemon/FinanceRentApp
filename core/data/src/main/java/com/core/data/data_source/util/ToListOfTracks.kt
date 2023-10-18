package com.core.data.data_source.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.core.common.util.toLocalDate
import com.core.data.data_source.RentsTrack
import com.feature_home.domain.model.FullGuestInfo
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun ToListOfTracks(
    startDate: LocalDate,
    endDate: LocalDate,
    startMonth: YearMonth,
    endMonth: YearMonth,
    nights: Int,
    forAllNights: Int,
    forOneNight: Int,
    isPaid: Boolean
): List<RentsTrack>{
    val listOfTracks = mutableListOf<RentsTrack>()
    var stopMonth = startMonth.minusMonths(1)
    var lastAmount = forAllNights
    var nightsCheck = 0
    while(stopMonth!=endMonth){
        var nightsPeriod = 0
        var amount = 0
        stopMonth = stopMonth.plusMonths(1)
        when {
            stopMonth == startMonth && stopMonth!=endMonth -> {
                nightsPeriod = ChronoUnit.DAYS.between(startDate, stopMonth.atEndOfMonth()).toInt() + 1
                amount = nightsPeriod*forOneNight
                lastAmount -=amount
                nightsCheck +=nightsPeriod
            }
            stopMonth != startMonth && stopMonth!=endMonth -> {
                nightsPeriod = ChronoUnit.DAYS.between(stopMonth.atDay(1), stopMonth.atEndOfMonth()).toInt() + 1
                amount = nightsPeriod*forOneNight
                lastAmount -=amount
                nightsCheck +=nightsPeriod
            }
            stopMonth == endMonth && stopMonth!=startMonth -> {
                nightsPeriod = ChronoUnit.DAYS.between(stopMonth.atDay(1), endDate).toInt()
                amount = lastAmount
                lastAmount -= amount
                nightsCheck +=nightsPeriod
            }
        }
        listOfTracks.add(
            RentsTrack(
                trackId = null,
                rentId = -1,
                year=stopMonth.year,
                month=stopMonth.monthValue,
                nights = nightsPeriod,
                amount = amount,
                isPaid = isPaid,
                transaction_id = null
            )
        )

    }
    Log.d("Dates",
        if(nightsCheck==nights) "Nights check right" else "Nights check Wrong: nights: $nights and nightsCheck: $nightsCheck"
    )
    return listOfTracks
}