package com.feature_transactions.domain.use_cases

data class TransactionUseCases(
    val getFilteredTransactions: GetFilteredTransactions,
    val getSearchedTransactions: GetSearchedTransactions
)
