package com.feature_home.domain.di

import com.feature_home.domain.repository.HomeRepository
import com.feature_home.domain.use_cases.GetFinResults
import com.feature_home.domain.use_cases.HomeUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeObject {

    @Provides
    @Singleton
    fun provideBookingUseCases(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getFinResults = GetFinResults(repository)
        )
    }

}