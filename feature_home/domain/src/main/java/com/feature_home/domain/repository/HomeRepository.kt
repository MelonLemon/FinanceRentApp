package com.feature_home.domain.repository

import com.feature_home.domain.model.AdditionalInfo
import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FlatFinInfo
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.RentDates
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.TransactionInfo
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getUpdatedTransactions(yearMonth: YearMonth, listOfSections: List<SectionInfo>): List<SectionInfo>
    suspend fun addNewFlat(name:String)
    suspend fun editSection(blockId: Int, newName:String?,
                            newIncomeCategories:List<FinCategory>?,
                            newExpCategories:List<FinCategory>?,
                            year: Int,
                            month: Int
    ):List<SectionInfo>
    suspend fun addNewSection(name:String,
                              incomeCategories:List<FinCategory>?,
                              expCategories:List<FinCategory>?,
                              year: Int,
                              month: Int
    ):List<SectionInfo>
    suspend fun addTransaction(sectionId: Int, currency_name: String, transaction: TransactionInfo, year: Int,
                               month: Int, listOfSections:List<SectionInfo>):List<SectionInfo>
    suspend fun getFinResults(): FinState
    suspend fun getFlatsInfo():List<FlatInfo>
    suspend fun getBlockNameById(blockId: Int):String
    suspend fun getSectionsInfo(year:Int, month:Int):List<SectionInfo>
    fun getFinResultsSectionsFlow(year: Int, month: Int): Flow<List<FinResultsSection>>
    fun getAllFinResultFlatByMonth(year: Int, month: Int, rentPercent: Float = 0f): Flow<FinResultsFlat?>
    fun getListOfFlats(year: Int, month: Int, numDays: Int): Flow<List<FlatFinInfo>>
    fun getSumOfAllTransactions(): Flow<Int?>

    fun getFlatsAdditionalInfo(year: Int, month: Int): Flow<Map<Int, List<AdditionalInfo>>>
    suspend fun getExpensesCategories(flatId: Int):List<FinCategory>
    suspend fun updatePaidStatusGuest(flatId: Int, guestId:Int, status: Boolean, currency_name: String)
    fun getGuests(yearMonth: YearMonth, flatId: Int): Flow<List<FullGuestInfo>>
    fun getTransactions(yearMonth: YearMonth, flatId: Int): Flow<List<TransactionInfo>>
    fun getFinResultFlatMonthly(flatId: Int): Flow<List<FinResultsFlat>>
    suspend fun getListRentDates(flatId: Int): List<Long>

    suspend fun addNewGuest(flatId: Int, fullGuestInfo: FullGuestInfo, currency_name: String,  month:YearMonth)
    suspend fun editGuest(flatId: Int, fullGuestInfo: FullGuestInfo, oldFullGuestInfo: FullGuestInfo, currency_name: String, month:YearMonth)
    suspend fun addFlatExpenses(flatId: Int, expensesCategoryId: Int, amount: Int, currency_name: String, month: YearMonth)


}