package com.core.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.core.common.util.SimpleItem
import com.core.common.util.toLocalDate
import com.core.data.data_source.RentCountDao
import com.feature_transactions.domain.model.AllTransactionsDay
import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionListItem
import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
    ): Flow<Map<Long, List<TransactionListItem>>> {
        Log.d("Transactions", "Begin Repository GetFilteredTransactions")


        return when {
            months==null && categoriesIds==null -> return dao.getTransactionsWithoutFilters(year=year)
            months!=null && categoriesIds!=null -> dao.getTransactionsFiltered(year=year, months=months, categoriesIds=categoriesIds)
            months!=null && categoriesIds==null -> return dao.getTransactionsFilterMonths(year=year, months=months)
            months==null && categoriesIds!=null -> dao.getTransactionsFilterCategories(year=year, categoriesIds=categoriesIds)
            else -> flowOf()
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