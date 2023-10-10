package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetFinFlatState @Inject constructor(
    private val repository: HomeRepository
) {

    operator fun invoke(flatId: Int): Flow<List<FinResultsFlat>> {
        return repository.getFinResultFlatMonthly(flatId=flatId)
    }
}