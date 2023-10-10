package com.feature_home.presentation.flat.util

import com.feature_home.domain.model.FullGuestInfo
import java.time.YearMonth

sealed class FlatScreenEvents{
    data class OnYearMonthChange(val yearMonth: YearMonth): FlatScreenEvents()
    data class OnIncomeExpensesClick(val isIncomeSelected: Boolean): FlatScreenEvents()
    data class OnPaidSwitchChange(val id: Int, val is_paid: Boolean): FlatScreenEvents()
    object OpenNewGuestDialog: FlatScreenEvents()
    data class OpenGuestDialog(val guestInfo: FullGuestInfo): FlatScreenEvents()
    data class OnGuestAddEdit(val guestInfo: FullGuestInfo): FlatScreenEvents()
    data class OnCategoryClick(val categoryId: Int): FlatScreenEvents()
    data class OnTransactionAdd(val amount: Int): FlatScreenEvents()
}
