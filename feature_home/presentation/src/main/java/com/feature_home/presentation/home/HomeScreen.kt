package com.feature_home.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature_home.presentation.home.util.HomeScreenEvents
import com.feature_home.presentation.home.util.HomeState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.core.common.MoneyText
import com.feature_home.presentation.components.SettingsDialog
import com.feature_home.presentation.home.util.HomeUiEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    homeState: HomeState,
    homeEvents: (HomeScreenEvents) -> Unit,
    homeUiEvents: SharedFlow<HomeUiEvents>
) {
    //Dialog States
    var currencyDialogVisibility by remember{ mutableStateOf(false)}

    LaunchedEffect(key1 = true){
        homeUiEvents.collectLatest { event ->
            when(event) {
                is HomeUiEvents.CloseCurrencyDialog -> {
                    currencyDialogVisibility = false
                }

            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    MaterialTheme.colorScheme.background
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Column() {
                        MoneyText(
                            amount = homeState.finState.finalAmount,
                            currency = homeState.currencyState.selectedCurrency,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            item{
                Row() {
                    IconButton(onClick = {
                        currencyDialogVisibility = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

        }
        //Dialogs
        SettingsDialog(
            onCancel = {
                currencyDialogVisibility = false
            },
            selectedCurrency = homeState.currencyState.selectedCurrency,
            listCurrency = homeState.currencyState.listCurrency,
            onAgree = {currency ->
                homeEvents(HomeScreenEvents.OnCurrencyChange(currency))
            }
        )

    }
}