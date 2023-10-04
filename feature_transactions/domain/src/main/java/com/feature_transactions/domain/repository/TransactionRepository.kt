package com.feature_transactions.domain.repository

import com.feature_transactions.domain.model.TransactionMonth
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface TransactionRepository {

    fun getFilteredTransactions(year: Int, months: List<Int>?,
                               isFlatSelected: Boolean, listOfSectionsId: List<Int>?,
                               incomeCatIds: List<Int>?, expensesCatIds: List<Int>?): Flow<List<TransactionMonth>>

}