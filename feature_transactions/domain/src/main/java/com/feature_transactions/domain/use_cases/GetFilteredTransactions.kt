package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
class GetFilteredTransactions  @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        year: Int,
        months: List<Int>?,
        isFlatSelected: Boolean,
        listOfSectionsId: List<Int>?,
        incomeCatIds: List<Int>?,
        expensesCatIds: List<Int>?
    ): Flow<List<TransactionMonth>>{
        return repository.getFilteredTransactions(year=year, months=months,
            isFlatSelected=isFlatSelected, listOfSectionsId=listOfSectionsId,
            incomeCatIds=incomeCatIds, expensesCatIds=expensesCatIds)

    }
}