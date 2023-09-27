package com.feature_home.domain.use_cases

import com.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class GetFinResults @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(){


    }
}