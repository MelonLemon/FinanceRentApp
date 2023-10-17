package com.feature_transactions.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import java.time.Month
import java.util.Currency

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterAmountCard(
    modifier: Modifier = Modifier,
    amount: Int,
    currency: Currency,
    filterState: FilterState
) {
    val incomeCategories = IncomeCategories.values()
    val expensesCategories = ExpensesCategories.values()
    val listOfPeriodFilters = mutableListOf<String>(
        "${filterState.periodFilterState.selectedYear}"
    )
    if(!filterState.periodFilterState.isAllMonthsSelected){
        filterState.periodFilterState.months.forEach {month->
            listOfPeriodFilters.add(
                Month.of(month).name
            )
        }
    }
    val listOfSecFilters = if(filterState.sectionsFilterState.isAllSelected) listOf<String>("All Selected") else
        filterState.sectionsFilterState.listOfBlocks.filter { it.id in filterState.sectionsFilterState.listOfSelectedBlIds }.map{it.name}

    val listOfCatFilters = mutableListOf<String>(
        if (filterState.categoryFilterState.isAllSelected) stringResource(R.string.all_selected) else "",
        if (!filterState.categoryFilterState.isAllSelected && filterState.categoryFilterState.isAllIncomeSelected) stringResource(R.string.all_income) else "",
        if (!filterState.categoryFilterState.isAllSelected && filterState.categoryFilterState.isAllExpensesSelected) stringResource(R.string.all_expenses) else "",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ){
            MoneyText(amount = amount, currency = currency)
            Text(text=stringResource(R.string.period))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOfPeriodFilters.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
            Text(text=stringResource(R.string.sections))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOfSecFilters.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
            Text(text=stringResource(R.string.categories))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOfCatFilters.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
            if(!filterState.categoryFilterState.isAllSelected &&  !filterState.categoryFilterState.isAllIncomeSelected){
                Text(text=stringResource(R.string.income_categories))
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val incomeCatList = incomeCategories.filter { it.id in filterState.categoryFilterState.selectedIncomeCatId }.map { it.category }
                    incomeCatList.forEach { stringId ->
                        MarkedInfoDisplay(text=stringResource(stringId))
                    }
                }
            }
            if(!filterState.categoryFilterState.isAllSelected && !filterState.categoryFilterState.isAllExpensesSelected){
                Text(text=stringResource(R.string.expenses_categories))
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val expensesCatList = expensesCategories.filter { it.id in filterState.categoryFilterState.selectedExpensesCatId }.map { it.category }
                    expensesCatList.forEach { stringId ->
                        MarkedInfoDisplay(text=stringResource(stringId))
                    }
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
                    },
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                if(isMonthSelected){
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
    listOfBlocks: List<SimpleItem>,
    listOfSelectedBlIds: List<Int>,
    isAllSelected: Boolean,
    onAllClick: () -> Unit,
    onBlockClick: (Int) -> Unit
) {
    LazyColumn(){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAllClick()
                    },
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

        items(listOfBlocks){ section ->
            val isSelected = isAllSelected || (section.id in listOfSelectedBlIds)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onBlockClick(section.id)
                    },
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
                    },
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
                    },
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
                        modifier=Modifier.size(102.dp),
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
                    },
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
                        modifier=Modifier.size(102.dp),
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

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterWidget(
    modifier: Modifier=Modifier,
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
    val pagerState = rememberPagerState(pageCount = {
        3
    })

    val fullYearMonths = (1..12).toMutableList()
    Column(
        modifier= modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(min = 500.dp)
    ) {
        CloseNavigationRow(
            text = stringResource(R.string.filters),
            onBtnClick = {
                onApplyBtnClick(
                    tempPeriodFilterState, tempCategoryFilterState, tempSectionsFilterState
                )
            },
            btnText = stringResource(R.string.apply)
        )
        HorizontalPager(state=pagerState){ page ->
            if(page==0){
                PeriodFilterWidget(
                    listOfYear = tempPeriodFilterState.years,
                    onYearClick = {year ->
                        tempPeriodFilterState = tempPeriodFilterState.copy(
                            selectedYear = year
                        )
                    },
                    onMonthClick = {month ->
                        val newList = if(tempPeriodFilterState.isAllMonthsSelected) fullYearMonths else tempPeriodFilterState.months.toMutableList()
                        if(month in newList) newList.remove(month) else newList.add(month)
                        tempPeriodFilterState = tempPeriodFilterState.copy(
                            months = newList,
                            isAllMonthsSelected = newList.size==12
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
            if(page==1){
                SectionsFilterWidget(
                    isAllSelected=tempSectionsFilterState.isAllSelected,
                    listOfBlocks=tempSectionsFilterState.listOfBlocks,
                    listOfSelectedBlIds=tempSectionsFilterState.listOfSelectedBlIds,
                    onAllClick= {
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            isAllSelected = !tempSectionsFilterState.isAllSelected,
                            listOfSelectedBlIds = if(!tempSectionsFilterState.isAllSelected) tempSectionsFilterState.listOfBlocks.map{it.id}
                            else emptyList()
                        )
                    },
                    onBlockClick= {sectionId ->
                        val newList = tempSectionsFilterState.listOfSelectedBlIds.toMutableList()
                        if(sectionId in newList) newList.remove(sectionId) else newList.add(sectionId)
                        tempSectionsFilterState = tempSectionsFilterState.copy(
                            listOfSelectedBlIds = newList
                        )
                    }
                )
            }

            if(page==2){
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
                            isAllExpensesSelected = !tempCategoryFilterState.isAllSelected,
                            selectedIncomeCatId = if(!tempCategoryFilterState.isAllIncomeSelected) incomeCategories.map { it.id }
                            else emptyList(),
                            selectedExpensesCatId = if(!tempCategoryFilterState.isAllExpensesSelected) expensesCategories.map { it.id }
                            else emptyList()
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
                            isAllIncomeSelected = false,
                            isAllSelected = false
                        )
                    },
                    onExpensesCatClick= {expensesCatId ->
                        val newList = tempCategoryFilterState.selectedExpensesCatId.toMutableList()
                        if(expensesCatId in newList) newList.remove(expensesCatId) else newList.add(expensesCatId)
                        tempCategoryFilterState = tempCategoryFilterState.copy(
                            selectedExpensesCatId = newList,
                            isAllExpensesSelected = false,
                            isAllSelected = false
                        )
                    }
                )
            }









        }
    }
}
