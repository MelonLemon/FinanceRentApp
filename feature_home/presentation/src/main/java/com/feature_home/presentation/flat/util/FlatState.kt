package com.feature_home.presentation.flat.util

import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinFlatState
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.TransactionInfo
import com.feature_home.presentation.util.CurrencyState
import java.time.YearMonth

data class FlatState(
    val isLoading: Boolean = true,
    val flatName: String="",
    val yearMonth: YearMonth,
    val listOfYears: List<Int>,
    val finalAmount: Int = 0,
    val finResults: List<FinResultsFlat> = emptyList(),
    val guests: List<FullGuestInfo> = emptyList(),
    val listRentDates: List<Long> = emptyList(),
    val guestDialogGuestInfo: FullGuestInfo = FullGuestInfo(),
    val expensesCategories: List<FinCategory> = emptyList(),
    val selectedCategoryId: Int=-1,
    val transactionsDisplay: List<TransactionInfo> = emptyList(),
    val isIncomeSelected: Boolean = true,
    val currencyState: CurrencyState = CurrencyState()
)

data class IndependentFlatState(
    val isLoading: Boolean = true,
    val flatName: String="",
    val listOfYears: List<Int>,
    val guestDialogGuestInfo: FullGuestInfo = FullGuestInfo(),
    val expensesCategories: List<FinCategory> = emptyList(),
    val selectedCategoryId: Int=-1,
    val isIncomeSelected: Boolean = true,
    val currencyState: CurrencyState = CurrencyState()
)