package com.feature_transactions.domain.use_cases

import com.core.common.util.SimpleItem
import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject


class GetFlatsSections @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Pair<List<SimpleItem>, List<SimpleItem>>{
        return repository.getFlatsSections()

    }
}