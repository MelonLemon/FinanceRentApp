package com.feature_transactions.presentation.util

import com.core.common.util.SimpleItem
import com.feature_transactions.domain.model.CategoriesFilter

data class FilterState(
    val categoriesFilterList: List<CategoriesFilter> = emptyList(),
    val periodFilterState: PeriodFilterState,
    val sectionsFilterState: SectionsFilterState=SectionsFilterState(),
    val categoryFilterState: CategoryFilterState=CategoryFilterState()
)

data class PeriodFilterState(
    val years: List<Int>,
    val selectedYear: Int,
    val isAllMonthsSelected: Boolean = true,
    val months: List<Int> = emptyList()
)

data class SectionsFilterState(
    val listOfBlocks: List<SimpleItem> = emptyList(),
    val listOfSelectedBlIds: List<Int> = emptyList(),
    val isAllSelected: Boolean = true
)

data class CategoryFilterState(
    val isAllSelected: Boolean = true,
    val isAllIncomeSelected: Boolean = true,
    val isAllExpensesSelected: Boolean = true,
    val selectedIncomeCatId: List<Int> = emptyList(),
    val selectedExpensesCatId: List<Int> = emptyList(),
)