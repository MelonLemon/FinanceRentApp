package com.feature_home.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.feature_home.presentation.flat.FlatScreen
import com.feature_home.presentation.flat.FlatViewModel
import com.feature_home.presentation.home.HomeScreen
import com.feature_home.presentation.home.HomeViewModel

const val HomePattern = "HomeInfo"
const val homeRoute = "home_route"
const val flatRoute = "flat_route"

fun NavController.navigateToHome() {
    this.navigate(homeRoute)
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeScreen(
    toFlatScreen: (Int) -> Unit
) {

    composable(route = homeRoute) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()
        val homeEvents = viewModel::homeScreenEvents
        val homeUiEvents by viewModel::onHomeUiEvents
        HomeScreen(
            homeState=homeState,
            homeEvents=homeEvents,
            homeUiEvents=homeUiEvents,
            toFlatScreen=toFlatScreen
        )
    }
}

fun NavController.navigateToFlat(flatId: Int) {
    this.navigate("${flatRoute}/$flatId")
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.flatScreen(
    backToHomeScreen: () -> Unit
) {

    composable(
        route = "${flatRoute}/{flatId}",
        arguments = listOf(
            navArgument("flatId") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        val viewModel = hiltViewModel<FlatViewModel>()
        val flatState by viewModel.flatState.collectAsStateWithLifecycle()
        val flatEvents = viewModel::flatScreenEvents
        val flatUiEvents by viewModel::onFlatUiEvents
        FlatScreen(
            flatState=flatState,
            flatEvents=flatEvents,
            flatUiEvents=flatUiEvents,
            backToHomeScreen=backToHomeScreen
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        startDestination = homeRoute,
        route = HomePattern
    ) {
        homeScreen(
            toFlatScreen = {id ->
                navController.navigateToFlat(id)
            }
        )
        flatScreen(
           backToHomeScreen = {
               navController.popBackStack()
           }
        )

    }
}

fun NavController.navigateToHomeGraph() {
    this.navigate(HomePattern)
}