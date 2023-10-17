package com.feature_transactions.domain.use_cases

import com.core.common.util.SimpleItem
import com.feature_transactions.domain.repository.TransactionRepository
import javax.inject.Inject


class GetBlocks @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<SimpleItem>{
        return repository.getBlocks()

    }
}