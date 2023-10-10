package com.core.data.data_source.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.core.common.util.listLongDates
import com.core.common.util.toLocalDate
import com.core.data.data_source.RentsTrack
import com.feature_home.domain.model.FullGuestInfo
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
fun getListOfTracks(fullGuestInfo: FullGuestInfo): List<RentsTrack>{
    val startDate = fullGuestInfo.start_date!!.toLocalDate()
    val startDateMonth = YearMonth.from(startDate)
    val endDate = fullGuestInfo.end_date!!.toLocalDate()
    val endDateMonth = YearMonth.from(endDate)
    val nights = ChronoUnit.DAYS.between(startDate, endDate).toInt()

    val listOfTracks = mutableListOf<RentsTrack>()
    if(startDateMonth.equals(endDateMonth)){
        val listDates = listLongDates(startDate = startDate!!.plusDays(1), endDate = endDate!!.minusDays(1))
        listOfTracks.add(
            RentsTrack(
                trackId = null,
                rentId = -1,
                year = startDateMonth.year,
                month = startDateMonth.monthValue,
                nights = nights,
                listDates = listDates,
                listHalfDates = listOf(fullGuestInfo.start_date!!, fullGuestInfo.end_date!!),
                amount = fullGuestInfo.for_all_nights,
                isPaid = fullGuestInfo.is_paid,
                transaction_id = null
            )
        )
    } else {
        val stopMonth = startDateMonth
        var stopStartDate = startDate!!.plusDays(1)
        var stopEndDate = startDateMonth.atEndOfMonth()
        while (!stopMonth.equals(endDateMonth.plusMonths(1))){
            val listDates = listLongDates(startDate = stopStartDate, endDate = stopEndDate)
            val listHalfDates = if(!stopMonth.equals(startDateMonth) && !stopMonth.equals(endDateMonth)) null else
                if(stopMonth.equals(startDateMonth)) listOf<Long>(fullGuestInfo.start_date!!)  else listOf<Long>(fullGuestInfo.end_date!!)
            val nightsPeriod = ChronoUnit.DAYS.between(startDate, endDate).toInt()
            listOfTracks.add(
                RentsTrack(
                    trackId = null,
                    rentId = -1,
                    year = stopMonth.year,
                    month = stopMonth.monthValue,
                    nights = nightsPeriod,
                    listDates = listDates,
                    listHalfDates = listHalfDates,
                    amount = fullGuestInfo.for_all_nights,
                    isPaid = fullGuestInfo.is_paid,
                    transaction_id = null
                )
            )
            stopMonth.plusMonths(1)
            stopStartDate = stopMonth.atDay(1)
            stopEndDate = if(stopMonth.equals(endDateMonth)) endDate!!.minusDays(1) else stopMonth.atEndOfMonth()
        }
    }
    return listOfTracks
}