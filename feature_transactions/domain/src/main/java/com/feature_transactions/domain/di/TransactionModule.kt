package com.feature_transactions.domain.di

import com.feature_transactions.domain.repository.TransactionRepository
import com.feature_transactions.domain.use_cases.GetCategoriesList
import com.feature_transactions.domain.use_cases.GetFilteredCategoriesId
import com.feature_transactions.domain.use_cases.GetSearchedTransactions
import com.feature_transactions.domain.use_cases.GetFilteredTransactions
import com.feature_transactions.domain.use_cases.GetBlocks
import com.feature_transactions.domain.use_cases.GetYearsList
import com.feature_transactions.domain.use_cases.TransactionUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideBookingUseCases(repository: TransactionRepository): TransactionUseCases {
        return TransactionUseCases(
            getFilteredTransactions = GetFilteredTransactions(repository),
            getSearchedTransactions = GetSearchedTransactions(),
            getFilteredCategoriesId = GetFilteredCategoriesId(),
            getCategoriesList = GetCategoriesList(repository),
            getYearsList = GetYearsList(repository),
            getBlocks = GetBlocks(repository)
        )
    }
}