package com.feature_home.presentation.home.util

import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.Transaction
import java.util.Currency

sealed class HomeScreenEvents{
    object OpenNewSectionDialog: HomeScreenEvents()
    data class OpenSectionDialog(val sectionInfo: SectionInfo): HomeScreenEvents()
    data class OnYearChange(val year: Int): HomeScreenEvents()
    data class OnCurrencyChange(val currency: Currency): HomeScreenEvents()
    data class OnNewFlatAdd(val name: String, val city: String): HomeScreenEvents()
    data class OnSectionAddEdit(val sectionInfo: SectionInfo): HomeScreenEvents()
    data class OnIsIncomeSelectedSection(val index: Int, val isIncomeSelected: Boolean): HomeScreenEvents()
    data class OnCategoryIdChange(val categoryId: Int, val index: Int): HomeScreenEvents()
    data class OnTransactionAdd(val sectionId: Int?, val transaction: Transaction): HomeScreenEvents()
}
