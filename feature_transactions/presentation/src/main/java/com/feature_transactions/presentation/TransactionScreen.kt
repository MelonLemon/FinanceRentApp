package com.feature_transactions.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.core.common.components.CloseNavigationRow
import com.core.common.components.MoneyText
import com.core.common.components.SearchInput
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.feature_transactions.presentation.components.CategoriesFilterWidget
import com.feature_transactions.presentation.components.FilterAmountCard
import com.feature_transactions.presentation.components.FilterRow
import com.feature_transactions.presentation.components.FilterWidget
import com.feature_transactions.presentation.components.PeriodFilterWidget
import com.feature_transactions.presentation.components.SectionsFilterWidget
import com.feature_transactions.presentation.components.transactionDay
import com.feature_transactions.presentation.util.TransactionScreenEvents
import com.feature_transactions.presentation.util.TransactionState
import com.feature_transactions.presentation.util.TransactionsUiEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    transactionState: TransactionState,
    transactionScreenEvents: (TransactionScreenEvents) -> Unit,
    transactionsUiEvents: SharedFlow<TransactionsUiEvents>,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var filterModalSheetVisibility by remember{ mutableStateOf(false) }

//FOR LAUNCHED EFFECT
    val snackbarHostState = remember { SnackbarHostState() }
    val unknownError = stringResource(R.string.something_goes_wrong_trye_again)

    LaunchedEffect(key1 = true){
        transactionsUiEvents.collectLatest { event ->
            when(event) {
                is TransactionsUiEvents.CloseFilterBottomSheet -> {
                    filterModalSheetVisibility = false
                }
                is TransactionsUiEvents.ErrorMsgUnknown -> {
                    snackbarHostState.showSnackbar(
                        message = unknownError,
                        actionLabel = null
                    )
                }


            }
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    MaterialTheme.colorScheme.background
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                item {
                    FilterAmountCard(
                        amount = transactionState.filterAmount,
                        currency = transactionState.selectedCurrency,
                        filterState = transactionState.filterState
                    )
                }
                item {
                    SearchInput(
                        modifier= Modifier,
                        text = transactionState.searchText,
                        onCancelClicked = {
                            transactionScreenEvents(TransactionScreenEvents.OnSearchCancelClicked)
                        },
                        onTextChanged = { text ->
                            transactionScreenEvents(
                                TransactionScreenEvents.OnSearchTextChanged(text))
                        }
                    )
                }
                item {
                    FilterRow(
                        onFilterBtnClick = {
                            filterModalSheetVisibility = true
                        }
                    )
                }

            if(transactionState.isDownloading) {
                item{
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

            } else {
                transactionState.transactionsByMonth.forEach { month ->
                    val showMonth = month.month in transactionState.filterState.periodFilterState.months
                    if(showMonth){

                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Text(text= Month.of(month.month).name)
                                MoneyText(amount = month.amount, currency = month.currency)
                            }
                        }
                        val pattern = "dd.MM"
                        val format = DateTimeFormatter.ofPattern(pattern)
                        month.daysList.forEach { days ->
                            transactionDay(
                                title = days.date.format(format) ?: pattern,
                                listOfItems = days.transactions
                            )
                        }
                    }

                }
            }


        }
        
        if(filterModalSheetVisibility){
            ModalBottomSheet(
                onDismissRequest = { filterModalSheetVisibility = false },
                sheetState = sheetState
            ) {
                FilterWidget(
                    periodFilterState = transactionState.filterState.periodFilterState,
                    categoryFilterState = transactionState.filterState.categoryFilterState,
                    sectionsFilterState = transactionState.filterState.sectionsFilterState,
                    onApplyBtnClick = { periodFilterState, categoryFilterState, sectionsFilterState ->
                        transactionScreenEvents(TransactionScreenEvents.OnApplyFilterChanges(
                            periodFilterState=periodFilterState, categoryFilterState=categoryFilterState, sectionsFilterState=sectionsFilterState
                        ))
                    }
                )
            }
        }

    }
}