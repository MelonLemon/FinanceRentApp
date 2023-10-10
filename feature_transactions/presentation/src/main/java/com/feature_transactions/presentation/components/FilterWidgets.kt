package com.feature_transactions.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.components.CloseNavigationRow
import com.core.common.components.IconCard
import com.core.common.components.MarkedInfoDisplay
import com.core.common.components.MoneyText
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.core.common.util.SimpleItem
import com.feature_transactions.presentation.R
import com.feature_transactions.presentation.util.CategoryFilterState
import com.feature_transactions.presentation.util.FilterState
import com.feature_transactions.presentation.util.PeriodFilterState
import com.feature_transactions.presentation.util.SectionsFilterState
import com.feature_transactions.presentation.util.TransactionScreenEvents
import kotlinx.coroutines.launch
import java.time.Month
import java.util.Currency

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterAmountCard(
    modifier: Modifier = Modifier,
    amount: Int,
    currency: Currency,
    filterState: FilterState
) {
    val listOfFilters = mutableListOf<String>(
        "${filterState.periodFilterState.selectedYear}",

    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ){
            MoneyText(amount = amount, currency = currency)
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOfFilters.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
        }
    }
}

@Composable
fun FilterRow(
    onFilterBtnClick: () -> Unit
) {
    val listOfFilters = listOf(stringResource(R.string.period), stringResource(R.string.sections), stringResource(R.string.categories))
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        item{
          IconButton(onClick = onFilterBtnClick) {
              Icon(imageVector = ImageVector.vectorResource(id = R.drawable.filter_pieces), contentDescription = null)
          }
        }
        items(3){
            MarkedInfoDisplay(text = listOfFilters[it])
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodFilterWidget(
    listOfYear: List<Int>,
    selectedYear: Int,
    isAllMonthsSelected: Boolean,
    selectedMonths: List<Int>,
    onYearClick: (Int) -> Unit,
    onMonthClick: (Int) -> Unit,
    allMonthsClick: () -> Unit
) {
    val listOfMonths = (1..12).toList()
    LazyColumn(){

        item{
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(listOfYear){ year ->
                    FilledTonalButton(
                        onClick = { onYearClick(year) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if(year==selectedYear) MaterialTheme.colorScheme.primaryContainer else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if(year==selectedYear) MaterialTheme.colorScheme.onPrimaryContainer else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(text="$year")
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        allMonthsClick()
                    }
                    .background(if (isAllMonthsSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isAllMonthsSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.select_all),
                    color = if (isAllMonthsSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(listOfMonths){ month ->
            val isMonthSelected = isAllMonthsSelected || (month in selectedMonths)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onMonthClick(month)
                    }
                    .background(if (isMonthSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(month in selectedMonths){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = if (isMonthSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= Month.of(month).name,
                    color = if (isMonthSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SectionsFilterWidget(
    listOfSections: List<SimpleItem>,
    listOfSelectedSecIds: List<Int>,
    isAllSelected: Boolean,
    isFlatSelected: Boolean,
    isAllSectionsSelected: Boolean,
    onAllClick: () -> Unit,
    onFlatClick: () -> Unit,
    onAllSectionClick: () -> Unit,
    onSectionClick: (Int) -> Unit
) {
    LazyColumn(){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllClick()
                    }
                    .background(if (isAllSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isAllSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.select_all),
                    color = if (isAllSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            val isSelected = isAllSelected || isFlatSelected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFlatClick()
                    }
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.flats),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            val isSelected = isAllSelected || isAllSectionsSelected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllSectionClick()
                    }
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.select_all_sections),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(listOfSections){ section ->
            val isSelected = isAllSelected || isAllSectionsSelected || (section.id in listOfSelectedSecIds)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSectionClick(section.id)
                    }
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= section.name,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesFilterWidget(
    isAllSelected: Boolean,
    isAllIncomeSelected: Boolean,
    isAllExpensesSelected: Boolean,
    selectedIncomeCatId: List<Int>,
    selectedExpensesCatId: List<Int>,
    incomeCategories: Array<IncomeCategories>,
    expensesCategories: Array<ExpensesCategories>,
    onAllClick: () -> Unit,
    onAllIncomeClick: () -> Unit,
    onAllExpensesClick: () -> Unit,
    onIncomeCatClick: (Int) -> Unit,
    onExpensesCatClick: (Int) -> Unit
) {


    LazyColumn(){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllClick()
                    }
                    .background(if (isAllSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isAllSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.select_all),
                    color = if (isAllSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            val isSelected = isAllSelected || isAllIncomeSelected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllIncomeClick()
                    }
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.income_categories),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                incomeCategories.forEach{ item ->
                    val nameString = stringResource(item.category)
                    IconCard(
                        icon = ImageVector.vectorResource(item.icon),
                        onCardClick = {
                            onIncomeCatClick(item.id)
                        },
                        supportingText = nameString,
                        isSelected = item.id in selectedIncomeCatId
                    )
                }
            }
        }
        item {
            val isSelected = isAllSelected || isAllExpensesSelected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllExpensesClick()
                    }
                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isSelected){
                    Icon(imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text= stringResource(R.string.expenses_categories),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                expensesCategories.forEach{ item ->
                    val nameString = stringResource(item.category)
                    IconCard(
                        icon = ImageVector.vectorResource(item.icon),
                        onCardClick = {
                            onExpensesCatClick(item.id)
                        },
                        supportingText = nameString,
                        isSelected = item.id in selectedExpensesCatId
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterWidget(
    periodFilterState: PeriodFilterState,
    categoryFilterState: CategoryFilterState,
    sectionsFilterState: SectionsFilterState,
    onApplyBtnClick: (PeriodFilterState, CategoryFilterState, SectionsFilterState) -> Unit
    ) {
    var tempPeriodFilterState by remember{ mutableStateOf(periodFilterState) }
    var tempCategoryFilterState by remember{ mutableStateOf(categoryFilterState)}
    var tempSectionsFilterState by remember{ mutableStateOf(sectionsFilterState)}
    val incomeCategories = IncomeCategories.values()
    val expensesCategories = ExpensesCategories.values()
    Column() {
        CloseNavigationRow(
            text = stringResource(R.string.filters),
            onBtnClick = {
                onApplyBtnClick(
                    tempPeriodFilterState, tempCategoryFilterState, tempSectionsFilterState
                )
            },
            btnText = stringResource(R.string.apply)
        )
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
        )  {
            item {
                PeriodFilterWidget(
                    listOfYear = tempPeriodFilterState.years,
                    onYearClick = {year ->
                        tempPeriodFilterState = tempPeriodFilterState.copy(
                            selectedYear = year
                        )
                    },
                    onMonthClick = {month ->
                        val newList = tempPeriodFilterState.months.toMutableList()
                        if(month in newList) newList.remove(month) else newList.add(month)
                        tempPeriodFilterState = tempPeriodFilterState.copy(
                            months = newList
                        )

                    },
                    selectedMonths = tempPeriodFilterState.months,
                    selectedYear = tempPeriodFilterState.selectedYear,
                    isAllMonthsSelected = tempPeriodFilterState.isAllMonthsSelected,
                    allMonthsClick = {
                        tempPeriodFilterState = tempPeriodFilterState.copy(
                            isAllMonthsSelected = !tempPeriodFilterState.isAllMonthsSelected
                        )
                    }
                )
            }
            item{
                SectionsFilterWidget(
                    isAllSelected=tempSectionsFilterState.isAllSelected,
                    isFlatSelected=tempSectionsFilterState.isFlatSelected,
                    isAllSectionsSelected=tempSectionsFilterState.isAllSectionsSelected,
                    listOfSections=tempSectionsFilterState.listOfSections,
                    listOfSelectedSecIds=tempSectionsFilterState.listOfSelectedSecIds,
                    onAllClick= {
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            isAllSelected = !tempSectionsFilterState.isAllSelected,
                            isAllSectionsSelected = !tempSectionsFilterState.isAllSelected,
                            isFlatSelected = !tempSectionsFilterState.isAllSelected,
                            listOfSelectedFlatIds = if(!tempSectionsFilterState.isAllSelected) tempSectionsFilterState.listOfFlats.map{it.id}
                            else emptyList(),
                            listOfSelectedSecIds = if(!tempSectionsFilterState.isAllSelected) tempSectionsFilterState.listOfSections.map{it.id}
                            else emptyList()
                        )
                    },
                    onFlatClick= {
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            isFlatSelected = !tempSectionsFilterState.isFlatSelected,
                            listOfSelectedFlatIds = if(!tempSectionsFilterState.isFlatSelected) tempSectionsFilterState.listOfFlats.map{it.id}
                            else emptyList()
                        )
                    },
                    onAllSectionClick= {
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            isAllSectionsSelected = !tempSectionsFilterState.isAllSectionsSelected,
                            listOfSelectedSecIds = if(!tempSectionsFilterState.isAllSectionsSelected) tempSectionsFilterState.listOfSections.map{it.id}
                            else emptyList()
                        )
                    },
                    onSectionClick= {sectionId ->
                        val newList = tempSectionsFilterState.listOfSelectedSecIds.toMutableList()
                        if(sectionId in newList) newList.remove(sectionId) else newList.add(sectionId)
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            listOfSelectedSecIds = newList
                        )
                    }
                )
            }
            item {
                CategoriesFilterWidget(
                    isAllSelected=tempCategoryFilterState.isAllSelected,
                    isAllIncomeSelected=tempCategoryFilterState.isAllIncomeSelected,
                    isAllExpensesSelected=tempCategoryFilterState.isAllExpensesSelected,
                    selectedIncomeCatId=tempCategoryFilterState.selectedIncomeCatId,
                    selectedExpensesCatId=tempCategoryFilterState.selectedExpensesCatId,
                    incomeCategories = incomeCategories,
                    expensesCategories = expensesCategories,
                    onAllClick= {
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            isAllSelected = !tempCategoryFilterState.isAllSelected,
                            isAllIncomeSelected = !tempCategoryFilterState.isAllSelected,
                            isAllExpensesSelected = !tempCategoryFilterState.isAllSelected
                        )
                    },
                    onAllIncomeClick= {
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            isAllIncomeSelected = !tempCategoryFilterState.isAllIncomeSelected,
                            selectedIncomeCatId = if(!tempCategoryFilterState.isAllIncomeSelected) incomeCategories.map { it.id }
                            else emptyList()
                        )
                    },
                    onAllExpensesClick= {
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            isAllExpensesSelected = !tempCategoryFilterState.isAllExpensesSelected,
                            selectedExpensesCatId = if(!tempCategoryFilterState.isAllExpensesSelected) expensesCategories.map { it.id }
                            else emptyList()
                        )
                    },
                    onIncomeCatClick= {incomeCatId ->
                        val newList = tempCategoryFilterState.selectedIncomeCatId.toMutableList()
                        if(incomeCatId in newList) newList.remove(incomeCatId) else newList.add(incomeCatId)
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            selectedIncomeCatId = newList,
                            isAllIncomeSelected = false
                        )
                    },
                    onExpensesCatClick= {expensesCatId ->
                        val newList = tempCategoryFilterState.selectedExpensesCatId.toMutableList()
                        if(expensesCatId in newList) newList.remove(expensesCatId) else newList.add(expensesCatId)
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            selectedExpensesCatId = newList,
                            isAllExpensesSelected = false
                        )
                    }
                )
            }
        }
    }
}
