package com.feature_home.presentation.home.util

import java.util.Currency

sealed class HomeScreenEvents{
    data class OnYearChange(val year: Int): HomeScreenEvents()
    data class OnCurrencyChange(val currency: Currency): HomeScreenEvents()
}
