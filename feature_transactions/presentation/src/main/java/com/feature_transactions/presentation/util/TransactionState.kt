package com.feature_transactions.presentation.util

import com.feature_transactions.domain.model.TransactionMonth
import java.time.YearMonth
import java.util.Currency
import java.util.Locale
import java.util.Locale.US
import java.util.Locale.setDefault

data class TransactionState(
    val searchText: String ="",
    val isDownloading: Boolean = true,
    val filterAmount: Int = 0,
    val selectedCurrency: Currency = Currency.getInstance(US),
    val transactionsByMonth: List<TransactionMonth> = emptyList(),
    val filterState: FilterState
)
