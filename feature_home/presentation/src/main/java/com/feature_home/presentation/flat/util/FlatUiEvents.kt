package com.feature_home.presentation.flat.util

import com.feature_home.presentation.home.util.HomeUiEvents

sealed class FlatUiEvents{
    object OpenGuestDialog: FlatUiEvents()
    object CloseGuestDialog: FlatUiEvents()
    object CloseGuestDialogWithError: FlatUiEvents()
    object CloseYearMonthDialog: FlatUiEvents()

    object ErrorMsgUnknown: FlatUiEvents()
}
