package com.feature_transactions.presentation.util

sealed class TransactionsUiEvents{
    object CloseFilterBottomSheet:TransactionsUiEvents()
    object CloseFilterBottomSheetWithError:TransactionsUiEvents()
    object ErrorMsgUnknown: TransactionsUiEvents()
}
