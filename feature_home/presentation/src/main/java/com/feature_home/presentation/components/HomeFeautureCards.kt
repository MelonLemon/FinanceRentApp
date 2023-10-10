package com.feature_home.presentation.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.common.components.CircleIcon
import com.core.common.components.EmptyContainer
import com.core.common.components.IconCard
import com.core.common.components.MarkedInfoDisplay
import com.core.common.components.MoneyText
import com.feature_home.presentation.R
import java.util.Currency


@Composable
fun RentPercentWidget(
    rent_percent: Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        MarkedInfoDisplay(text= stringResource(R.string.rent_percent) + " ${rent_percent*100}%")
        LinearProgressIndicator(
            progress = rent_percent,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FinResultFlatCard(
    modifier:Modifier= Modifier,
    icon: ImageVector?=null,
    title: String,
    paid_amount: Int,
    unpaid_amount: Int,
    expenses_amount: Int,
    currency: Currency,
    rent_percent: Float
) {
    val finResult = expenses_amount-paid_amount
    EmptyContainer(modifier=modifier){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
                if(icon!=null){
                    CircleIcon(
                        backgroundColor = MaterialTheme.colorScheme.onSurface,
                        icon = icon
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text=title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    MoneyText(
                        amount = finResult,
                        currency = currency,
                        style = MaterialTheme.typography.titleMedium,
                        color = if(finResult>0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    MarkedInfoDisplay(text= stringResource(R.string.income))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        MarkedInfoDisplay(text= stringResource(R.string.paid))
                        MoneyText(
                            amount=paid_amount,
                            currency= currency
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        MarkedInfoDisplay(text= stringResource(R.string.unpaid))
                        MoneyText(
                            amount=unpaid_amount,
                            currency= currency
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    MarkedInfoDisplay(text= stringResource(R.string.expenses))
                    MoneyText(
                        amount=expenses_amount,
                        currency= currency
                    )
                }
            }
            RentPercentWidget(
                rent_percent = rent_percent
            )
        }
    }
}





@Composable
fun FinResultCatCard(
    modifier:Modifier= Modifier,
    icon: ImageVector,
    amount: Int,
    currency: Currency
) {
    EmptyContainer(modifier=modifier){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            CircleIcon(
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                icon = icon
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    MarkedInfoDisplay(text= if(amount>0) stringResource(R.string.profit)
                    else stringResource(R.string.loss))
                    MoneyText(
                        amount=amount,
                        currency= currency,
                        color = if(amount>0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.errorContainer
                    )
                }
//                Button(
//                    onClick = { },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Transparent
//                    )
//                ) {
//                    Text(
//                        text = "${trend_Percent}%",
//                        color = if(trend_Percent>0) MaterialTheme.colorScheme.primary
//                        else MaterialTheme.colorScheme.errorContainer
//                    )
//                    Icon(imageVector = if(trend_Percent>0) ImageVector.vectorResource(id = R.drawable.baseline_trending_up_24) else
//                        ImageVector.vectorResource(id = R.drawable.baseline_trending_down_24),
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//
//                }
            }
        }}
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlatCard(
    modifier:Modifier= Modifier,
    title: String,
    amount: Int,
    currency: Currency,
    listInfo: List<String>,
    rent_percent: Float,
    onCardClick: () -> Unit
) {
    EmptyContainer(modifier=modifier.clickable{
        onCardClick()
    }){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text=title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                MoneyText(
                    amount = amount,
                    currency = currency,
                    style = MaterialTheme.typography.titleMedium,
                    color = if(amount>0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.errorContainer
                )
            }
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listInfo.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }

            RentPercentWidget(
                rent_percent = rent_percent
            )
        }}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuestCard(
    modifier:Modifier= Modifier,
    listInfo: List<String>,
    guest_name: String,
    amount: Int,
    currency: Currency,
    is_paid: Boolean,
    onPaidSwitchChange: (Boolean) -> Unit,
    onCardClick: () -> Unit
) {
    EmptyContainer(modifier=modifier.clickable {
        onCardClick()
    }){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text=guest_name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listInfo.forEach { info ->
                    MarkedInfoDisplay(text=info)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MoneyText(
                    amount=amount,
                    currency= currency,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Switch(
                        checked = is_paid,
                        onCheckedChange = onPaidSwitchChange)
                    Text(
                        text=if(is_paid) stringResource(R.string.paid) else stringResource(R.string.paid),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }

        }
    }
}

@Composable
fun TransactionCard(
    modifier:Modifier= Modifier,
    title: String,
    amount: Int,
    currency: Currency,
    icon: ImageVector
) {
    EmptyContainer(modifier=modifier){
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconCard(icon = icon)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text=title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    MoneyText(
                        amount = amount,
                        currency = currency,
                        style = MaterialTheme.typography.titleMedium,
                        color = if(amount>0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.errorContainer
                    )
                }

            }
        }
    }
}
