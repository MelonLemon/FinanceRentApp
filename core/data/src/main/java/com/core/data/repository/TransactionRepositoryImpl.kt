package com.core.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.core.common.util.SimpleItem
import com.core.common.util.toLocalDate
import com.core.data.data_source.RentCountDao
import com.feature_transactions.domain.model.AllTransactionsDay
import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.util.Currency

import javax.inject.Inject

class TransactionRepositoryImpl  @Inject constructor(
    private val dao: RentCountDao
): TransactionRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getFilteredTransactions(
        year: Int, months: List<Int>?,
        categoriesIds: List<Int>?,
        currency: Currency
    ): List<TransactionMonth> {
        Log.d("Transactions", "Begin Repository GetFilteredTransactions")
        val transactions = dao.getTransactions(
            year = year, months=months, categoriesIds=categoriesIds
        )
        Log.d("Transactions", "Before return Repository: $transactions")
        return if(transactions.isEmpty()){
            emptyList()
            } else {
                val monthsList = months ?: 1..12
                val daysMap = transactions.mapNotNull{ (key,value) ->
                    AllTransactionsDay(
                        date = key.toLocalDate()!!,
                        transactions = value
                    )
                }.groupBy { it.date.monthValue }
                val listTransactionMonth = monthsList.map {month ->
                    TransactionMonth(
                        year = year,
                        month = month,
                        amount = daysMap[month]?.sumOf { it.getSumOfTransactions() } ?: 0,
                        currency = currency,
                        daysList = daysMap[month] ?: emptyList()
                    )
                }
                listTransactionMonth
            }

    }

    override suspend fun getCategoriesList(): List<CategoriesFilter> {
        return dao.getCategoriesList()
    }

    override suspend fun getYearsList(): List<Int> {
        return dao.getYearsList()
    }

    override suspend fun getBlocks(): List<SimpleItem> {
        return dao.getBlocks()
    }

}