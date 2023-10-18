package com.feature_transactions.domain.use_cases

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.core.common.util.toLocalDate
import com.feature_transactions.domain.model.AllTransactionsDay
import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.util.Currency

class GetFilteredTransactions  @Inject constructor(
    private val repository: TransactionRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        year: Int,
        months: List<Int>?,
        categoriesIds: List<Int>?,
        currency: Currency
    ): Flow<List<TransactionMonth>>{
        return repository.getFilteredTransactions(year=year, months=months,
            categoriesIds=categoriesIds,
            currency=currency
        ).mapLatest { transactions ->
            if(transactions.isEmpty()){
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

    }
}