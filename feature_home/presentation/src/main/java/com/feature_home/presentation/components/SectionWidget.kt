package com.feature_home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.components.AddValueWidget
import com.core.common.components.IconCard
import com.core.common.components.MonthYearDisplay
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.feature_home.domain.model.SectionInfo
import java.time.YearMonth
import java.util.Currency

@RequiresApi(Build.VERSION_CODES.O)
fun LazyListScope.sectionBlock(
    sectionInfo: SectionInfo,
    currency: Currency,
    onEditBtnClick: () -> Unit,
    isIncomeSelected: Boolean,
    onIncomeExpClick: (Boolean) -> Unit,
    onCategoryClick: (Int) -> Unit,
    onAddBtnClick: (String) -> Unit,
    selectedYearMonth: YearMonth,
    onYearMonthClick: () -> Unit
){

    item{
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
           Text(
               text=sectionInfo.name,
               style = MaterialTheme.typography.headlineMedium
           )
            IconButton(
                onClick = onEditBtnClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
    item {
        IncomeExpensesToggle(
            isIncomeSelected = isIncomeSelected,
            onBtnClick = onIncomeExpClick
        )
    }
    item {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            if(isIncomeSelected){
                items(
                    items = sectionInfo.incomeCategories,
                    key = { section ->
                        "${section.id} + ${section.name}"
                    }

                ){ income_category ->
                    val icon = IncomeCategories.getIncIcon(income_category.standard_category_id)
                    IconCard(
                        icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
                        supportingText = income_category.name,
                        onCardClick = {
                            onCategoryClick(income_category.id)
                        },
                        isSelected = sectionInfo.selectedCategoryId==income_category.id
                    )
                }
            } else {
                items(
                    items = sectionInfo.expensesCategories,
                    key = { section ->
                        "${section.id} + ${section.name}"
                    }
                ){expenses_category ->
                    val icon = ExpensesCategories.getExpIcon(expenses_category.standard_category_id)
                    IconCard(
                        icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
                        supportingText = expenses_category.name,
                        onCardClick = {
                            onCategoryClick(expenses_category.id)
                        },
                        isSelected = sectionInfo.selectedCategoryId==expenses_category.id
                    )
                }
            }
        }
    }



    item {
        MonthYearDisplay(
          selectedYearMonth = selectedYearMonth,
            onBtnClick = onYearMonthClick
        )
    }
    item{
        AddValueWidget(
            onAddBtnClick = {value->
                onAddBtnClick(value)
            }
        )
    }

    items(
        items = sectionInfo.transactionsDisplay,
        key = { transaction ->
            "${transaction.id} + ${sectionInfo.name}"
        }
    ){transaction ->

        val category = if(transaction.isIncome) sectionInfo.incomeCategories.first { it.id==transaction.categoryId}
        else sectionInfo.expensesCategories.first { it.id==transaction.categoryId}
        val icon = if(transaction.isIncome) IncomeCategories.getIncIcon(category.standard_category_id) else
            ExpensesCategories.getExpIcon(category.standard_category_id)

        TransactionCard(
            title = category.name,
            icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info,
            amount = transaction.amount,
            currency = currency
        )
    }

}