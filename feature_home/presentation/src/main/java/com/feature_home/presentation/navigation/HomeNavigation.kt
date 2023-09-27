package com.feature_home.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.feature_home.presentation.home.HomeScreen
import com.feature_home.presentation.home.HomeViewModel

const val HomePattern = "HomeInfo"
const val homeRoute = "home_route"


fun NavController.navigateToHome() {
    this.navigate(homeRoute)
}


fun NavGraphBuilder.homeScreen() {

    composable(route = homeRoute) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()
        val homeEvents = viewModel::homeScreenEvents
        val homeUiEvents by viewModel::onHomeUiEvents
        HomeScreen(
            homeState=homeState,
            homeEvents=homeEvents,
            homeUiEvents=homeUiEvents
        )
    }
}


fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        startDestination = homeRoute,
        route = HomePattern
    ) {
        homeScreen()

    }
}

fun NavController.navigateToHomeGraph() {
    this.navigate(HomePattern)
}