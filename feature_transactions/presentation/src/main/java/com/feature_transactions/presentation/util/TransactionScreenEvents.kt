package com.feature_transactions.presentation.util

sealed class TransactionScreenEvents{
    data class OnSearchTextChanged(val text: String): TransactionScreenEvents()
}
