package com.feature_transactions.domain.use_cases

data class TransactionUseCases(
    val getFilteredTransactions: GetFilteredTransactions,
    val getSearchedTransactions: GetSearchedTransactions,
    val getFilteredCategoriesId: GetFilteredCategoriesId,
    val getCategoriesList: GetCategoriesList,
    val getYearsList: GetYearsList,
    val getFlatsSections: GetFlatsSections
)
