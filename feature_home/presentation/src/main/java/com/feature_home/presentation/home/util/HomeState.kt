package com.feature_home.presentation.home.util

import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FlatInfo
import com.feature_home.domain.model.SectionInfo
import com.feature_home.presentation.util.CurrencyState
import java.time.YearMonth

data class HomeState(
    val name:String="",
    val yearMonth: YearMonth,
    val listOfFlat: List<FlatInfo> = emptyList(),
    val finResultFlat: FinResultsFlat,
    val finResultsSections: List<FinResultsSection> = emptyList(),
    val listOfSections: List<SectionInfo> = emptyList(),
    val sectionDialogSectionInfo: SectionInfo = SectionInfo(),
    val finState: FinState = FinState(),
    val currencyState: CurrencyState = CurrencyState()
)
