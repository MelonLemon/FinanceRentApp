package com.feature_analytics.domain.use_cases

import com.feature_analytics.domain.repository.AnalyticsRepository
import javax.inject.Inject

class GetIncomeStatement @Inject constructor(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(){


    }
}