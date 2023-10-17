package com.feature_transactions.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.core.common.components.CircleIcon
import com.core.common.components.MoneyText
import com.core.common.util.ExpensesCategories
import com.core.common.util.IncomeCategories
import com.feature_transactions.domain.model.TransactionListItem
import java.util.Currency
import java.util.Locale

fun LazyListScope.transactionDay(
    title:String,
    listOfItems: List<TransactionListItem>
){
    item {
        Text(
            text=title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    itemsIndexed(
        items = listOfItems,
        key =  { index, item ->
            "title"+index+item.id
        }
    ){ index, item ->
        val icon = if(item.isIncome) IncomeCategories.getIncIcon(item.standard_category_id) else
            ExpensesCategories.getExpIcon(item.standard_category_id)
        TransactionRow(
            categoryName = item.categoryName,
            comment = item.comment,
            amount = item.amount,
            currency = Currency.getInstance(Locale.US), // item.currency -> Change
            icon = if(icon!=null) ImageVector.vectorResource(id = icon) else Icons.Default.Info
        )
        if(index<listOfItems.lastIndex){
            Divider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    categoryName: String = "",
    comment: String = "",
    amount: Int,
    currency: Currency,
    icon: ImageVector
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIcon(
            icon = icon
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = categoryName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = comment,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        MoneyText(
            amount = amount,
            currency = currency,
            color = if (amount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }

}