package com.feature_home.domain.use_cases

import com.feature_home.domain.model.TransactionInfo
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetTransactions @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(yearMonth: YearMonth, flatId: Int): Flow<List<TransactionInfo>> {
        return repository.getTransactions(yearMonth=yearMonth, flatId=flatId)
    }
}