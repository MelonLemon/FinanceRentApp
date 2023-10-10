package com.feature_home.presentation.home.util

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.TransactionInfo
import java.time.YearMonth
import java.util.Currency

sealed class HomeScreenEvents{
    object OpenNewSectionDialog: HomeScreenEvents()
    data class OpenSectionDialog(val sectionInfo: SectionInfo): HomeScreenEvents()
    data class OnYearMonthChange(val yearMonth: YearMonth): HomeScreenEvents()
    data class OnCurrencyChange(val currency: Currency): HomeScreenEvents()
    data class OnNewFlatAdd(val name: String): HomeScreenEvents()
    data class OnSectionAddEdit(val sectionInfo: SectionInfo): HomeScreenEvents()
    data class OnIsIncomeSelectedSection(val index: Int, val isIncomeSelected: Boolean): HomeScreenEvents()
    data class OnCategoryIdChange(val categoryId: Int, val index: Int): HomeScreenEvents()
    data class OnTransactionAdd(val sectionId: Int, val transaction: TransactionInfo): HomeScreenEvents()
}
