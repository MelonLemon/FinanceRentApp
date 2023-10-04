package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetTransactions @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(yearMonth: YearMonth, flatId: Int): Flow<List<Transaction>> {
        return repository.getTransactions(yearMonth=yearMonth, flatId=flatId)
    }
}