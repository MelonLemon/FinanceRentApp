package com.feature_home.presentation.flat.util

import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.presentation.util.CurrencyState
import java.util.Currency

data class FlatState(
    val flatInfo: FlatInfo?=null,
    val finResults: List<FinResultsFlat> = emptyList(),
    val guests: List<FullGuestInfo> = emptyList(),
    val guestDialogGuestInfo: FullGuestInfo = FullGuestInfo(),
    val finalAmount: Int =0,
    val listRentDates: List<Long> = emptyList(),
    val expensesCategories: List<FinCategory> = emptyList(),
    val selectedCategoryId: Int?=null,
    val transactionsDisplay: List<Transaction> = emptyList(),
    val isIncomeSelected: Boolean = true,
    val currencyState: CurrencyState = CurrencyState()
)
