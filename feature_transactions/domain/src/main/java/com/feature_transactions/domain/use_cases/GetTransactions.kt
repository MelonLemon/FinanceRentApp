package com.feature_transactions.domain.use_cases

import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactions  @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(){


    }
}