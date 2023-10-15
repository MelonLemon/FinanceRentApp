package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FlatFinInfo
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListOfFlats @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(year: Int, month: Int, numDays: Int): Flow<List<FlatFinInfo>>  {
        return repository.getListOfFlats(
            year=year,
            month=month,
            numDays = numDays
        )
    }
}