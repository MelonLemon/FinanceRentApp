package com.core.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.util.toRange
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.core.common.util.listLongDates
import com.core.common.util.toLocalDate
import com.core.data.data_source.Blocks
import com.core.data.data_source.RentCountDao
import com.core.data.data_source.Rents
import com.core.data.data_source.RentsTrack
import com.core.data.data_source.Transactions
import com.core.data.data_source.util.FLAT_CATEGORY
import com.core.data.data_source.util.SECTION_CATEGORY
import com.feature_home.domain.model.AdditionalInfo
import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FlatFinInfo
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.RentDates
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.TransactionInfo

import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dao: RentCountDao
): HomeRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUpdatedTransactions(
        yearMonth: YearMonth,
        listOfSections: List<SectionInfo>
    ): List<SectionInfo> {
        val blocksId = listOfSections.map { it.id }
        if(blocksId.contains(null)){
            throw Exception("Section id can't be null!")
        }
        val newTransactions = dao.getSectionTransactionsBYMonth(year=yearMonth.year, month=yearMonth.monthValue, blocksId= blocksId as List<Int>)
        val newListOfSections = listOfSections.map {
            it.copy(
                transactionsDisplay = newTransactions[it.id] ?: emptyList()
            )
        }
        return newListOfSections
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addNewFlat(name: String)  {
        dao.addBasic(
            name = name,
            blockCategory = FLAT_CATEGORY,
            incomeCategories = listOf(
                FinCategory(
                    id = 1,
                    standard_category_id = IncomeCategories.RENT_INCOME.id,
                    name = IncomeCategories.RENT_INCOME.name
                )
            ),
            expCategories = listOf(
                FinCategory(
                    id = 2,
                    standard_category_id = ExpensesCategories.HOUSING.id,
                    name = ExpensesCategories.HOUSING.name
                ),
                FinCategory(
                    id = 3,
                    standard_category_id = ExpensesCategories.UTILITIES.id,
                    name =  ExpensesCategories.UTILITIES.name
                ),
                FinCategory(
                    id = 4,
                    standard_category_id = ExpensesCategories.CLEANING_SERVICES.id,
                    name = ExpensesCategories.CLEANING_SERVICES.name
                ),
                FinCategory(
                    id = 4,
                    standard_category_id = ExpensesCategories.OTHERS.id,
                    name = ExpensesCategories.OTHERS.name
                )
            )
        )
    }

    override suspend fun editSection(
        blockId: Int,
        newName: String?,
        newIncomeCategories: List<FinCategory>?,
        newExpCategories: List<FinCategory>?,
        year: Int,
        month: Int
    ): List<SectionInfo> {
        return dao.editSection(
            blockId=blockId,
            newName=newName,
            newIncomeCategories=newIncomeCategories,
            newExpCategories=newExpCategories,
            year = year,
            month = month
        )

    }

    override suspend fun addNewSection(
        name: String,
        incomeCategories: List<FinCategory>?,
        expCategories: List<FinCategory>?,
        year: Int,
        month: Int
    ): List<SectionInfo> {
        return dao.addNewSection(
            name=name,
            incomeCategories=incomeCategories,
            expCategories=expCategories,
            year = year,
            month = month
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addTransaction(
        sectionId: Int,
        currency_name: String,
        transaction: TransactionInfo,
        year: Int,
        month: Int
    ): List<SectionInfo> {
        val currentDate = LocalDate.now().toEpochDay()
        return dao.addTransaction(
            year = year,
            month = month,
            transaction = Transactions(
                transactionId = null,
                blockId = sectionId,
                categoryId = transaction.categoryId,
                amount = transaction.amount,
                currency_name = currency_name,
                year=year,
                month=month,
                currentDate=currentDate,
                comment="$month/$year"
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinResults(): FinState {
        val currentMonth = YearMonth.now()
        val month = currentMonth.monthValue
        val year = currentMonth.year
        val finalAmount = dao.getSumOfAllTransactions()
        val finResultFlat = dao.getFinResultFlat(
            yearMonth = currentMonth,
            numDays = currentMonth.lengthOfMonth(),
            year=year,
            month=month)
        val finResultsSections = dao.finResultsSections(
            category = SECTION_CATEGORY,
            year=year,
            month=month
        )
        return FinState(
            finalAmount = finalAmount.first() ?: 0,
            finResultFlat = finResultFlat,
            finResultsSections = finResultsSections
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFlatsInfo(): List<FlatInfo> {
        val currentMonth = YearMonth.now()
        val daysInMonths = currentMonth.lengthOfMonth()
        val (blocks, guests, amountFlat) = dao.getFlatsInfo(
            year = currentMonth.year,
            month = currentMonth.monthValue,
            start_date = currentMonth.atDay(1).toEpochDay(),
            end_date = currentMonth.atEndOfMonth().toEpochDay(),
            flatCategory = FLAT_CATEGORY
        )
        val listOfFlatInfo = mutableListOf<FlatInfo>()

        blocks.forEach { flat ->
            val numberOfNights = guests[flat.blockId!!]?.sumOf { it.nights } ?:0  // change later
            val rentPercent = if(numberOfNights==0) 0f else  (numberOfNights/daysInMonths).toFloat()
            listOfFlatInfo.add(
                FlatInfo(
                    id = flat.blockId!!,
                    name = flat.name,
                    additionalInfo = guests[flat.blockId!!]?.map { "${it.name} - ${it.nights} - ${if(!it.isPaid) "X" else "P"}" } ?: emptyList(),
                    current_month_amount = amountFlat[flat.blockId!!] ?: 0,
                    rent_percent =  rentPercent
                )
            )
        }
        return listOfFlatInfo
    }

    override suspend fun getBlockNameById(blockId: Int): String {
        return dao.getBlockNameById(block_id=blockId)
    }

    override suspend fun getSectionsInfo(year:Int, month:Int): List<SectionInfo> {
        return dao.getListSections(
            sectionCategory = SECTION_CATEGORY,
            year = year,
            month = month
        )
    }

    override fun getFinResultsSectionsFlow(
        year: Int,
        month: Int
    ): Flow<List<FinResultsSection>> {
        return dao.finResultsSectionsFlow(
            category=SECTION_CATEGORY,
            year = year,
            month = month
        )
    }

    override fun getAllFinResultFlatByMonth(
        year: Int,
        month: Int,
        rentPercent: Float
    ): Flow<FinResultsFlat?> {
        return dao.getAllFinResultFlatByMonth(year=year, month=month, rentPercent=0f)
    }

    override fun getListOfFlats(
        year: Int,
        month: Int,
        numDays: Int
    ): Flow<List<FlatFinInfo>> {
        return dao.getListOfFlats(
            flatCategory=FLAT_CATEGORY,
            year = year,
            month = month,
            numDays = numDays
        )
    }

    override fun getSumOfAllTransactions(): Flow<Int?> {
        return dao.getSumOfAllTransactions()
    }

    override fun getFlatsAdditionalInfo(
        year: Int,
        month: Int
    ): Flow<Map<Int, List<AdditionalInfo>>> {
        return dao.getFlatsAdditionalInfo(
            year=year,
            month=month
        )
    }

    override suspend fun getExpensesCategories(flatId: Int): List<FinCategory> {
        return dao.getExpCatById(block_id = flatId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updatePaidStatusGuest(flatId: Int, guestId: Int, status: Boolean, currency_name: String) {
        val currentDate = LocalDate.now().toEpochDay()
        dao.updatePaidStatusGuest(rent_id = guestId, status=status, currency_name=currency_name, currentDate=currentDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getGuests(yearMonth: YearMonth, flatId: Int): Flow<List<FullGuestInfo>> {
        return dao.getAllRentsMonth(block_id = flatId, year=yearMonth.year, month=yearMonth.monthValue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTransactions(yearMonth: YearMonth, flatId: Int): Flow<List<TransactionInfo>> {
        return dao.getTransactionsByBlockIdMonth(year=yearMonth.year, month=yearMonth.monthValue, block_id = flatId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFinResultFlatMonthly(flatId: Int): Flow<List<FinResultsFlat>> {
        return dao.getFinResultFlatMonthly(flatId, year= Year.now().value)
    }

    override suspend fun getListRentDates(flatId: Int): List<Long> {
        return dao.getAllRentDates(flatId=flatId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addNewGuest(flatId: Int, fullGuestInfo: FullGuestInfo, currency_name: String, month: YearMonth) {
        if(fullGuestInfo.start_date==null || fullGuestInfo.end_date==null){
            throw Exception("Dates can't be null for guest")
        }
        val startDate = fullGuestInfo.start_date!!.toLocalDate()
        val endDate = fullGuestInfo.end_date!!.toLocalDate()
        val currentDate = LocalDate.now().toEpochDay()
        val nights = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        val listOfTracks = mutableListOf<RentsTrack>()
        if(YearMonth.from(startDate)==YearMonth.from(endDate)) {
            if (startDate != null) {
                dao.addNewGuest(
                    rents = Rents(
                        rentId = null,
                        blockId =  flatId,
                        name = fullGuestInfo.name,
                        phone = fullGuestInfo.phone,
                        comment = fullGuestInfo.comment,
                        startDate = fullGuestInfo.start_date!!,
                        endDate = fullGuestInfo.end_date!!,
                        forNight = fullGuestInfo.for_night,
                        forAllNights = fullGuestInfo.for_all_nights,
                        nights = nights,
                        isPaid = fullGuestInfo.is_paid),
                    currency_name = currency_name,
                    currentDate=currentDate,
                    month=startDate.monthValue,
                    year=startDate.year
                )
                Log.d("New Guest", "Add new Guest successfully!")
            }

        } else {

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun editGuest(flatId: Int, fullGuestInfo: FullGuestInfo, oldFullGuestInfo: FullGuestInfo, currency_name: String, month: YearMonth) {
        if(fullGuestInfo.start_date==null || fullGuestInfo.end_date==null || oldFullGuestInfo.start_date==null || oldFullGuestInfo.end_date==null ){
            throw Exception("Dates can't be null for guest")
        }
        val startDate = fullGuestInfo.start_date!!.toLocalDate()
        val endDate = fullGuestInfo.start_date!!.toLocalDate()
        val oldStartDate = fullGuestInfo.start_date!!.toLocalDate()
        val oldEndDate = fullGuestInfo.start_date!!.toLocalDate()
        val nights = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        val currentDate = LocalDate.now().toEpochDay()
        val newRent = Rents(
            rentId = fullGuestInfo.id,
            blockId =  flatId,
            name = fullGuestInfo.name,
            phone = fullGuestInfo.phone,
            comment = fullGuestInfo.comment,
            startDate = fullGuestInfo.start_date!!,
            endDate = fullGuestInfo.end_date!!,
            forNight = fullGuestInfo.for_night,
            forAllNights = fullGuestInfo.for_all_nights,
            nights = nights,
            isPaid = fullGuestInfo.is_paid)
        if(startDate!!.isEqual(oldStartDate) && endDate!!.isEqual(oldEndDate)){
            dao.renewRentInfo(rents = newRent,
                isAmountChanged = fullGuestInfo.for_all_nights == oldFullGuestInfo.for_all_nights,
                isStatusChanged = fullGuestInfo.is_paid == oldFullGuestInfo.is_paid,
                currency_name=currency_name,
                currentDate= currentDate
            )
        } else{
            val listOfTracks = mutableListOf<RentsTrack>() //change
            dao.renewRentDates(
                rents = newRent,
                currency_name = currency_name,
                listOfTracks = listOfTracks,
                currentDate= currentDate
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addFlatExpenses(flatId: Int, expensesCategoryId: Int, amount: Int, currency_name: String, month: YearMonth) {
        val currentDate = LocalDate.now().toEpochDay()
        dao.addNewTransaction(
            Transactions(
                transactionId = null,
                blockId = flatId,
                categoryId = expensesCategoryId,
                amount = amount,
                currency_name = currency_name,
                month = month.monthValue,
                year = month.year,
                currentDate=currentDate,
                comment = "${month.monthValue}/${month.year}"
            )
        )
    }

}