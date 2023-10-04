package com.feature_home.domain.use_cases


import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetFlatInfoById  @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(flatId: Int): FlatInfo {
        return repository.getFlatInfoById(flatId)
    }
}