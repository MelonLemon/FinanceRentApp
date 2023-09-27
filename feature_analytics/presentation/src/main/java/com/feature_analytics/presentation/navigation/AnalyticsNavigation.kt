package com.feature_analytics.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.feature_analytics.presentation.AnalyticsScreen
import com.feature_analytics.presentation.AnalyticsViewModel

const val AnalyticsPattern = "AnalyticsInfo"
const val analyticsRoute = "analytics_route"


fun NavController.navigateToAnalytics() {
    this.navigate(analyticsRoute)
}


fun NavGraphBuilder.analyticsScreen() {

    composable(route = analyticsRoute) {
        val viewModel = hiltViewModel<AnalyticsViewModel>()
        val analyticsState by viewModel.analyticsState.collectAsStateWithLifecycle()
        val analyticsEvents = viewModel::analyticsScreenEvents
        AnalyticsScreen(
            analyticsState=analyticsState,
            analyticsEvents=analyticsEvents
        )
    }
}


fun NavGraphBuilder.analyticsGraph(
    navController: NavController
) {
    navigation(
        startDestination = analyticsRoute,
        route = AnalyticsPattern
    ) {
        analyticsScreen()
    }
}

fun NavController.navigateToAnalyticsGraph() {
    this.navigate(AnalyticsPattern)
}