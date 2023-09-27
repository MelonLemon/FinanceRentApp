package com.feature_home.presentation.home.util

import java.util.Currency

data class CurrencyState(
    val listCurrency: List<Currency> = Currency.getAvailableCurrencies().toList(),
    val selectedCurrency: Currency = listCurrency[0]
)
