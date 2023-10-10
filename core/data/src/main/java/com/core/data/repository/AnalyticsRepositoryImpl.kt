package com.core.data.repository

import com.core.data.data_source.RentCountDao
import com.core.data.data_source.RentCountDatabase
import com.feature_analytics.domain.repository.AnalyticsRepository
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val dao: RentCountDao
): AnalyticsRepository {

}