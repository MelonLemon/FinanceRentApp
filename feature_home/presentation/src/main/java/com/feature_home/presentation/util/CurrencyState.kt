package com.feature_home.presentation.util

import java.util.Currency
import java.util.Locale

data class CurrencyState(
    val listCurrency: List<Currency> = Currency.getAvailableCurrencies().toList(),
    val selectedCurrency: Currency = Currency.getInstance(Locale.US)
)
