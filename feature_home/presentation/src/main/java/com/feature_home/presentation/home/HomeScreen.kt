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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.core.common.IconCard
import com.core.common.MoneyText
import com.feature_home.domain.model.Transaction
import com.feature_home.presentation.R
import com.feature_home.presentation.components.FinResultCatCard
import com.feature_home.presentation.components.FinResultFlatCard
import com.feature_home.presentation.components.FlatCard
import com.feature_home.presentation.components.NewFlatDialog
import com.feature_home.presentation.components.SectionDialog
import com.feature_home.presentation.components.SettingsDialog
import com.feature_home.presentation.components.sectionBlock
import com.feature_home.presentation.home.util.HomeUiEvents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    homeState: HomeState,
    homeEvents: (HomeScreenEvents) -> Unit,
    homeUiEvents: SharedFlow<HomeUiEvents>,
    toFlatScreen: (Int) -> Unit
) {
    //Dialog States
    var settingsDialogVisibility by remember{ mutableStateOf(false)}
    var newFlatDialogVisibility by remember{ mutableStateOf(false)}
    var sectionDialogVisibility by remember{ mutableStateOf(false)}

    LaunchedEffect(key1 = true){
        homeUiEvents.collectLatest { event ->
            when(event) {
                is HomeUiEvents.CloseSettingsDialog -> {
                    settingsDialogVisibility = false
                }
                is HomeUiEvents.CloseNewFlatDialog -> {
                    newFlatDialogVisibility = false
                }
                is HomeUiEvents.OpenSectionDialog -> {
                    sectionDialogVisibility = true
                }
                is HomeUiEvents.CloseSectionDialog -> {
                    sectionDialogVisibility = false
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
                        LazyRow(){
                            item {
                                FinResultFlatCard(
                                    icon = Icons.Default.Home,
                                    title = stringResource(R.string.flats),
                                    paid_amount = homeState.finResultFlat.paid_amount,
                                    unpaid_amount = homeState.finResultFlat.unpaid_amount,
                                    expenses_amount = homeState.finResultFlat.expenses_amount,
                                    currency = homeState.currencyState.selectedCurrency,
                                    rent_percent = homeState.finResultFlat.rent_percent
                                )
                            }
                            items(
                                items = homeState.finResultsSections,
                                key = {finResSection ->
                                    "${finResSection.id}SECTION"
                                }
                            ){ finResSection ->
                                FinResultCatCard(
                                    icon = Icons.Default.List,
                                    currency = homeState.currencyState.selectedCurrency,
                                    amount = finResSection.amount,
                                    trend_Percent = finResSection.trend_Percent
                                )
                            }
                        }
                    }
                }
            }
            item{
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconCard(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_add_home_24),
                        onCardClick = {
                            newFlatDialogVisibility = true
                        }
                    )
                    IconCard(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_create_new_folder_24),
                        onCardClick = {
                            homeEvents(HomeScreenEvents.OpenNewSectionDialog)
                        }
                    )
                    IconCard(
                        icon = Icons.Default.Settings,
                        onCardClick = {
                            settingsDialogVisibility = true
                        }
                    )

                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    Text(text= stringResource(R.string.flats))
                    MoneyText(
                        amount = homeState.listOfFlat.sumOf { it.current_month_amount },
                        currency = homeState.currencyState.selectedCurrency,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
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
                    title = "${flat.city}: ${flat.name}",
                    amount = flat.current_month_amount,
                    currency = homeState.currencyState.selectedCurrency,
                    listInfo = flat.additionalInfo,
                    rent_percent = flat.rent_percent,
                    onCardClick = {
                        toFlatScreen(flat.id)
                    }
                )
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
                                sectionId = sectionInfo.id,
                                transaction = Transaction(
                                    id = null,
                                    categoryId = sectionInfo.selectedCategoryId!!,
                                    amount = amountInt
                                )
                            ))
                        }

                    }

                )
            }
        }
        //Dialogs
        if(settingsDialogVisibility){
            SettingsDialog(
                onCancel = {
                    settingsDialogVisibility = false
                },
                selectedCurrency = homeState.currencyState.selectedCurrency,
                listCurrency = homeState.currencyState.listCurrency,
                onAgree = {currency ->
                    homeEvents(HomeScreenEvents.OnCurrencyChange(currency))
                }
            )
        }

        if(newFlatDialogVisibility){
            NewFlatDialog(
                onCancel = {
                    newFlatDialogVisibility = false
                },
                onAgree = { name, city ->
                    homeEvents(HomeScreenEvents.OnNewFlatAdd(name=name,city=city))
                },
                listOfFlat = homeState.listOfFlat.map{it.name}
            )
        }
        if(sectionDialogVisibility){
            val listOfSections = homeState.listOfSections.map{it.name}
            listOfSections.toMutableList().remove(homeState.sectionDialogSectionInfo.name)
            SectionDialog(
                listOfName = listOfSections,
                sectionInfo = homeState.sectionDialogSectionInfo,
                onCancel = {
                    sectionDialogVisibility = false
                },
                onAgree = {newSectionInfo ->
                    homeEvents(HomeScreenEvents.OnSectionAddEdit(newSectionInfo))
                }
            )
        }

    }
}