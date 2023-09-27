package com.feature_analytics.domain.di

import com.feature_analytics.domain.repository.AnalyticsRepository
import com.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.feature_analytics.domain.use_cases.GetIncomeStatement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideBookingUseCases(repository: AnalyticsRepository): AnalyticsUseCases {
        return AnalyticsUseCases(
            getIncomeStatement = GetIncomeStatement(repository)
        )
    }

}