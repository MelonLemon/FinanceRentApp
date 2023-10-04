package com.feature_home.presentation.home.util

sealed class HomeUiEvents{
    object CloseSettingsDialog: HomeUiEvents()
    object CloseNewFlatDialog: HomeUiEvents()
    object CloseSectionDialog: HomeUiEvents()
    object OpenSectionDialog: HomeUiEvents()
    object CloseYearMonthDialog: HomeUiEvents()
    object ErrorMsgUnknown: HomeUiEvents()
}
