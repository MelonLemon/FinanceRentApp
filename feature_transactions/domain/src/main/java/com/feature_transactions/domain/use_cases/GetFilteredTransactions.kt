package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.Currency

class GetFilteredTransactions  @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        year: Int,
        months: List<Int>?,
        categoriesIds: List<Int>?,
        currency: Currency
    ): Flow<List<TransactionMonth>>{
        return repository.getFilteredTransactions(year=year, months=months,
            categoriesIds=categoriesIds,
            currency=currency
        )

    }
}