package com.feature_home.domain.use_cases


import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetFlatNameById  @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int): String {
        return repository.getBlockNameById(flatId)
    }
}