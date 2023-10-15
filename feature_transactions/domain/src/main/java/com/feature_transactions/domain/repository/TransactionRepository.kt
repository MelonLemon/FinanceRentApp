package com.feature_transactions.domain.repository

import com.core.common.util.SimpleItem
import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionMonth
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import java.util.Currency

interface TransactionRepository {

    suspend fun getFilteredTransactions(year: Int, months: List<Int>?,
                                categoriesIds: List<Int>?, currency: Currency
    ): List<TransactionMonth>

    suspend fun getCategoriesList(): List<CategoriesFilter>
    suspend fun getYearsList(): List<Int>
    suspend fun getFlatsSections(): Pair<List<SimpleItem>, List<SimpleItem>>

}