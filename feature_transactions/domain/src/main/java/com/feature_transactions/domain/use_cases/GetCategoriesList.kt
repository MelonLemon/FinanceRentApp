package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionMonth
import com.feature_transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Currency
import javax.inject.Inject

class GetCategoriesList @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<CategoriesFilter> {
        return repository.getCategoriesList()

    }
}