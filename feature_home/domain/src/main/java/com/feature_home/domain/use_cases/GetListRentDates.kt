package com.feature_home.domain.use_cases

import com.feature_home.domain.model.Transaction
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetListRentDates @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(flatId: Int): Flow<List<Long>> {
        return repository.getListRentDates(flatId=flatId)
    }
}