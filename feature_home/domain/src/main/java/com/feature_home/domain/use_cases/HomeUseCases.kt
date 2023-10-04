package com.feature_home.domain.use_cases

data class HomeUseCases(
    val getFinResults: GetFinResults,
    val getUpdatedTransactions: GetUpdatedTransactions,
    val addNewFlat: AddNewFlat,
    val addEditSection: AddEditSection,
    val addTransaction: AddTransaction,
    val getFlatsInfo: GetFlatsInfo,
    val getSectionsInfo: GetSectionsInfo,
    val updatePaidStatusGuest: UpdatePaidStatusGuest,
    val addEditGuest: AddEditGuest,
    val addFlatExpenses: AddFlatExpenses,
    val getFlatInfoById: GetFlatInfoById,
    val getGuests: GetGuests,
    val getTransactions: GetTransactions,
    val getFinFlatState: GetFinFlatState,
    val getListRentDates: GetListRentDates,
    val getExpensesCategories: GetExpensesCategories
)
