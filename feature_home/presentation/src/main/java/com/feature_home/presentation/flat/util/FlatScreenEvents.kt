package com.feature_home.presentation.flat.util

import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.Transaction
import com.feature_home.presentation.home.util.HomeScreenEvents

sealed class FlatScreenEvents{
    data class OnIncomeExpensesClick(val isIncomeSelected: Boolean): FlatScreenEvents()
    data class OnPaidSwitchChange(val id: Int, val is_paid: Boolean): FlatScreenEvents()
    object OpenNewGuestDialog: FlatScreenEvents()
    data class OpenGuestDialog(val guestInfo: FullGuestInfo): FlatScreenEvents()
    data class OnGuestAddEdit(val guestInfo: FullGuestInfo): FlatScreenEvents()
    data class OnCategoryClick(val categoryId: Int): FlatScreenEvents()
    data class OnTransactionAdd(val amount: Int): FlatScreenEvents()
}
