package com.feature_home.domain.use_cases

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetSumOfAllTransactions @Inject constructor(
    private val repository: HomeRepository
) {
     operator fun invoke(): Flow<Int?> {
        return repository.getSumOfAllTransactions()
    }
}