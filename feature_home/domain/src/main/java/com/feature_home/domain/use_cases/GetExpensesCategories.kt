package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesCategories @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int): List<FinCategory> {
        return repository.getExpensesCategories(flatId=flatId)
    }
}