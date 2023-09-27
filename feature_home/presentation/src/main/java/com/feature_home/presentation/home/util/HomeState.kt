package com.feature_home.presentation.home.util

data class HomeState(
    val name:String="",
    val finState: FinState = FinState(),
    val currencyState: CurrencyState  = CurrencyState()
)
