package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.repository.HomeRepository
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGuests  @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(yearMonth: YearMonth, flatId: Int): Flow<List<FullGuestInfo>> {
        return repository.getGuests(yearMonth=yearMonth, flatId=flatId)
    }
}