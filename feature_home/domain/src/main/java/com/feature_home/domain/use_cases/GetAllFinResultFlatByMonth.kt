package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFinResultFlatByMonth @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(year: Int, month: Int): Flow<FinResultsFlat?>  {
        return repository.getAllFinResultFlatByMonth(year=year, month=month)
    }
}