package com.feature_analytics.presentation

import androidx.lifecycle.ViewModel
import com.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.feature_analytics.presentation.util.AnalyticsScreenEvents
import com.feature_analytics.presentation.util.AnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val useCases: AnalyticsUseCases
): ViewModel() {

    private val _analyticsState = MutableStateFlow((AnalyticsState()))
    val analyticsState  = _analyticsState.asStateFlow()

    fun analyticsScreenEvents(event: AnalyticsScreenEvents){
        when(event) {
            is AnalyticsScreenEvents.OnYearClick -> {

            }
        }
    }
}