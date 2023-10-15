package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.Currency

class GetFilteredTransactions  @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        year: Int,
        months: List<Int>?,
        categoriesIds: List<Int>?,
        currency: Currency
    ): Pair<Boolean, List<TransactionMonth>?>{
        return try {
            val transaction = repository.getFilteredTransactions(year=year, months=months,
                categoriesIds=categoriesIds,
                currency=currency
            )
            Pair(true, transaction)
        } catch (e: Exception){
            Pair(false, null)
        }

    }
}