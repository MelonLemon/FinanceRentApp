package com.feature_home.domain.repository

import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.Transaction
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getUpdatedTransactions(yearMonth: YearMonth, listOfSections: List<SectionInfo>): List<SectionInfo>
    suspend fun addNewFlat(name:String, city: String):List<FlatInfo>
    suspend fun addEditSection(sectionInfo: SectionInfo):List<SectionInfo>
    suspend fun addEditTransactions(sectionId: Int, transaction: Transaction):List<SectionInfo>
    suspend fun getFinResults(): FinState
    suspend fun getFlatsInfo():List<FlatInfo>
    suspend fun getFlatInfoById(flatId: Int):FlatInfo
    suspend fun getSectionsInfo():List<SectionInfo>

    suspend fun getExpensesCategories(flatId: Int):List<FinCategory>
    suspend fun updatePaidStatusGuest(flatId: Int, guestId:Int, status: Boolean)
    fun getGuests(yearMonth: YearMonth, flatId: Int): Flow<List<FullGuestInfo>>
    fun getTransactions(yearMonth: YearMonth, flatId: Int): Flow<List<Transaction>>
    fun getGetFinFlatState(flatId: Int): Flow<FinFlatState>
    fun getListRentDates(flatId: Int): Flow<List<Long>>

    suspend fun addEditGuest(flatId: Int, fullGuestInfo: FullGuestInfo, month:YearMonth)
    suspend fun addFlatExpenses(flatId: Int, expensesCategoryId: Int, amount: Int)


}