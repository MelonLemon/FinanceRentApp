package com.feature_home.domain.use_cases

import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetListRentDates @Inject constructor(
    private val repository: HomeRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(flatId: Int): Flow<List<Long>> {
        return repository.getListRentDates(flatId=flatId).mapLatest { listRents ->
            val listRentDates = mutableListOf<Long>()
            val tempRentDates = mutableListOf<Long>()
            listRents.forEach { rentDate ->
                if(rentDate.list_full_days!=null) listRentDates+=rentDate.list_full_days!!
                if(rentDate.list_full_days!=null) tempRentDates+=rentDate.list_half_days!!
            }
            val fullDates = tempRentDates.groupBy{it}.filter{ it.value.size>1 }.keys
            listRentDates + fullDates
        }
    }
}