package com.feature_home.domain.di

import com.feature_home.domain.repository.HomeRepository
import com.feature_home.domain.use_cases.AddEditGuest
import com.feature_home.domain.use_cases.AddEditSection
import com.feature_home.domain.use_cases.AddFlatExpenses
import com.feature_home.domain.use_cases.AddNewFlat
import com.feature_home.domain.use_cases.AddTransaction
import com.feature_home.domain.use_cases.GetExpensesCategories
import com.feature_home.domain.use_cases.GetFinFlatState
import com.feature_home.domain.use_cases.GetFinResults
import com.feature_home.domain.use_cases.GetFlatInfoById
import com.feature_home.domain.use_cases.GetFlatsInfo
import com.feature_home.domain.use_cases.GetGuests
import com.feature_home.domain.use_cases.GetListRentDates
import com.feature_home.domain.use_cases.GetSectionsInfo
import com.feature_home.domain.use_cases.GetTransactions
import com.feature_home.domain.use_cases.GetUpdatedTransactions
import com.feature_home.domain.use_cases.HomeUseCases
import com.feature_home.domain.use_cases.UpdatePaidStatusGuest
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
            getFinResults = GetFinResults(repository),
            getUpdatedTransactions = GetUpdatedTransactions(repository),
            addNewFlat = AddNewFlat(repository),
            addEditSection = AddEditSection(repository),
            addTransaction = AddTransaction(repository),
            getFlatsInfo = GetFlatsInfo(repository),
            getSectionsInfo = GetSectionsInfo(repository),
            updatePaidStatusGuest = UpdatePaidStatusGuest(repository),
            addEditGuest = AddEditGuest(repository),
            addFlatExpenses = AddFlatExpenses(repository),
            getFlatInfoById = GetFlatInfoById(repository),
            getGuests = GetGuests(repository),
            getTransactions = GetTransactions(repository),
            getFinFlatState = GetFinFlatState(repository),
            getListRentDates = GetListRentDates(repository),
            getExpensesCategories = GetExpensesCategories(repository)
        )
    }

}