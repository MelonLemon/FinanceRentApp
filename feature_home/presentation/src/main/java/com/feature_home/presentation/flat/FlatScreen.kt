package com.feature_home.presentation.flat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.components.AddValueWidget
import com.core.common.components.BackToNavigationRow
import com.core.common.components.IconCard
import com.core.common.components.MoneyText
import com.core.common.components.MonthYearDisplay
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.feature_home.domain.model.FinCategory
import com.feature_home.presentation.R
import com.feature_home.presentation.components.FinResultFlatCard
import com.feature_home.presentation.components.GuestCard
import com.feature_home.presentation.components.GuestInfoBottomSheet
import com.feature_home.presentation.components.IncomeExpensesToggle
import com.feature_home.presentation.components.TransactionCard
import com.feature_home.presentation.components.YearMonthDialog
import com.feature_home.presentation.flat.util.FlatScreenEvents
import com.feature_home.presentation.flat.util.FlatState
import com.feature_home.presentation.flat.util.FlatUiEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlatScreen(
    flatState: FlatState,
    flatEvents: (FlatScreenEvents) -> Unit,
    flatUiEvents: SharedFlow<FlatUiEvents>,
    backToHomeScreen: () -> Unit
) {
    //Basic Category Income
    val basicCategory = FinCategory(
        id=1,
        standard_category_id = 6,
        name= stringResource(R.string.paid_rent)
    )

    //BottomSheet
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var guestModalSheetVisibility by remember{ mutableStateOf(false) }

    //Dialog
    var yearMonthDialogVisibility by remember{ mutableStateOf(false)}

    //FOR LAUNCHED EFFECT
    val snackbarHostState = remember { SnackbarHostState() }
    val unknownError = stringResource(R.string.somethings_goes_wrong_try_again)

    LaunchedEffect(key1 = true){
        flatUiEvents.collectLatest { event ->
            when(event) {
                is FlatUiEvents.OpenGuestDialog -> {
                    guestModalSheetVisibility = true
                }
                is FlatUiEvents.CloseGuestDialog -> {
                    guestModalSheetVisibility = false
                }
                is FlatUiEvents.CloseGuestDialogWithError -> {
                    guestModalSheetVisibility = false
                    snackbarHostState.showSnackbar(
                        message = unknownError,
                        actionLabel = null
                    )
                }
                is FlatUiEvents.CloseYearMonthDialog -> {
                    yearMonthDialogVisibility = false
                }
                is FlatUiEvents.ErrorMsgUnknown -> {
                    snackbarHostState.showSnackbar(
                        message = unknownError,
                        actionLabel = null
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            ),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
        ) { innerPadding ->
        if(flatState.isLoading) {
            //progressbar
        } else {

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.Transparent)
            ) {
                BackToNavigationRow(
                    text=flatState.flatName,
                    onBtnClick = {
                        backToHomeScreen()
                    }
                )
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(
                        Color.Transparent
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item{
                    Spacer(modifier = Modifier.height(30.dp))
                    Column() {
                        MoneyText(
                            amount = flatState.finalAmount,
                            currency = flatState.currencyState.selectedCurrency,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ){
                            items(
                                items = flatState.finResults,
                                key = {finResult ->
                                    "${finResult.month}${finResult.year}"
                                }
                            ){finResultFlat ->
                                FinResultFlatCard(
                                    title = Month.of(finResultFlat.month).name,
                                    paid_amount = finResultFlat.paid_amount ?:0,
                                    unpaid_amount = finResultFlat.unpaid_amount ?:0,
                                    expenses_amount = finResultFlat.expenses_amount ?:0,
                                    currency = flatState.currencyState.selectedCurrency,
                                    rent_percent = finResultFlat.rent_percent ?:0f
                                )
                            }
                        }
                    }
                }
                item {
                    IncomeExpensesToggle(
                        isIncomeSelected = flatState.isIncomeSelected,
                        onBtnClick = {isIncomeSelected ->
                            flatEvents(FlatScreenEvents.OnIncomeExpensesClick(isIncomeSelected))
                        }
                    )
                }
                if(flatState.isIncomeSelected){
                    item{
                        IconCard(
                            icon = ImageVector.vectorResource(id = R.drawable.baseline_person_add_24),
                            onCardClick = {
                                flatEvents(FlatScreenEvents.OpenNewGuestDialog)
                            }
                        )
                    }
                    item {
                        MonthYearDisplay(
                            selectedYearMonth = flatState.yearMonth,
                            onBtnClick = {
                                yearMonthDialogVisibility = true
                            }
                        )
                    }
                    items(
                        items = flatState.guests,
                        key = {guest ->
                            guest.name
                        }
                    ){ guest ->
                        Log.d("Guests", "start_date: ${guest.start_date}, end_date:${guest.end_date}")
                        GuestCard(
                            listInfo = listOf(guest.comment),
                            guest_name = guest.name,
                            amount = guest.for_all_nights,
                            currency = flatState.currencyState.selectedCurrency,
                            is_paid = guest.is_paid,
                            onPaidSwitchChange = { is_paid->
                                flatEvents(FlatScreenEvents.OnPaidSwitchChange(
                                    id = guest.id!!,
                                    is_paid = is_paid
                                ))
                            },
                            onCardClick = {
                                flatEvents(FlatScreenEvents.OpenGuestDialog(guest))
                            },
                            startDate = guest.start_date!!,
                            endDate = guest.end_date!!
                        )
                    }

                }

                if(!flatState.isIncomeSelected){
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            items(
                                items = flatState.expensesCategories,
                                key = { section ->
                                    "${section.id} + ${section.name}"
                                }
                            ){expenses_category ->
                                Log.d("Expenses", "expenses_category:$expenses_category ")

                                val iconExp = ExpensesCategories.getExpIcon(expenses_category.standard_category_id)
                                IconCard(
                                    icon = if(iconExp!=null) ImageVector.vectorResource(id = iconExp) else Icons.Default.Info,
                                    supportingText = expenses_category.name,
                                    onCardClick = {
                                        flatEvents(FlatScreenEvents.OnCategoryClick(expenses_category.id))
                                    },
                                    isSelected = flatState.selectedCategoryId==expenses_category.id
                                )
                            }
                        }
                    }


                    item {
                        MonthYearDisplay(
                            selectedYearMonth = flatState.yearMonth,
                            onBtnClick = {
                                yearMonthDialogVisibility = true
                            }
                        )
                    }

                    item{
                        AddValueWidget(
                            onAddBtnClick = {amountString->
                                val amountInt = amountString.toIntOrNull() ?:0
                                if(amountInt!=0){
                                    flatEvents(FlatScreenEvents.OnTransactionAdd(amountInt))
                                }
                            }
                        )
                    }
                }
                item{
                    Text(text= stringResource(R.string.transactions))
                }
                items(
                    items = flatState.transactionsDisplay,
                    key = { transaction ->
                        "${transaction.id}EXP"
                    }
                ){transaction ->
                    Log.d("Expenses", "transaction:$transaction ")
                    Log.d("Expenses", "flatState.expensesCategories:${flatState.expensesCategories} ")

                    val category = if(transaction.isIncome) basicCategory else flatState.expensesCategories.first { it.id==transaction.categoryId}
                    val icon = if(transaction.isIncome) IncomeCategories.getIncIcon(category.standard_category_id) else
                        ExpensesCategories.getExpIcon(category.standard_category_id)
                    TransactionCard(
                        title = category.name,
                        icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
                        amount = transaction.amount,
                        currency = flatState.currencyState.selectedCurrency,
                    )
                }

            }
                //Modal Sheet
                if(guestModalSheetVisibility){
                    ModalBottomSheet(
                        onDismissRequest = {
                            guestModalSheetVisibility = false
                        },
                        sheetState = sheetState
                    ) {
                        GuestInfoBottomSheet(
                            onCancel = {
                                guestModalSheetVisibility = false
                            },
                            onAgree = { guestInfo ->
                                flatEvents(FlatScreenEvents.OnGuestAddEdit(guestInfo))
                            },
                            fullGuestInfo = flatState.guestDialogGuestInfo,
                            listDates = flatState.listRentDates
                        )
                    }

                }

                //Dialog
                if(yearMonthDialogVisibility){
                    YearMonthDialog(
                        selectedYearMonth = flatState.yearMonth,
                        onCancel = {
                            yearMonthDialogVisibility = false
                        },
                        onAgree = {newYearMonth ->
                            flatEvents(FlatScreenEvents.OnYearMonthChange(newYearMonth))
                        }
                    )
                }
            }
        }
    }


}