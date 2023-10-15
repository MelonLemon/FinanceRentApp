package com.feature_transactions.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.feature_transactions.presentation.TransactionScreen
import com.feature_transactions.presentation.TransactionViewModel

const val TransactionPattern = "TransactionInfo"
const val transactionRoute = "transaction_route"


fun NavController.navigateToTransactions() {
    this.navigate(transactionRoute)
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.transactionScreen() {

    composable(route = transactionRoute) {
        val viewModel = hiltViewModel<TransactionViewModel>()
        val transactionState by viewModel.transactionState.collectAsStateWithLifecycle()
        val transactionScreenEvents = viewModel::transactionScreenEvents
        val transactionsUiEvents by viewModel::onTransactionsUiEvents
        TransactionScreen(
            transactionState=transactionState,
            transactionScreenEvents=transactionScreenEvents,
            transactionsUiEvents = transactionsUiEvents
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.transactionGraph(
    navController: NavController
) {
    navigation(
        startDestination = transactionRoute,
        route = TransactionPattern
    ) {
        transactionScreen()

    }
}

fun NavController.navigateToTransactionGraph() {
    this.navigate(TransactionPattern)
}