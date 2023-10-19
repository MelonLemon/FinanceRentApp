package com.feature_home.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature_home.presentation.home.util.HomeScreenEvents
import com.feature_home.presentation.home.util.HomeState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.core.common.components.IconCard
import com.core.common.components.MoneyText
import com.feature_home.domain.model.TransactionInfo
import com.feature_home.presentation.R
import com.feature_home.presentation.components.FinResultCatCard
import com.feature_home.presentation.components.FinResultFlatCard
import com.feature_home.presentation.components.FlatCard
import com.feature_home.presentation.components.NewFlatDialog
import com.feature_home.presentation.components.SectionBottomSheet
import com.feature_home.presentation.components.SettingsBottomSheet
import com.feature_home.presentation.components.YearMonthDialog
import com.feature_home.presentation.components.sectionBlock
import com.feature_home.presentation.home.util.HomeUiEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeState: HomeState,
    homeEvents: (HomeScreenEvents) -> Unit,
    homeUiEvents: SharedFlow<HomeUiEvents>,
    toFlatScreen: (Int) -> Unit
) {
    //Dialog States
    var settingsBottomSheetVisibility by remember{ mutableStateOf(false)}
    var newFlatDialogVisibility by remember{ mutableStateOf(false)}
    var sectionBottomSheetVisibility by remember{ mutableStateOf(false)}
    var yearMonthDialogVisibility by remember{ mutableStateOf(false)}

    //BottomSheet
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    //FOR LAUNCHED EFFECT
    val snackbarHostState = remember { SnackbarHostState() }
    val unknownError = stringResource(R.string.somethings_goes_wrong_try_again)

    LaunchedEffect(key1 = true){
        homeUiEvents.collectLatest { event ->
            when(event) {
                is HomeUiEvents.CloseSettingsDialog -> {
                    showBottomSheet = false
                    settingsBottomSheetVisibility = false
                }
                is HomeUiEvents.CloseNewFlatDialog -> {
                    newFlatDialogVisibility = false
                }
                is HomeUiEvents.OpenSectionDialog -> {
                    sectionBottomSheetVisibility = true
                    showBottomSheet = true
                }
                is HomeUiEvents.CloseSectionDialog -> {
                    showBottomSheet = false
                    sectionBottomSheetVisibility = false
                }
                is HomeUiEvents.CloseYearMonthDialog -> {
                    yearMonthDialogVisibility = false
                }
                is HomeUiEvents.ErrorMsgUnknown -> {
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
        if(homeState.isLoading) {
            //progressbar
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item{
                    Spacer(modifier = Modifier.height(30.dp))
                    Column() {
                        MoneyText(
                            amount = homeState.finState.finalAmount ?:0,
                            currency = homeState.currencyState.selectedCurrency,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        LazyRow(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ){
                            item {
                                FinResultFlatCard(
                                    icon = Icons.Default.Home,
                                    title = stringResource(R.string.flats),
                                    paidAmount = homeState.finState.finResultFlat.paid_amount ?:0,
                                    unpaidAmount = homeState.finState.finResultFlat.unpaid_amount ?:0,
                                    expensesAmount = homeState.finState.finResultFlat.expenses_amount ?:0,
                                    currency = homeState.currencyState.selectedCurrency
                                )
                            }
                            items(
                                items = homeState.finState.finResultsSections,
                                key = {finResSection ->
                                    "${finResSection.id}SECTION"
                                }
                            ){ finResSection ->
                                FinResultCatCard(
                                    icon = Icons.Default.List,
                                    currency = homeState.currencyState.selectedCurrency,
                                    amount = finResSection.amount ?:0,
                                    title = homeState.listOfSections.find { it.id== finResSection.id}?.name ?: ""
                                )
                            }
                        }
                    }
                }
                item{
                    LazyRow(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .clip(MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            IconCard(
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_add_home_24),
                                onCardClick = {
                                    newFlatDialogVisibility = true
                                },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        item {
                            IconCard(
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_create_new_folder_24),
                                onCardClick = {
                                    homeEvents(HomeScreenEvents.OpenNewSectionDialog)
                                },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        item {
                            IconCard(
                                icon = Icons.Default.Settings,
                                onCardClick = {
                                    settingsBottomSheetVisibility = true
                                    showBottomSheet = true
                                },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){

                        Text(
                            text=stringResource(R.string.flats),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        MoneyText(
                            amount = homeState.listOfFlat.sumOf { it.current_month_amount },
                            currency = homeState.currencyState.selectedCurrency,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                items(
                    items = homeState.listOfFlat,
                    key = { flat ->
                        "${flat.id} + ${flat.name}"
                    }
                ) {flat ->

                    FlatCard(
                        title = flat.name,
                        amount = flat.current_month_amount,
                        currency = homeState.currencyState.selectedCurrency,
                        listInfo = homeState.flatsAdditionalInfo[flat.id] ?: emptyList(),
                        rent_percent = flat.rent_percent,
                        onCardClick = {
                            toFlatScreen(flat.id)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                }

                homeState.listOfSections.forEachIndexed { index, sectionInfo ->
                    sectionBlock(
                        sectionInfo=sectionInfo,
                        currency = homeState.currencyState.selectedCurrency,
                        onEditBtnClick = {
                            homeEvents(HomeScreenEvents.OpenSectionDialog(sectionInfo))
                        },
                        isIncomeSelected = sectionInfo.isIncomeSelected,
                        onIncomeExpClick = {isSelected ->
                            homeEvents(
                                HomeScreenEvents.OnIsIncomeSelectedSection(
                                    index=index,
                                    isIncomeSelected=isSelected
                                ))
                        },
                        onCategoryClick = { selectedCategoryId ->
                            homeEvents(
                                HomeScreenEvents.OnCategoryIdChange(
                                    index=index,
                                    categoryId=selectedCategoryId
                                ))
                        },
                        onAddBtnClick = {amount ->
                            val amountInt = amount.toIntOrNull() ?:0
                            if(amountInt!=0){
                                homeEvents(HomeScreenEvents.OnTransactionAdd(
                                    sectionId = sectionInfo.id!!,
                                    transaction = TransactionInfo(
                                        id = null,
                                        categoryId = sectionInfo.selectedCategoryId!!,
                                        amount = if(sectionInfo.isIncomeSelected) amountInt else -amountInt,
                                        isIncome = sectionInfo.isIncomeSelected
                                    )
                                ))
                            }

                        },
                        selectedYearMonth = homeState.yearMonth,
                        onYearMonthClick = {
                            yearMonthDialogVisibility = true
                        }

                    )
                }
            }
        }

        //Bottom Sheet

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    settingsBottomSheetVisibility = false
                    sectionBottomSheetVisibility = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                if(settingsBottomSheetVisibility){
                    SettingsBottomSheet(
                        onCancel = {
                            showBottomSheet = false
                            settingsBottomSheetVisibility = false
                        },
                        selectedCurrency = homeState.currencyState.selectedCurrency,
                        listCurrency = homeState.currencyState.listCurrency,
                        onAgree = {currency ->
                            homeEvents(HomeScreenEvents.OnCurrencyChange(currency))
                        }
                    )
                }

                if(sectionBottomSheetVisibility){

                    SectionBottomSheet(
                        listOfName = homeState.listOfSections.map{it.name},
                        sectionInfo = homeState.sectionDialogSectionInfo,
                        onCancel = {
                            showBottomSheet = false
                            sectionBottomSheetVisibility = false
                        },
                        onAgree = {newSectionInfo ->
                            homeEvents(HomeScreenEvents.OnSectionAddEdit(newSectionInfo))
                        }
                    )
                }
            }
        }
        //Dialogs


        if(newFlatDialogVisibility){
            NewFlatDialog(
                onCancel = {
                    newFlatDialogVisibility = false
                },
                onAgree = { name ->
                    homeEvents(HomeScreenEvents.OnNewFlatAdd(name=name))
                },
                listOfFlat = homeState.listOfFlat.map{it.name}
            )
        }



        if(yearMonthDialogVisibility){
            YearMonthDialog(
                selectedYearMonth = homeState.yearMonth,
                onCancel = {
                    yearMonthDialogVisibility = false
                },
                onAgree = {newYearMonth ->
                    homeEvents(HomeScreenEvents.OnYearMonthChange(newYearMonth))
                }
            )
        }

    }
}