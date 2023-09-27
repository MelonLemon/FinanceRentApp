package com.feature_analytics.presentation.util

sealed class AnalyticsScreenEvents{
    data class OnYearClick(val year: Int): AnalyticsScreenEvents()
}
