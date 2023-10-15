package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FinState
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinResultsSections @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(year: Int, month: Int): Flow<List<FinResultsSection>> {
        return repository.getFinResultsSectionsFlow(
            year=year,month=month
        )
    }
}
