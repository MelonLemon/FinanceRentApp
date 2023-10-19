package com.feature_home.domain.use_cases

import com.feature_home.domain.model.FlatFinInfo
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetFlatsAdditionalInfo @Inject constructor(
    private val repository: HomeRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(year: Int, month: Int): Flow<Map<Int, List<String>>> {
        return repository.getFlatsAdditionalInfo(
            year=year,
            month=month
        ).mapLatest { info ->
            info.mapValues { (_, item) ->
                val listString = item.map {addInfo ->
                    "${addInfo.rent_name} - ${addInfo.nights} nights"
                }
                listString
            }
        }
    }
}