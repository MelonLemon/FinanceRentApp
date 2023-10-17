package com.feature_home.domain.use_cases

import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetListRentDates @Inject constructor(
    private val repository: HomeRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator  fun invoke(flatId: Int): List<Long> {
        return repository.getListRentDates(flatId=flatId)

    }
}