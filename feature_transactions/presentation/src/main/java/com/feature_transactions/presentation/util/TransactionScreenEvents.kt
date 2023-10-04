package com.feature_transactions.presentation.util

sealed class TransactionScreenEvents{
    data class OnSearchTextChanged(val text: String): TransactionScreenEvents()
    object OnSearchCancelClicked: TransactionScreenEvents()

    data class OnApplyFilterChanges(val periodFilterState: PeriodFilterState,
                                    val categoryFilterState: CategoryFilterState,
                                    val sectionsFilterState: SectionsFilterState): TransactionScreenEvents()

}
