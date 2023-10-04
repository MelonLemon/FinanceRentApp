package com.feature_home.presentation.home.util

import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FinState
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.presentation.util.CurrencyState
import java.time.YearMonth

data class HomeState(
    val isLoading: Boolean = true,
    val yearMonth: YearMonth,
    val listOfYears: List<Int>,
    val listOfFlat: List<FlatInfo> = emptyList(),
    val listOfSections: List<SectionInfo> = emptyList(),
    val sectionDialogSectionInfo: SectionInfo = SectionInfo(),
    val finState: FinState,
    val currencyState: CurrencyState = CurrencyState()
)
