package com.feature_home.domain.use_cases

data class HomeUseCases(
    val getFinResults: GetFinResults,
    val getUpdatedTransactions: GetUpdatedTransactions,
    val addNewFlat: AddNewFlat,
    val editSection: EditSection,
    val addTransaction: AddTransaction,
    val getFlatsInfo: GetFlatsInfo,
    val getSectionsInfo: GetSectionsInfo,
    val updatePaidStatusGuest: UpdatePaidStatusGuest,
    val editGuest: EditGuest,
    val addFlatExpenses: AddFlatExpenses,
    val getFlatNameById: GetFlatNameById,
    val getGuests: GetGuests,
    val getTransactions: GetTransactions,
    val getFinFlatState: GetFinFlatState,
    val getListRentDates: GetListRentDates,
    val getExpensesCategories: GetExpensesCategories,
    val addNewSection: AddNewSection,
    val addNewGuest: AddNewGuest
)
