package com.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.AddValueWidget
import com.core.common.IconCard
import com.feature_home.domain.model.SectionInfo
import java.util.Currency

fun LazyListScope.sectionBlock(
    sectionInfo: SectionInfo,
    currency: Currency,
    onEditBtnClick: () -> Unit,
    isIncomeSelected: Boolean,
    onIncomeExpClick: (Boolean) -> Unit,
    onCategoryClick: (Int) -> Unit,
    onAddBtnClick: (String) -> Unit
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
    if(isIncomeSelected){
        items(
            items = sectionInfo.incomeCategories,
            key = { section ->
                "${section.id} + ${section.name}"
            }

        ){ income_category ->
            IconCard(
                icon = ImageVector.vectorResource(id = income_category.icon),
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
            IconCard(
                icon = ImageVector.vectorResource(id = expenses_category.icon),
                supportingText = expenses_category.name,
                onCardClick = {
                    onCategoryClick(expenses_category.id)
                },
                isSelected = sectionInfo.selectedCategoryId==expenses_category.id
            )
        }
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
        val category = if(sectionInfo.isIncomeSelected) sectionInfo.incomeCategories.first { it.id==transaction.categoryId}
        else sectionInfo.expensesCategories.first { it.id==transaction.categoryId}
        TransactionCard(
            title = category.name,
            icon = ImageVector.vectorResource(id = category.icon),
            amount = transaction.amount,
            currency = currency
        )
    }

}